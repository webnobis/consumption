package com.webnobis.consumption.business.impl;

import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.webnobis.consumption.business.YearService;
import com.webnobis.consumption.model.Coverage;
import com.webnobis.consumption.repository.RepositoryService;

public record YearServiceImpl(RepositoryService repositoryService) implements YearService {

	@Override
	public Set<Integer> getYears() {
		return getYearsWithMonths().keySet();
	}

	@Override
	public Map<Integer, Set<Month>> getYearsWithMonths() {
		return repositoryService.findCoverages().stream().collect(Collectors.groupingBy(yearMonth -> yearMonth.year()))
				.entrySet().stream()
				.collect(Collectors.toMap(entry -> entry.getKey(), entry -> toMonths(entry.getValue())));
	}

	private Set<Month> toMonths(List<Coverage> coverages) {
		return coverages.stream().map(coverage -> coverage.month()).collect(Collectors.toSet());
	}

}
