package com.webnobis.consumption;

import java.nio.file.Paths;

import com.webnobis.consumption.business.ConsumptionService;
import com.webnobis.consumption.business.CoverageService;
import com.webnobis.consumption.business.YearService;
import com.webnobis.consumption.business.repository.ConsumptionServiceRepository;
import com.webnobis.consumption.business.repository.CoverageServiceRepository;
import com.webnobis.consumption.business.repository.YearServiceRepository;
import com.webnobis.consumption.presentation.ApplicationFrame;
import com.webnobis.consumption.repository.RepositoryService;
import com.webnobis.consumption.repository.file.FileRepositoryService;

/**
 * Consumption application
 * 
 * @author steffen
 */
public class ConsumptionMain {

	// may be overwritten by tests
	static Application application = ApplicationFrame::new;

	/**
	 * Starts the application with file base repository
	 * 
	 * @param args 1st argument is the repository folder otherwise the example
	 *             folder will be used
	 * @see ApplicationFrame#ApplicationFrame(YearService, ConsumptionService,
	 *      CoverageService)
	 */
	public static void main(String[] args) {
		RepositoryService repositoryService = new FileRepositoryService((args.length > 0) ? Paths.get(args[0]) : null);
		YearService yearService = new YearServiceRepository(repositoryService);
		ConsumptionService consumptionService = new ConsumptionServiceRepository(repositoryService);
		CoverageService coverageService = new CoverageServiceRepository(repositoryService);
		application.start(yearService, consumptionService, coverageService);
	}

	/**
	 * Application
	 * 
	 * @author steffen
	 *
	 */
	@FunctionalInterface
	interface Application {

		/**
		 * Starts the application
		 * 
		 * @param yearService        year service
		 * @param consumptionService consumption service
		 * @param coverageService    coverage service
		 */
		void start(YearService yearService, ConsumptionService consumptionService, CoverageService coverageService);
	}

}
