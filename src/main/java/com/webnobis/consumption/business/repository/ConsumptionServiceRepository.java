package com.webnobis.consumption.business.repository;

import java.time.Month;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
import com.webnobis.consumption.model.transformer.ConsumptionTransformer;
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
	public List<Consumption> getMonthlyAnnualConsumptions(Medium medium, Collection<Integer> years,
			boolean andDecemberYearBefore) {
		Objects.requireNonNull(medium);
		if (Objects.requireNonNull(years).isEmpty()) {
			return Collections.emptyList();
		}

		SortedSet<Integer> yearsWithYearBefore = new TreeSet<>(years);
		yearsWithYearBefore.add(yearsWithYearBefore.first().intValue() - 1);
		Map<Integer, List<Coverage>> coverages = repositoryService.findCoverages().stream()
				.filter(coverage -> medium.equals(coverage.medium()))
				.filter(coverage -> yearsWithYearBefore.contains(coverage.year()))
				.collect(Collectors.groupingBy(Coverage::year));

		// only set if andDecemberYearBefore=true
		Coverage decemberYearBefore = andDecemberYearBefore
				? coverages.get(yearsWithYearBefore.first()).stream()
						.filter(coverage -> Month.DECEMBER.equals(coverage.month())).findFirst().orElse(null)
				: null;

		// mostly the December, but if andDecemberYearBefore=true, the month before
		float initialDialCount = coverages.get(yearsWithYearBefore.first()).stream().sorted(Comparator.reverseOrder())
				.filter(coverage -> !coverage.equals(decemberYearBefore)).findFirst().map(Coverage::dialCount)
				.orElse(0f);
		Function<Coverage, Consumption> transformer = new CoverageToConsumptionTransformer(initialDialCount);
		// calculate consumption also of December year before if set
		List<Consumption> consumptions = Stream
				.concat(andDecemberYearBefore ? Stream.of(decemberYearBefore) : Stream.empty(),
						years.stream().map(coverages::get).flatMap(List::stream))
				.sorted().map(transformer).toList();
		if (!andDecemberYearBefore) {
			return consumptions;
		} else {
			// duplicate Decembers as month from last year
			return Stream
					.concat(consumptions.stream().filter(consumption -> Month.DECEMBER.equals(consumption.month()))
							.map(consumption -> ConsumptionTransformer.toMonthFromLastYear(consumption,
									consumption.year() + 1)),
							consumptions.stream())
					.filter(consumption -> years.contains(consumption.year())).sorted().toList();
		}
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
		return consumptions.stream().reduce((c1, c2) -> new Consumption(last12Month ? -1 : c2.year(), c2.month(),
				c2.medium(), c2.dialCount(), c1.consumption() + c2.consumption())).map(Stream::of)
				.orElseGet(Stream::empty);
	}
}