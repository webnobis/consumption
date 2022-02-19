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
	public List<Consumption> getMonthlyAnnualConsumptions(Medium medium, Collection<Integer> years, boolean andDecemberYearBefore) {
		Objects.requireNonNull(medium);
		if (Objects.requireNonNull(years).isEmpty()) {
			return Collections.emptyList();
		}

		Map<Integer, List<Coverage>> coverages = repositoryService.findCoverages().stream()
				.filter(coverage -> medium.equals(coverage.medium())).collect(Collectors.groupingBy(Coverage::year));
		int minYear = years.stream().sorted().findFirst().orElseThrow();
		float initialDialCount = coverages.keySet().stream().sorted(Comparator.reverseOrder())
				.filter(year -> year < minYear).findFirst().flatMap(yearBefore -> coverages.get(yearBefore).stream()
						.sorted(Comparator.reverseOrder()).map(Coverage::dialCount).findFirst())
				.orElse(0f);
		Function<Coverage, Consumption> transformer = new CoverageToConsumptionTransformer(initialDialCount);
		return years.stream().map(coverages::get).flatMap(List::stream).sorted().map(transformer).toList();
	}

	@Override
	public List<Consumption> getAnnualConsumptions(Medium medium, Collection<Integer> years, boolean andLast12Month) {
		List<Consumption> tmpList = getMonthlyAnnualConsumptions(medium, years, false);
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

	static Stream<Consumption> toSum(List<Consumption> consumptions, boolean last12Month) {
		return consumptions.stream()
				.reduce((c1, c2) -> new Consumption(last12Month ? -1 : c2.year(), c2.month(), c2.medium(),
						c2.dialCount(), c1.consumption() + c2.consumption(), false))
				.map(Stream::of).orElseGet(Stream::empty);
	}
}