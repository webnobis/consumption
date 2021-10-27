package com.webnobis.consumption.model.transformer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DialCountTransformerTest {

	@Test
	void testToFloat() {
		assertEquals(0.0f, DialCountTransformer.toFloat(null), 0.0f);
		assertEquals(0.0f, DialCountTransformer.toFloat(""), 0.0f);
		assertEquals(0.0f, DialCountTransformer.toFloat("000000000"), 0.0f);
		assertEquals(0.0f, DialCountTransformer.toFloat("0000.0000"), 0.0f);
		assertEquals(9.5f, DialCountTransformer.toFloat("9,5"), 0.0f);
		assertEquals(9.51f, DialCountTransformer.toFloat("9.51"), 0.0f);
		assertEquals(-17888.7777f, DialCountTransformer.toFloat("-17,888.7777"), 0.0f);
		assertEquals(1934523.455f, DialCountTransformer.toFloat("19.3.4.5,2.3,455"), 0.0f);
	}

	@Test
	void testToText() {
		assertEquals("0.0", DialCountTransformer.toText(0));
		assertEquals("0.0", DialCountTransformer.toText(Float.NaN));
		assertEquals("-8888.6", DialCountTransformer.toText(-8888.5555f));
		assertEquals("23456.0", DialCountTransformer.toText(23456f));
		assertEquals("76400.6", DialCountTransformer.toText(76400.61234567f));
	}

}
