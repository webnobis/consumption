package com.webnobis.consumption.business.impl;

import java.time.Month;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import com.webnobis.consumption.business.ConsumptionService;
import com.webnobis.consumption.model.Consumption;
import com.webnobis.consumption.model.Medium;
import com.webnobis.consumption.repository.RepositoryService;

public class ConsumptionServiceImpl implements ConsumptionService {

	private static final Logger log = Logger.getLogger(ConsumptionServiceImpl.class);

	private final RepositoryService repositoryService;

	public ConsumptionServiceImpl(RepositoryService repositoryService) {
		this.repositoryService = Objects.requireNonNull(repositoryService, "repositoryService is null");
	}

	@Override
	public SortedSet<Consumption> getConsumptions(Medium medium, Collection<Integer> years, boolean monthly) {
		Objects.requireNonNull(medium, "medium is null");
		Objects.requireNonNull(years, "years is null");
		
		final SortedSet<Consumption> consumptions = getConsumptions(medium, years);
		if (monthly) {
			log.info("found " + consumptions.size() + " monthly consumptions");
			return consumptions;
		} else {
			SortedSet<Integer> yearsWithYearBefore =new TreeSet<>(years);
			yearsWithYearBefore.add(yearsWithYearBefore.first() - 1);
			SortedSet<Consumption> sumConsumptions = Stream.concat(consumptions.stream()
					.map(Consumption::getYear)
					.map(year -> toYearSumConsumption(consumptions, year)), Stream.of(toLast12MonthSumConsumption(getConsumptions(medium, yearsWithYearBefore))))
					.distinct()
					.collect(Collectors.toCollection(TreeSet::new));
			log.info("found " + sumConsumptions.size() + " yearly consumptions");
			return sumConsumptions;
		}
	}
	
	private SortedSet<Consumption> getConsumptions(Medium medium, Collection<Integer> years) {
		if (years.isEmpty()) {
			return Collections.emptySortedSet();
		}
		int lastYear = new TreeSet<>(years).last();
		return repositoryService.findCoverages()
				.stream()
				.filter(coverage -> medium.equals(coverage.getMedium()))
				.filter(coverage -> coverage.getYear() <= lastYear)
				.sorted()
				.map(new CoverageToConsumptionTransformer())
				.filter(consumption -> years.contains(consumption.getYear()))
				.collect(Collectors.toCollection(TreeSet::new));
	}
	
	private Consumption toYearSumConsumption(SortedSet<Consumption> yearConsumptions, int year) {
		Consumption last = yearConsumptions.last();
		float yearSum = (float) yearConsumptions.parallelStream()
			.filter(consumption -> year == consumption.getYear())
			.mapToDouble(consumption -> consumption.getConsumption())
			.sum();
		return new Consumption(year, Month.DECEMBER, last.getMedium(), last.getDialCount(), yearSum, false);
	}
	
	private Consumption toLast12MonthSumConsumption(SortedSet<Consumption> yearConsumptions) {
		Consumption last = yearConsumptions.last();
		float last12MonthSum = (float) yearConsumptions.parallelStream()
			.filter(consumption -> withinLast12MonthRange(consumption, last.getMonth(), last.getYear()))
			.peek(System.out::println)
			.mapToDouble(consumption -> consumption.getConsumption())
			.sum();
		return new Consumption(last.getYear(), last.getMonth(), last.getMedium(), last.getDialCount(), last12MonthSum, false);
	}

	private boolean withinLast12MonthRange(Consumption checkConsumption, Month lastMonth, int lastYear) {
		if (checkConsumption.getYear() == lastYear) {
			return true;
		}
		if (checkConsumption.getYear() == (lastYear - 1)) {
			return checkConsumption.getMonth().compareTo(lastMonth) > 0;
		}
		return false;
	}
	
}