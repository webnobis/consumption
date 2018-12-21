package com.webnobis.consumption.business.impl;

import java.time.Month;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.webnobis.consumption.business.CoverageService;
import com.webnobis.consumption.model.Coverage;
import com.webnobis.consumption.model.Medium;
import com.webnobis.consumption.repository.RepositoryService;

public class CoverageServiceImpl implements CoverageService {

	private final RepositoryService repositoryService;

	public CoverageServiceImpl(RepositoryService repositoryService) {
		this.repositoryService = Objects.requireNonNull(repositoryService, "repositoryService is null");
	}

	@Override
	public SortedSet<Coverage> createNewCoveragesOfCurrentMonth() {
		YearMonth yearMonth = YearMonth.now();
		return new TreeSet<>(Arrays.stream(Medium.values())
				.map(medium -> new Coverage(yearMonth, medium))
				.collect(Collectors.toSet()));
	}

	@Override
	public SortedSet<Coverage> createNewCoveragesOfLastMonth() {
		YearMonth yearMonth = YearMonth.now().minusMonths(1);
		return new TreeSet<>(Arrays.stream(Medium.values())
				.map(medium -> new Coverage(yearMonth, medium))
				.collect(Collectors.toSet()));
	}

	@Override
	public SortedSet<Coverage> getCoverages(int year, Month month) {
		Objects.requireNonNull(month, "month is null");
		return new TreeSet<>(repositoryService.findCoverages()
				.parallelStream()
				.filter(coverage -> (year == coverage.getYear()))
				.filter(coverage -> (month.equals(coverage.getMonth())))
				.collect(Collectors.toSet()));
	}

	@Override
	public void storeCoverages(Collection<Coverage> coverages) {
		repositoryService.storeCoverages(coverages);
	}

}
