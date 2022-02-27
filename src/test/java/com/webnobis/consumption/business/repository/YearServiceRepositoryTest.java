package com.webnobis.consumption.business.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Month;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.webnobis.consumption.business.YearService;
import com.webnobis.consumption.model.Coverage;
import com.webnobis.consumption.model.Medium;
import com.webnobis.consumption.repository.RepositoryService;

class YearServiceRepositoryTest {

	private static final Coverage YEAR_1_MONTH_1 = new Coverage(2021, Month.AUGUST, Medium.STROM, 11009.3f);

	private static final Coverage YEAR_1_MONTH_2 = new Coverage(2021, Month.AUGUST, Medium.WASSER, 409.8f);

	private static final Coverage YEAR_1_MONTH_3 = new Coverage(2021, Month.NOVEMBER, Medium.STROM, 19022.6f);

	private static final Coverage YEAR_2_MONTH_1 = new Coverage(2022, Month.JULY, Medium.STROM, 21342.2f);

	private static final Coverage YEAR_2_MONTH_2 = new Coverage(2022, Month.JULY, Medium.GAS, 32455.1f);

	private YearService yearService;

	private YearService emptyYearService;

	@BeforeEach
	void setUp() throws Exception {
		yearService = new YearServiceRepository(new TestRepositoryService());
		emptyYearService = new YearServiceRepository(new EmptyRepositoryService());
	}

	@Test
	void testGetYears() {
		List<Integer> years = yearService.getYears();
		assertEquals(2, years.size());
		assertEquals(YEAR_1_MONTH_1.year(), years.get(0));
		assertEquals(YEAR_2_MONTH_1.year(), years.get(1));
	}

	@Test
	void testGetYearsWithMonths() {
		Map<Integer, List<Month>> yearsWithMonths = yearService.getYearsWithMonths();
		assertEquals(2, yearsWithMonths.size());
		List<Month> months1 = yearsWithMonths.get(YEAR_1_MONTH_2.year());
		assertNotNull(months1);
		assertEquals(2, months1.size());
		assertEquals(YEAR_1_MONTH_1.month(), months1.get(0));
		assertEquals(YEAR_1_MONTH_3.month(), months1.get(1));
		List<Month> months2 = yearsWithMonths.get(YEAR_2_MONTH_2.year());
		assertNotNull(months2);
		assertEquals(1, months2.size());
		assertEquals(YEAR_2_MONTH_1.month(), months2.get(0));
	}

	@Test
	void testGetEmpty() {
		assertTrue(emptyYearService.getYears().isEmpty());
		assertTrue(emptyYearService.getYearsWithMonths().isEmpty());
	}

	private class TestRepositoryService implements RepositoryService {

		@Override
		public Set<Coverage> findCoverages() {
			return Stream.of(YEAR_1_MONTH_1, YEAR_2_MONTH_1, YEAR_1_MONTH_2, YEAR_2_MONTH_2, YEAR_1_MONTH_3)
					.collect(Collectors.toSet());
		}

		@Override
		public void storeCoverages(Collection<Coverage> coverages) {
			throw new UnsupportedOperationException();
		}

	}

	private class EmptyRepositoryService implements RepositoryService {

		@Override
		public Set<Coverage> findCoverages() {
			return Collections.emptySet();
		}

		@Override
		public void storeCoverages(Collection<Coverage> coverages) {
			throw new UnsupportedOperationException();
		}

	}

}
