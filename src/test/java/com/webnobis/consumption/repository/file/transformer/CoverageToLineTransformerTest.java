package com.webnobis.consumption.repository.file.transformer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Month;

import org.junit.jupiter.api.Test;

import com.webnobis.consumption.model.Coverage;
import com.webnobis.consumption.model.Medium;

class CoverageToLineTransformerTest {

	@Test
	void testApply() {
		String expectedLine = "31.03.2015;STROM;76400.6";
		Coverage c = new Coverage(2015,Month.MARCH,Medium.STROM,76400.61234567f);
		assertEquals(expectedLine, new CoverageToLineTransformer().apply(c));
	}

}
