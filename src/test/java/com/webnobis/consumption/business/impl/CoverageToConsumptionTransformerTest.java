package com.webnobis.consumption.business.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Month;

import org.junit.jupiter.api.Test;

import com.webnobis.consumption.model.Consumption;
import com.webnobis.consumption.model.Coverage;
import com.webnobis.consumption.model.Medium;

class CoverageToConsumptionTransformerTest {

	@Test
	void testApply() {
		Coverage c1 = new Coverage(1988,Month.JANUARY,Medium.GAS,400);
		Coverage c2 = new Coverage(2001,Month.JULY,Medium.STROM,2000);
		Coverage c3 = new Coverage(2,Month.SEPTEMBER,Medium.WASSER,2);
		
		Consumption expectedC1 = new Consumption(c1.getYear(), c1.getMonth(), c1.getMedium(), c1.getDialCount(), 0, false);
		Consumption expectedC2 = new Consumption(c2.getYear(), c2.getMonth(), c2.getMedium(), c2.getDialCount(), 1600, false);
		Consumption expectedC3 = new Consumption(c3.getYear(), c3.getMonth(), c3.getMedium(), c3.getDialCount(), 2, true);
		
		CoverageToConsumptionTransformer transformer = new CoverageToConsumptionTransformer();
		assertEquals(expectedC1, transformer.apply(c1));
		assertEquals(expectedC2, transformer.apply(c2));
		assertEquals(expectedC3, transformer.apply(c3));
	}

}
