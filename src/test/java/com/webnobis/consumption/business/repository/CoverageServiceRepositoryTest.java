package com.webnobis.consumption.business.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Month;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.webnobis.consumption.business.CoverageService;
import com.webnobis.consumption.model.Coverage;
import com.webnobis.consumption.model.Medium;
import com.webnobis.consumption.repository.RepositoryService;

class CoverageServiceRepositoryTest {

	private static final Coverage YEAR_1_MONTH_1 = new Coverage(2021, Month.JANUARY, Medium.STROM, 10000.5f);

	private static final Coverage YEAR_1_MONTH_2 = new Coverage(YEAR_1_MONTH_1.year(), YEAR_1_MONTH_1.month(),
			Medium.GAS, 33333.3f);

	private static final Coverage YEAR_1_MONTH_3 = new Coverage(YEAR_1_MONTH_1.year(), YEAR_1_MONTH_1.month(),
			Medium.WASSER, 400f);

	private static final Coverage YEAR_1_MONTH_4 = new Coverage(YEAR_1_MONTH_1.year(), Month.DECEMBER, Medium.WASSER,
			488.8f);

	private static final Coverage YEAR_2_MONTH_1 = new Coverage(2022, Month.JULY, Medium.STROM, 16789.2f);

	private TestRepositoryService repositoryService;

	private CoverageService coverageService;

	@BeforeEach
	void setUp() throws Exception {
		repositoryService = new TestRepositoryService();
		coverageService = new CoverageServiceRepository(repositoryService);
	}

	@Test
	void testCreateNewCoveragesOfCurrentMonth() {
		testCreateNewCoverages(YearMonth.now(), coverageService.createNewCoveragesOfCurrentMonth());
	}

	@Test
	void testCreateNewCoveragesOfLastMonth() {
		testCreateNewCoverages(YearMonth.now().minusMonths(1), coverageService.createNewCoveragesOfLastMonth());
	}

	private void testCreateNewCoverages(YearMonth expected, List<Coverage> coverages) {
		assertNotNull(coverages);
		assertEquals(Medium.values().length, coverages.size());
		IntStream.range(0, Medium.values().length)
				.forEach(i -> assertEquals(Medium.values()[i], coverages.get(i).medium()));
		coverages.forEach(coverage -> {
			assertEquals(expected.getYear(), coverage.year());
			assertEquals(expected.getMonth(), coverage.month());
			assertEquals(0f, coverage.dialCount());
		});
	}

	@Test
	void testGetCoverages() {
		List<Coverage> coverages = coverageService.getCoverages(YEAR_1_MONTH_1.year(), YEAR_1_MONTH_1.month());
		assertEquals(3, coverages.size());
		List<Medium> mediums = Arrays.asList(Medium.values());
		coverages.forEach(coverage -> {
			assertEquals(YEAR_1_MONTH_1.year(), coverage.year());
			assertEquals(YEAR_1_MONTH_1.month(), coverage.month());
			assertTrue(mediums.contains(coverage.medium()));
		});

		coverages = coverageService.getCoverages(YEAR_2_MONTH_1.year(), YEAR_2_MONTH_1.month());
		assertEquals(1, coverages.size());
		Coverage coverage = coverages.iterator().next();
		assertEquals(YEAR_2_MONTH_1.year(), coverage.year());
		assertEquals(YEAR_2_MONTH_1.month(), coverage.month());
		assertEquals(Medium.STROM, coverage.medium());
	}

	@Test
	void testGetCoveragesNotFound() {
		List<Coverage> coverages = coverageService.getCoverages(YEAR_2_MONTH_1.year(), Month.MAY);
		assertTrue(coverages.isEmpty());

		coverages = coverageService.getCoverages(-1, YEAR_1_MONTH_4.month());
		assertTrue(coverages.isEmpty());
	}

	@Test
	void testGetCoveragesNull() {
		assertThrows(NullPointerException.class, () -> coverageService.getCoverages(YEAR_2_MONTH_1.year(), null));
	}

	@Test
	void testStoreCoverages() {
		assertNull(repositoryService.coveragesRef.get());

		Collection<Coverage> coverages = repositoryService.findCoverages();
		coverageService.storeCoverages(coverages);
		assertEquals(repositoryService.coveragesRef.get(), coverages);
	}

	private class TestRepositoryService implements RepositoryService {

		final AtomicReference<Collection<Coverage>> coveragesRef = new AtomicReference<>();

		@Override
		public Set<Coverage> findCoverages() {
			return Stream.of(YEAR_1_MONTH_1, YEAR_2_MONTH_1, YEAR_1_MONTH_2, YEAR_1_MONTH_3, YEAR_1_MONTH_4)
					.collect(Collectors.toSet());
		}

		@Override
		public void storeCoverages(Collection<Coverage> coverages) {
			coveragesRef.set(coverages);
		}

	}

}
