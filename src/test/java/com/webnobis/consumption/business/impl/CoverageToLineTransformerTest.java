package com.webnobis.consumption.business.impl;

import static org.junit.Assert.assertEquals;

import java.time.Month;

import org.junit.Test;

import com.webnobis.consumption.model.Coverage;
import com.webnobis.consumption.model.Medium;
import com.webnobis.consumption.repository.impl.CoverageToLineTransformer;

public class CoverageToLineTransformerTest {

	@Test
	public void testApply() {
		String expectedLine = "31.03.2015;STROM;76400.6";
		Coverage c = new Coverage(2015,Month.MARCH,Medium.STROM,76400.61234567f);
		assertEquals(expectedLine, new CoverageToLineTransformer().apply(c));
	}

}
