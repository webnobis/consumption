package com.webnobis.consumption.business.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.Month;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

import com.webnobis.consumption.business.CoverageService;
import com.webnobis.consumption.model.Coverage;
import com.webnobis.consumption.model.Medium;
import com.webnobis.consumption.repository.RepositoryService;

import mockit.Expectations;
import mockit.Mocked;

public class CoverageServiceTest {

	@Mocked
	private RepositoryService repositoryService;

	private CoverageService service;

	@Before
	public void setUp() throws Exception {
		service = new CoverageServiceImpl(repositoryService);
	}

	@Test
	public void testCreateNewCoveragesOfLastMonth() {
		YearMonth yearMonth = YearMonth.now().minusMonths(1);
		new Expectations() {
			{
				repositoryService.findCoverages();
				times = 0;
			}
		};

		Set<Coverage> expectedCoverages = Stream.of(Medium.values()).map(medium -> new Coverage(yearMonth, medium)).collect(Collectors.toSet());
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
	}

	@Test
	public void testGetCoverages() {
		int year = 2020;
		Month month = Month.AUGUST;
		Set<Coverage> foundCoverages = createFoundCoverages(year);
		new Expectations() {
			{
				repositoryService.findCoverages();
				result = foundCoverages;
			}
		};
		Set<Coverage> expectedCoverages = foundCoverages.stream().filter(coverage -> (year == coverage.getYear())).filter(coverage -> month.equals(coverage.getMonth())).collect(Collectors.toSet());
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
		Set<Coverage> coverages = new HashSet<>();
		IntStream.of(2000, 1998, expectedYear, 1988).forEach(year -> {
			Stream.of(Month.values()).forEach(month -> {
				Stream.of(Medium.values()).forEach(medium -> {
					coverages.add(new Coverage(year, month, medium, Float.MIN_VALUE));
				});
			});
		});
		return coverages;
	}

	@Test
	public void testStoreCoverages() {
		Collection<Coverage> coverages = Arrays.asList(new Coverage(1988, Month.FEBRUARY, Medium.WASSER, Float.MIN_VALUE), new Coverage(1877, Month.MAY, Medium.STROM, Float.MAX_VALUE));
		new Expectations() {
			{
				repositoryService.storeCoverages(coverages);
			}
		};

		service.storeCoverages(coverages);
	}
}
