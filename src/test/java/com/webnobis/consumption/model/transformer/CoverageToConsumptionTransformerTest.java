package com.webnobis.consumption.model.transformer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Month;

import org.junit.jupiter.api.Test;

import com.webnobis.consumption.model.Consumption;
import com.webnobis.consumption.model.Coverage;
import com.webnobis.consumption.model.Medium;

class CoverageToConsumptionTransformerTest {

	@Test
	void testApply() {
		Coverage c1 = new Coverage(1988, Month.JANUARY, Medium.GAS, 400);
		Coverage c2 = new Coverage(2001, Month.JULY, Medium.STROM, 2000);
		Coverage c3 = new Coverage(2, Month.SEPTEMBER, Medium.WASSER, 2);

		Consumption expectedC1 = new Consumption(c1.year(), c1.month(), c1.medium(), c1.dialCount(), 0, false);
		Consumption expectedC2 = new Consumption(c2.year(), c2.month(), c2.medium(), c2.dialCount(), 1600, false);
		Consumption expectedC3 = new Consumption(c3.year(), c3.month(), c3.medium(), c3.dialCount(), 2, true);

		CoverageToConsumptionTransformer transformer = new CoverageToConsumptionTransformer();
		assertEquals(expectedC1, transformer.apply(c1));
		assertEquals(expectedC2, transformer.apply(c2));
		assertEquals(expectedC3, transformer.apply(c3));
	}

}
