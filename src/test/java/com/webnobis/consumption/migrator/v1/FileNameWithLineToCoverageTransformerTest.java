package com.webnobis.consumption.migrator.v1;

import static org.junit.Assert.assertEquals;

import java.time.Month;

import org.junit.Test;

import com.webnobis.consumption.model.Coverage;
import com.webnobis.consumption.model.Medium;

public class FileNameWithLineToCoverageTransformerTest {
	
	private static final String OLD_LINE = "30.08.2008;710,0 m³;31;7,7 m³;Aug 2008;7,7 m³;;\"Aug\";";

	@Test
	public void testApply() {
		Coverage expectedC = new Coverage(2008, Month.AUGUST, Medium.WASSER, 710.0f);
		FileNameWithLineToCoverageTransformer transformer = new FileNameWithLineToCoverageTransformer("WASSER.csv");
		assertEquals(expectedC, transformer.apply(OLD_LINE));
	}

}
