package com.webnobis.consumption.business.impl;

import java.time.Month;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

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
	public Set<Consumption> getConsumptions(Medium medium, Collection<Integer> years, boolean monthly) {
		Objects.requireNonNull(medium, "medium is null");
		Objects.requireNonNull(years, "years is null");
		Set<Consumption> consumptions = getConsumptions(medium, years);
		if (monthly) {
			log.debug("found " + consumptions.size() + " monthly consumptions");
		} else {
			consumptions = consumptions.stream()
					.collect(Collectors.groupingBy(consumption -> consumption.getYear()))
					.values().stream()
					.map(this::toYearSumConsumption)
					.collect(Collectors.toSet());
			log.debug("found " + consumptions.size() + " yearly consumptions");
		}
		return consumptions;
	}
	
	private Set<Consumption> getConsumptions(Medium medium, Collection<Integer> years) {
		int lastYear = new TreeSet<>(years).last();
		return repositoryService.findCoverages()
				.stream()
				.filter(coverage -> medium.equals(coverage.getMedium()))
				.filter(coverage -> coverage.getYear() <= lastYear)
				.sorted()
				.map(new CoverageToConsumptionTransformer())
				.filter(consumption -> years.contains(consumption.getYear()))
				.collect(Collectors.toSet());
	}
	
	private Consumption toYearSumConsumption(List<Consumption> yearConsumptions) {
		Consumption last = new TreeSet<>(yearConsumptions).last();
		float yearSum = (float) yearConsumptions.parallelStream()
			.mapToDouble(consumption -> consumption.getConsumption())
			.sum();
		return new Consumption(last.getYear(), Month.DECEMBER, last.getMedium(), last.getDialCount(), yearSum, false);
	}

}