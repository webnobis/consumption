package com.webnobis.consumption.business.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.Month;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.webnobis.consumption.business.CoverageService;
import com.webnobis.consumption.model.Coverage;
import com.webnobis.consumption.model.Medium;
import com.webnobis.consumption.repository.RepositoryService;

@ExtendWith(MockitoExtension.class)
class CoverageServiceTest {

	@Mock
	private RepositoryService repositoryService;

	@Captor
	private ArgumentCaptor<Collection<Coverage>> coverageCaptor;

	private CoverageService service;

	@BeforeEach
	void setUp() throws Exception {
		service = new CoverageServiceImpl(repositoryService);
	}

	@Test
	void testCreateNewCoveragesOfLastMonth() {
		YearMonth yearMonth = YearMonth.now().minusMonths(1);

		Set<Coverage> expectedCoverages = Stream.of(Medium.values()).map(medium -> new Coverage(yearMonth, medium))
				.collect(Collectors.toSet());
		assertNotNull(expectedCoverages);
		assertEquals(Medium.values().length, expectedCoverages.size());

		Set<Coverage> coverages = service.createNewCoveragesOfLastMonth();
		assertNotNull(coverages);
		assertEquals(Medium.values().length, coverages.size());

		EnumSet<Medium> mediums = EnumSet.allOf(Medium.class);
		coverages.forEach(coverage -> {
			assertEquals(yearMonth.getYear(), coverage.getYear());
			assertEquals(yearMonth.getMonth(), coverage.getMonth());
			assertTrue(mediums.contains(coverage.getMedium()));
			assertEquals(0f, coverage.getDialCount(), 0f);
		});

		verifyNoInteractions(repositoryService);
	}

	@Test
	void testGetCoverages() {
		int year = 2020;
		Month month = Month.AUGUST;
		Set<Coverage> foundCoverages = createFoundCoverages(year);
		when(repositoryService.findCoverages()).thenReturn(foundCoverages);

		Set<Coverage> expectedCoverages = foundCoverages.stream().filter(coverage -> (year == coverage.getYear()))
				.filter(coverage -> month.equals(coverage.getMonth())).collect(Collectors.toSet());
		assertNotNull(expectedCoverages);
		assertEquals(Medium.values().length, expectedCoverages.size());

		Set<Coverage> coverages = service.getCoverages(year, month);
		assertNotNull(coverages);
		assertEquals(Medium.values().length, coverages.size());

		EnumSet<Medium> mediums = EnumSet.allOf(Medium.class);
		coverages.forEach(coverage -> {
			assertEquals(year, coverage.getYear());
			assertEquals(month, coverage.getMonth());
			assertTrue(mediums.contains(coverage.getMedium()));
		});
	}

	private static Set<Coverage> createFoundCoverages(int expectedYear) {
		Set<Coverage> coverages = IntStream.of(2000, 1998, expectedYear, 1988).boxed()
				.flatMap(year -> Stream.of(Month.values())
						.flatMap(month -> Stream.of(Medium.values())
								.map(medium -> new Coverage(year, month, medium, Float.MIN_VALUE))))
				.collect(Collectors.toSet());
		return coverages;
	}

	@Test
	void testStoreCoverages() {
		Collection<Coverage> coverages = Arrays.asList(
				new Coverage(1988, Month.FEBRUARY, Medium.WASSER, Float.MIN_VALUE),
				new Coverage(1877, Month.MAY, Medium.STROM, Float.MAX_VALUE));

		service.storeCoverages(coverages);

		verify(repositoryService).storeCoverages(coverageCaptor.capture());
		assertEquals(coverages, coverageCaptor.getValue());
	}
}
