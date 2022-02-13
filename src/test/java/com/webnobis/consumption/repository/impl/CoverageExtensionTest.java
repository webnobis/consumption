package com.webnobis.consumption.repository.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.webnobis.consumption.repository.impl.CoverageExtension.CoverageReader;

@ExtendWith(CoverageExtension.class)
class CoverageExtensionTest {

	@Test
	void testNothing() {
		assertTrue(true);
	}

	@Test
	void testPath(Path folder) {
		assertNotNull(folder);
		assertTrue(Files.exists(folder));
		assertTrue(Files.isDirectory(folder));
	}

	@Test
	void testCoverageReader(CoverageReader coverageReader) {
		assertNotNull(coverageReader);
		assertFalse(coverageReader.readToList().isEmpty());
	}

	@Test
	void testBoth(CoverageReader coverageReader, Path folder) {
		assertNotNull(coverageReader);
		assertNotNull(folder);
		coverageReader.readToMap().forEach((path, coverages)-> {
			assertEquals(folder, path.getParent());
			assertFalse(coverageReader.readToList().isEmpty());
		});
	}

}
