package com.webnobis.consumption.business.repository;

import java.time.Month;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.webnobis.consumption.business.ConsumptionService;
import com.webnobis.consumption.model.Consumption;
import com.webnobis.consumption.model.Coverage;
import com.webnobis.consumption.model.Medium;
import com.webnobis.consumption.model.transformer.CoverageToConsumptionTransformer;
import com.webnobis.consumption.repository.RepositoryService;

public record ConsumptionServiceRepository(RepositoryService repositoryService) implements ConsumptionService {

	@Override
	public SortedSet<Consumption> getConsumptions(Medium medium, Collection<Integer> years, boolean monthly) {
		if (monthly) {
			return new TreeSet<>(getMonthlyAnnualConsumptions(medium, years));
		} else {
			return new TreeSet<>(getAnnualConsumptions(medium, years, false));
		}
	}

	@Override
	public List<Consumption> getMonthlyAnnualConsumptions(Medium medium, Collection<Integer> years) {
		Objects.requireNonNull(medium);
		Objects.requireNonNull(years);
		Function<Coverage, Consumption> transformer = new CoverageToConsumptionTransformer();
		return repositoryService.findCoverages().parallelStream()
				.filter(consumption -> medium.equals(consumption.medium()))
				.filter(consumption -> years.contains(consumption.year())).map(transformer).sorted().toList();
	}

	@Override
	public List<Consumption> getAnnualConsumptions(Medium medium, Collection<Integer> years, boolean andLast12Month) {
		return Stream.concat(getAnnualConsumptions(medium, years),
				andLast12Month ? getLast12MonthConsumptions(medium, years) : Stream.empty()).toList();
	}

	private Stream<Consumption> getAnnualConsumptions(Medium medium, Collection<Integer> years) {
		return getMonthlyAnnualConsumptions(medium, years).stream()
				.collect(Collectors.groupingBy(consumption -> consumption.year())).entrySet().stream()
				.map(entry -> toSum(entry.getValue(), medium, entry.getKey()));
	}

	private Stream<Consumption> getLast12MonthConsumptions(Medium medium, Collection<Integer> years) {
		List<Consumption> tmpList = getMonthlyAnnualConsumptions(medium, years);
		return Stream.of(toSum(tmpList.subList(Math.max(0, tmpList.size() - 12), tmpList.size()), medium,
				new TreeSet<>(years).last()));
	}

	private static Consumption toSum(Collection<Consumption> consumptions, Medium missingMedium, int missingYear) {
		return consumptions.parallelStream()
				.reduce((c1, c2) -> new Consumption(c1.year(), Month.DECEMBER, c1.medium(),
						Math.max(c1.dialCount(), c2.dialCount()), c1.consumption() + c2.consumption(), false))
				.orElse(new Consumption(missingYear, Month.DECEMBER, missingMedium, 0, 0, false));
	}
}