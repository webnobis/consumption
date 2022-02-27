package com.webnobis.consumption;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.webnobis.consumption.business.repository.ConsumptionServiceRepository;
import com.webnobis.consumption.business.repository.CoverageServiceRepository;
import com.webnobis.consumption.business.repository.YearServiceRepository;
import com.webnobis.consumption.repository.file.FileRepositoryService;

class ConsumptionMainTest {

	private static final Path FOLDER = Paths.get("a", "b", "c");

	@Test
	void testMain() {
		ConsumptionMain.application = (yearService, consumptionService, coverageService) -> {
			assertEquals(YearServiceRepository.class, yearService.getClass());
			assertEquals(ConsumptionServiceRepository.class, consumptionService.getClass());
			assertEquals(CoverageServiceRepository.class, coverageService.getClass());

			Stream.of(yearService, consumptionService, coverageService).map(obj -> {
				try {
					return (FileRepositoryService) obj.getClass().getMethod("repositoryService").invoke(obj);
				} catch (Exception e) {
					fail(e);
					return null;
				}
			}).forEach(repositoryService -> assertEquals(FOLDER, repositoryService.getFolder()));
		};

		ConsumptionMain.main(new String[] { FOLDER.toString() });
	}

}
