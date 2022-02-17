package com.webnobis.consumption.business.repository;

import java.time.Month;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.webnobis.consumption.business.ConsumptionService;
import com.webnobis.consumption.model.Consumption;
import com.webnobis.consumption.model.Coverage;
import com.webnobis.consumption.model.Medium;
import com.webnobis.consumption.model.transformer.CoverageToConsumptionTransformer;
import com.webnobis.consumption.repository.RepositoryService;

/**
 * Repository based consumption service
 * 
 * @author steffen
 *
 */
public record ConsumptionServiceRepository(RepositoryService repositoryService) implements ConsumptionService {

	@Override
	public List<Consumption> getMonthlyAnnualConsumptions(Medium medium, Collection<Integer> years) {
		Objects.requireNonNull(medium);
		Objects.requireNonNull(years);
		Map<Integer, List<Coverage>> coverages = repositoryService.findCoverages().stream()
				.filter(coverage -> medium.equals(coverage.medium())).collect(Collectors.groupingBy(Coverage::year));

		List<Integer> tmpList = coverages.keySet().stream().sorted().toList();
		Integer yearBefore = years.stream().sorted().findFirst().map(tmpList::indexOf)
				.map(index -> Math.max(0, index - 1)).map(tmpList::get).orElse(null);
		if (yearBefore == null) {
			return Collections.emptyList();
		}

		Function<Coverage, Consumption> transformer = new CoverageToConsumptionTransformer(coverages.get(yearBefore)
				.stream().sorted(Comparator.reverseOrder()).findFirst().map(Coverage::dialCount).orElse(0f));
		return coverages.entrySet().stream().filter(entry -> years.contains(entry.getKey()))
				.flatMap(entry -> entry.getValue().stream()).map(transformer).sorted().toList();
	}

	@Override
	public List<Consumption> getAnnualConsumptions(Medium medium, Collection<Integer> years, boolean andLast12Month) {
		List<Consumption> tmpList = getMonthlyAnnualConsumptions(medium, years);
		return Stream.concat(getAnnualConsumptions(tmpList),
				andLast12Month ? getLast12MonthConsumptions(tmpList) : Stream.empty()).toList();
	}

	private Stream<Consumption> getAnnualConsumptions(List<Consumption> tmpList) {
		return tmpList.stream().collect(Collectors.groupingBy(Consumption::year)).values().stream()
				.flatMap(consumptions -> toSum(consumptions, false)).sorted();
	}

	private Stream<Consumption> getLast12MonthConsumptions(List<Consumption> tmpList) {
		return toSum(tmpList.subList(Math.max(0, tmpList.size() - Month.values().length), tmpList.size()), true);
	}

	private static Stream<Consumption> toSum(List<Consumption> consumptions, boolean last12Month) {
		return consumptions.stream()
				.reduce((c1, c2) -> new Consumption(last12Month ? -1 : c2.year(), Month.DECEMBER, c2.medium(),
						Math.max(c1.dialCount(), c2.dialCount()), c1.consumption() + c2.consumption(), false))
				.map(Stream::of).orElseGet(Stream::empty);
	}
}