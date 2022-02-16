package com.webnobis.consumption.business.repository;

import java.time.Month;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.webnobis.consumption.business.CoverageService;
import com.webnobis.consumption.model.Coverage;
import com.webnobis.consumption.model.Medium;
import com.webnobis.consumption.repository.RepositoryService;

public record CoverageServiceRepository(RepositoryService repositoryService) implements CoverageService {

	@Override
	public List<Coverage> createNewCoveragesOfCurrentMonth() {
		return createNewCoverages(YearMonth.now());
	}

	@Override
	public List<Coverage> createNewCoveragesOfLastMonth() {
		return createNewCoverages(YearMonth.now().minusMonths(1));
	}

	private List<Coverage> createNewCoverages(YearMonth yearMonth) {
		return Arrays.stream(Medium.values())
				.map(medium -> new Coverage(yearMonth.getYear(), yearMonth.getMonth(), medium, 0)).sorted().toList();
	}

	@Override
	public List<Coverage> getCoverages(int year, Month month) {
		Objects.requireNonNull(month, "month is null");
		return repositoryService.findCoverages().parallelStream().filter(coverage -> year == coverage.year())
				.filter(coverage -> month.equals(coverage.month())).sorted().toList();
	}

	@Override
	public void storeCoverages(Collection<Coverage> coverages) {
		repositoryService.storeCoverages(coverages);
	}

}
