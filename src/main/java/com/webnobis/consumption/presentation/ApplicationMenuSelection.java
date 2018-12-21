package com.webnobis.consumption.presentation;

import com.webnobis.consumption.presentation.consumption.Report;

public interface ApplicationMenuSelection {
	
	public void openConsumption(Report report);
	
	public void openCoverage();
	
	public void exit();

}
