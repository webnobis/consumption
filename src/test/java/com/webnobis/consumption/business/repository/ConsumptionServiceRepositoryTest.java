package com.webnobis.consumption.business.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.Month;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.webnobis.consumption.business.ConsumptionService;
import com.webnobis.consumption.model.Consumption;
import com.webnobis.consumption.model.Coverage;
import com.webnobis.consumption.model.Medium;
import com.webnobis.consumption.model.transformer.CoverageToConsumptionTransformer;
import com.webnobis.consumption.repository.RepositoryService;

class ConsumptionServiceRepositoryTest {

	private static Map<Integer, List<Coverage>> coverages;

	private ConsumptionService consumptionService;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		coverages = IntStream.rangeClosed(1900, 2100).boxed()
				.flatMap(year -> Arrays.stream(Month.values())
						.flatMap(month -> Arrays.stream(Medium.values())
								.map(medium -> new Coverage(year, month, medium,
										DialCountBuilder.getDialCount(year, month, medium)))))
				.collect(Collectors.groupingBy(Coverage::year));
	}

	@BeforeEach
	void setUp() throws Exception {
		consumptionService = new ConsumptionServiceRepository(new TestRepositoryService());
	}

	@Test
	void testToSum() {
		int year = 1988;
		float dialCountBefore = 1500f;
		Function<Coverage, Consumption> transformer = new CoverageToConsumptionTransformer(dialCountBefore);
		List<Consumption> consumptions = ConsumptionServiceRepository.toSum(coverages.get(year).stream()
				.filter(coverage -> Medium.WASSER.equals(coverage.medium())).map(transformer).toList(), false).toList();
		assertEquals(1, consumptions.size());

		float lastDialCount = coverages.get(year).stream().filter(coverage -> Medium.WASSER.equals(coverage.medium()))
				.filter(coverage -> Month.DECEMBER.equals(coverage.month())).map(Coverage::dialCount).findAny()
				.orElseThrow();
		Consumption consumption = consumptions.iterator().next();
		assertEquals(year, consumption.year());
		assertEquals(Month.DECEMBER, consumption.month());
		assertEquals(Medium.WASSER, consumption.medium());
		assertEquals(lastDialCount, consumption.dialCount());
		assertEquals(lastDialCount - dialCountBefore, consumption.consumption());
		assertFalse(consumption.meterChanged());
	}

	@Test
	void testToSumWithLast12Month() {
		Consumption c1 = new Consumption(1988, Month.APRIL, Medium.STROM, 11200f, 1004.5f, false);
		Consumption c2 = new Consumption(2005, Month.MAY, Medium.STROM, 23455.3f, 999.5f, false);
		List<Consumption> consumptions = ConsumptionServiceRepository.toSum(Arrays.asList(c1, c2), true).toList();
		assertEquals(1, consumptions.size());

		Consumption consumption = consumptions.iterator().next();
		assertEquals(-1, consumption.year());
		assertEquals(Month.MAY, consumption.month());
		assertEquals(Medium.STROM, consumption.medium());
		assertEquals(c2.dialCount(), consumption.dialCount());
		assertEquals(c1.consumption() + c2.consumption(), consumption.consumption());
		assertFalse(consumption.meterChanged());
	}

	@Test
	void testToSumEmpty() {
		List<Consumption> consumptions = ConsumptionServiceRepository.toSum(Collections.emptyList(), true).toList();
		assertTrue(consumptions.isEmpty());
	}

	@Test
	void testToSumNull() {
		assertThrows(NullPointerException.class, () -> ConsumptionServiceRepository.toSum(null, false));
	}

	@Test
	void testGetAnnualConsumptions() {
		List<Integer> years = Arrays.asList(2001, 2002, 2003);
		List<Consumption> consumptions = consumptionService.getAnnualConsumptions(Medium.GAS, years, false);
		assertEquals(years.size(), consumptions.size());

		Float[] dialCounts = Stream.concat(Stream.of(2000), years.stream()).map(coverages::get).flatMap(List::stream)
				.filter(coverage -> Medium.GAS.equals(coverage.medium()))
				.filter(coverage -> Month.DECEMBER.equals(coverage.month())).map(Coverage::dialCount)
				.toArray(i -> new Float[i]);
		IntStream.range(0, years.size())
				.forEach(i -> assertEquals(dialCounts[i + 1] - dialCounts[i], consumptions.get(i).consumption()));
	}

	@Test
	void testGetAnnualConsumptionsAndLast12Month() {
		int year = 1971;
		List<Consumption> consumptions = consumptionService.getAnnualConsumptions(Medium.GAS,
				Collections.singleton(year), true);
		assertEquals(2, consumptions.size());

		Consumption yearConsumption = consumptions.get(0);
		Consumption last12MonthConsumption = consumptions.get(1);
		assertEquals(year, yearConsumption.year());
		assertEquals(-1, last12MonthConsumption.year());
		assertEquals(yearConsumption.dialCount(), last12MonthConsumption.dialCount());
		assertEquals(yearConsumption.consumption(), last12MonthConsumption.consumption());
		Stream.of(yearConsumption, last12MonthConsumption).forEach(consumption -> {
			assertEquals(Month.DECEMBER, consumption.month());
			assertEquals(Medium.GAS, consumption.medium());
			assertFalse(consumption.meterChanged());
		});
	}

	@Test
	void testGetMonthlyAnnualConsumptions() {
		List<Integer> years = Arrays.asList(2010, 2011);
		List<Consumption> consumptions = consumptionService.getMonthlyAnnualConsumptions(Medium.WASSER, years, false);
		assertEquals(years.size() * Month.values().length, consumptions.size());

		double[] dialCounts = Stream.of(2009, 2011).map(coverages::get).flatMap(List::stream)
				.filter(coverage -> Medium.WASSER.equals(coverage.medium()))
				.filter(coverage -> Month.DECEMBER.equals(coverage.month())).mapToDouble(Coverage::dialCount).toArray();
		double consumption = consumptions.stream().mapToDouble(Consumption::consumption).sum();
		assertEquals(dialCounts[1] - dialCounts[0], consumption);
	}

	@Disabled
	@Test
	void testGetMonthlyAnnualConsumptionsAndDecemberYearBefore() {
		// TODO
		fail("not implemented");
	}

	@Test
	void testGetMonthlyConsumptions() {
		SortedSet<Integer> years = new TreeSet<>(IntStream.rangeClosed(1910, 2090).boxed().toList());
		List<Consumption> consumptions = consumptionService.getMonthlyConsumptions(Medium.WASSER, years, Month.AUGUST);
		assertEquals(years.size(), consumptions.size());

		consumptions.forEach(consumption -> {
			assertEquals(Month.AUGUST, consumption.month());
			assertEquals(Medium.WASSER, consumption.medium());
			assertFalse(consumption.meterChanged());
		});
	}

	private static interface DialCountBuilder {

		static float getDialCount(int year, Month month, Medium medium) {
			int i = 10 + (month.getValue() * 7);
			return Float.parseFloat(String.valueOf(year) + i + '.' + medium.name().length());
		}

	}

	private class TestRepositoryService implements RepositoryService {

		@Override
		public Set<Coverage> findCoverages() {
			return coverages.values().stream().flatMap(List::stream).collect(Collectors.toSet());
		}

		@Override
		public void storeCoverages(Collection<Coverage> coverages) {
			throw new UnsupportedOperationException();
		}

	}

}
