package com.webnobis.consumption.repository.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.webnobis.consumption.model.Coverage;
import com.webnobis.consumption.model.Medium;
import com.webnobis.consumption.repository.RepositoryService;

class RepositoryServiceTest {

	private static final String FIND_FILE = "Coverage2014.csv";

	private static final int LINES = Month.values().length * Medium.values().length;

	private static final int FIND_YEAR = Integer.parseInt(FIND_FILE.replaceAll("\\D", ""));

	private static final int STORE_YEAR = FIND_YEAR + 10;

	private static final String STORE_FILE = FIND_FILE.replace(String.valueOf(FIND_YEAR), String.valueOf(STORE_YEAR));

	private static Path tmpFolder;

	private static Path tmpFile;

	private RepositoryService service;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		tmpFolder = Files.createTempDirectory(RepositoryServiceTest.class.getSimpleName());
		tmpFile = tmpFolder.resolve(FIND_FILE);
		Files.copy(ClassLoader.getSystemResourceAsStream(FIND_FILE), tmpFile, StandardCopyOption.REPLACE_EXISTING);
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		Files.newDirectoryStream(tmpFolder).forEach(file -> {
			try {
				Files.delete(file);
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		});
		Files.delete(tmpFolder);
	}

	@BeforeEach
	void setUp() throws Exception {
		service = new RepositoryServiceImpl(tmpFolder);
	}

	@Test
	void testFindCoverages() {
		Set<Coverage> coverages = service.findCoverages();
		assertNotNull(coverages);
		assertFalse(coverages.isEmpty());
		assertEquals(LINES, coverages.size());
		coverages.forEach(coverage -> assertEquals(FIND_YEAR, coverage.getYear()));

		Map<Month, List<Coverage>> map = coverages.stream().collect(Collectors.groupingBy(Coverage::getMonth));
		assertEquals(LINES / Medium.values().length, map.size());
		map.forEach((k, v) -> assertEquals(Medium.values().length, v.size()));
	}

	@Test
	void testStoreCoverages() throws IOException {
		Path storeFile = tmpFolder.resolve(STORE_FILE);
		assertFalse(Files.exists(storeFile));
		List<Coverage> coverages = createCoverages();
		assertEquals(LINES, coverages.size());

		service.storeCoverages(coverages);

		assertTrue(Files.exists(storeFile));
		List<String> expectedLines = coverages.stream().map(new CoverageToLineTransformer())
				.collect(Collectors.toList());
		assertEquals(LINES, expectedLines.size());

		List<String> lines = Files.readAllLines(storeFile, StandardCharsets.UTF_8);
		assertNotNull(lines);
		lines.remove(0); // header

		assertEquals(expectedLines.size(), lines.size());
		assertTrue(expectedLines.containsAll(lines));
	}

	private static List<Coverage> createCoverages() throws IOException {
		return Files.readAllLines(tmpFile, StandardCharsets.UTF_8).stream().map(new LineToCoverageTransformer())
				.filter(coverage -> (coverage != null))
				.map(coverage -> new Coverage(STORE_YEAR, coverage.getMonth(), coverage.getMedium(), Float.MAX_VALUE))
				.collect(Collectors.toList());
	}

}
