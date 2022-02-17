package com.webnobis.consumption.business.repository;

import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.webnobis.consumption.business.YearService;
import com.webnobis.consumption.model.Coverage;
import com.webnobis.consumption.repository.RepositoryService;

/**
 * Repository based year service
 * 
 * @author steffen
 *
 */
public record YearServiceRepository(RepositoryService repositoryService) implements YearService {

	@Override
	public List<Integer> getYears() {
		return repositoryService.findCoverages().stream().map(Coverage::year).distinct().sorted().toList();
	}

	@Override
	public Map<Integer, List<Month>> getYearsWithMonths() {
		return repositoryService.findCoverages().stream().collect(Collectors.groupingBy(Coverage::year)).entrySet()
				.stream().collect(Collectors.toMap(Entry::getKey,
						entry -> entry.getValue().stream().map(Coverage::month).distinct().sorted().toList()));
	}

}
