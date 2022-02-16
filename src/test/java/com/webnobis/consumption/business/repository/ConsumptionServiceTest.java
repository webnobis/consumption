package com.webnobis.consumption.business.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.webnobis.consumption.business.ConsumptionService;
import com.webnobis.consumption.business.repository.ConsumptionServiceRepository;
import com.webnobis.consumption.model.Consumption;
import com.webnobis.consumption.model.Coverage;
import com.webnobis.consumption.model.Medium;
import com.webnobis.consumption.repository.RepositoryService;

@ExtendWith(MockitoExtension.class)
class ConsumptionServiceTest {

	private static final Collection<Integer> YEARS = Arrays.asList(1999, 2010, 2002, 2015, 2016, 2003, 1998);

	private static final Collection<Integer> FILTER_YEARS = Arrays.asList(1999, 2015, 2003);

	private static final Medium FILTER_MEDIUM = Medium.STROM;

	private static final int MAX_CONSUMPTION = 2000;

	private static AtomicInteger dialCount;

	private static Set<Coverage> foundCoverages;

	@Mock
	private RepositoryService repositoryService;

	private ConsumptionService service;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		dialCount = new AtomicInteger(21013); // start value
		foundCoverages = createFoundCoverages();
	}

	private static Set<Coverage> createFoundCoverages() {
		Random random = new Random();
		List<Coverage> coverages = new ArrayList<>();
		YEARS.stream().sorted().forEach(year -> {
			Arrays.stream(Month.values()).forEach(month -> {
				coverages.add(
						new Coverage(year, month, FILTER_MEDIUM, dialCount.addAndGet(random.nextInt(MAX_CONSUMPTION))));
			});
		});
		// first and last year not complete
		return new HashSet<>(coverages.subList(4, coverages.size() - 3));
	}

	private static Set<Consumption> toConsumptions(Set<Coverage> coverages) {
		AtomicReference<Float> dialCountBefore = new AtomicReference<>(1f);
		return coverages.stream()
				.map(coverage -> new Consumption(coverage.year(), coverage.month(), FILTER_MEDIUM, coverage.dialCount(),
						coverage.dialCount() - dialCountBefore.getAndSet(coverage.dialCount()), false))
				.collect(Collectors.toSet());
	}

	@BeforeEach
	void setUp() throws Exception {
		service = new ConsumptionServiceRepository(repositoryService);
	}

	@Test
	void testGetYearConsumptions() {
		when(repositoryService.findCoverages()).thenReturn(foundCoverages);

		Map<Integer, List<Consumption>> yearConsumptions = toConsumptions(foundCoverages).stream()
				.filter(consumption -> FILTER_YEARS.contains(consumption.year()))
				.collect(Collectors.groupingBy(consumption -> consumption.year()));
		Set<Consumption> expectedConsumptions = yearConsumptions.values().stream()
				.map(consumptions -> toYearSumConsumption(consumptions)).collect(Collectors.toSet());

		Set<Consumption> consumptions = service.getConsumptions(FILTER_MEDIUM, FILTER_YEARS, false);
		assertNotNull(consumptions);
		assertEquals(FILTER_YEARS.size(), consumptions.size());
		assertTrue(Objects.deepEquals(expectedConsumptions, consumptions));
	}

	private Consumption toYearSumConsumption(List<Consumption> yearConsumptions) {
		Consumption last = new TreeSet<>(yearConsumptions).last();
		float yearSum = (float) yearConsumptions.stream().mapToDouble(consumption -> consumption.consumption()).sum();
		return new Consumption(last.year(), last.month(), last.medium(), last.dialCount(), yearSum, false);
	}

	@Test
	void testGetMonthConsumptions() {
		when(repositoryService.findCoverages()).thenReturn(foundCoverages);

		Set<Consumption> expectedConsumptions = toConsumptions(foundCoverages).stream()
				.filter(consumption -> FILTER_YEARS.contains(consumption.year())).collect(Collectors.toSet());

		Set<Consumption> consumptions = service.getConsumptions(FILTER_MEDIUM, FILTER_YEARS, true);
		assertNotNull(consumptions);
		assertEquals(Month.values().length * FILTER_YEARS.size(), consumptions.size());
		assertTrue(Objects.deepEquals(expectedConsumptions, consumptions));
	}

}
