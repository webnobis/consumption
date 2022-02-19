package com.webnobis.consumption.model.transformer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Month;

import org.junit.jupiter.api.Test;

import com.webnobis.consumption.model.Consumption;
import com.webnobis.consumption.model.Medium;

class ConsumptionTransformerTest {

	private static final Consumption CONSUMPTION = new Consumption(1611, Month.APRIL, Medium.STROM, 12.2f, 3.3f);

	@Test
	void testToMonthFromLastYear() {
		assertFalse(CONSUMPTION.monthFromLastYear());

		int year = 1788;
		Consumption consumption = ConsumptionTransformer.toMonthFromLastYear(CONSUMPTION, year);
		assertNotEquals(CONSUMPTION.year(), consumption.year());
		assertEquals(year, consumption.year());
		assertEquals(CONSUMPTION.month(), consumption.month());
		assertEquals(CONSUMPTION.medium(), consumption.medium());
		assertEquals(CONSUMPTION.dialCount(), consumption.dialCount());
		assertEquals(CONSUMPTION.consumption(), consumption.consumption());
		assertEquals(CONSUMPTION.meterChanged(), consumption.meterChanged());
		assertTrue(consumption.monthFromLastYear());
	}

}
