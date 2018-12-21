package com.webnobis.consumption;

import java.nio.file.Paths;

import com.webnobis.consumption.business.ConsumptionService;
import com.webnobis.consumption.business.CoverageService;
import com.webnobis.consumption.business.YearService;
import com.webnobis.consumption.business.impl.ConsumptionServiceImpl;
import com.webnobis.consumption.business.impl.CoverageServiceImpl;
import com.webnobis.consumption.business.impl.YearServiceImpl;
import com.webnobis.consumption.presentation.ApplicationFrame;
import com.webnobis.consumption.repository.RepositoryService;
import com.webnobis.consumption.repository.impl.RepositoryServiceImpl;

public class ConsumptionMain {

	public static void main(String[] args) {
		RepositoryService repositoryService = new RepositoryServiceImpl((args.length > 0)? Paths.get(args[0]): null);
		YearService yearService = new YearServiceImpl(repositoryService);
		ConsumptionService consumptionService = new ConsumptionServiceImpl(repositoryService);
		CoverageService coverageService = new CoverageServiceImpl(repositoryService);
		new ApplicationFrame(yearService, consumptionService, coverageService);
	}

}
