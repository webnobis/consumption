package com.webnobis.consumption.repository.file;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.nio.file.Path;
import java.time.Month;
import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.webnobis.consumption.model.Coverage;
import com.webnobis.consumption.model.Medium;
import com.webnobis.consumption.repository.RepositoryService;
import com.webnobis.consumption.repository.file.CoverageExtension.CoverageReader;

@ExtendWith(CoverageExtension.class)
class FileRepositoryServiceTest {

	private static final String FILE_1 = "Coverage2015.csv";

	private static final int YEAR_1 = 2015;

	private static final String FILE_2 = "Coverage2016.csv";

	private static final int YEAR_2 = 2016;

	private static final Month MONTH = Month.OCTOBER;

	private static final Medium MEDIUM = Medium.WASSER;

	private static final float VALUE = 219.6f;

	private RepositoryService service;

	@BeforeEach
	void setUp(Path tmpFolder) throws Exception {
		service = new FileRepositoryService(tmpFolder);
	}

	@Test
	void testFindCoverages(CoverageReader coverageReader) {
		Set<Coverage> expectedCoverages = coverageReader.readToSet();
		Set<Coverage> coverages = service.findCoverages();
		assertEquals(expectedCoverages.size(), coverages.size());
		assertTrue(expectedCoverages.containsAll(coverages));
	}

	@Test
	void testStoreCoveragesKnownFile(Path tmpFolder, CoverageReader coverageReader) {
		Coverage coverage = new Coverage(YEAR_1, MONTH, MEDIUM, VALUE);
		Map<Path, Set<Coverage>> coverages = coverageReader.readToMap();
		assertTrue(coverages.get(tmpFolder.resolve(FILE_1)).stream().noneMatch(coverage::equals));

		service.storeCoverages(Collections.singleton(coverage));

		coverages = coverageReader.readToMap();
		assertTrue(coverages.get(tmpFolder.resolve(FILE_1)).stream().anyMatch(coverage::equals));
	}

	@Test
	void testStoreCoveragesNewFile(CoverageReader coverageReader) {
		service.storeCoverages(Collections.singleton(new Coverage(YEAR_2, MONTH, MEDIUM, VALUE)));

		Map<Path, Set<Coverage>> coverages = coverageReader.readToMap();
		assertEquals(4, coverages.size());
		assertTrue(coverages.keySet().stream().map(file -> file.getFileName().toString()).anyMatch(FILE_2::equals));
		coverages.values().stream().filter(set -> set.size() == 1).map(set -> set.iterator().next()).findAny()
				.ifPresentOrElse(coverage -> {
					assertEquals(YEAR_2, coverage.year());
					assertEquals(MONTH, coverage.month());
					assertEquals(MEDIUM, coverage.medium());
					assertEquals(VALUE, coverage.dialCount());
				}, () -> fail(new NoSuchElementException()));
	}

}
