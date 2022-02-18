package com.webnobis.consumption.model.transformer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Month;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.webnobis.consumption.model.Consumption;
import com.webnobis.consumption.model.Coverage;
import com.webnobis.consumption.model.Medium;

class CoverageToConsumptionTransformerTest {

	private static final float INITIAL_DIAL_COUNT = 333.4f;

	private Function<Coverage, Consumption> transformer;

	@BeforeEach
	void setUp() {
		transformer = new CoverageToConsumptionTransformer(INITIAL_DIAL_COUNT);
	}

	@Test
	void testApplyInitial() {
		Coverage coverage = new Coverage(1988, Month.JANUARY, Medium.GAS, 400.8f);

		Consumption consumption = transformer.apply(coverage);
		assertNotNull(consumption);
		assertEquals(coverage.year(), consumption.year());
		assertEquals(coverage.month(), consumption.month());
		assertEquals(coverage.medium(), consumption.medium());
		assertEquals(coverage.dialCount(), consumption.dialCount());
		assertEquals(coverage.dialCount() - INITIAL_DIAL_COUNT, consumption.consumption());
		assertFalse(consumption.meterChanged());
	}

	@Test
	void testApply() {
		Coverage coverage1 = new Coverage(1988, Month.JANUARY, Medium.GAS, 400);
		Coverage coverage2 = new Coverage(2001, Month.JULY, Medium.GAS, 2000);

		Consumption consumption = transformer.apply(coverage1);
		assertNotNull(consumption);
		assertEquals(coverage1.year(), consumption.year());
		assertEquals(coverage1.month(), consumption.month());
		assertEquals(coverage1.medium(), consumption.medium());
		assertEquals(coverage1.dialCount(), consumption.dialCount());
		assertEquals(coverage1.dialCount() - INITIAL_DIAL_COUNT, consumption.consumption());
		assertFalse(consumption.meterChanged());

		consumption = transformer.apply(coverage2);
		assertNotNull(consumption);
		assertEquals(coverage2.dialCount() - coverage1.dialCount(), consumption.consumption());
		assertFalse(consumption.meterChanged());
	}

	@Test
	void testApplyMeterChanged() {
		Coverage coverage1 = new Coverage(1988, Month.JANUARY, Medium.GAS, 400);
		Coverage coverage2 = new Coverage(2001, Month.JULY, Medium.GAS, 3.3f);

		Consumption consumption = transformer.apply(coverage1);
		assertNotNull(consumption);
		assertEquals(coverage1.dialCount() - INITIAL_DIAL_COUNT, consumption.consumption());
		assertFalse(consumption.meterChanged());

		consumption = transformer.apply(coverage2);
		assertNotNull(consumption);
		assertEquals(coverage2.dialCount(), consumption.dialCount());
		assertEquals(coverage2.dialCount(), consumption.consumption());
		assertTrue(consumption.meterChanged());
	}

	@Test
	void testApplyNull() {
		assertThrows(NullPointerException.class, () -> transformer.apply(null));
	}

}
