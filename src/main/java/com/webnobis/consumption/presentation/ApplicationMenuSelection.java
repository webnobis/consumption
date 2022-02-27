package com.webnobis.consumption.presentation;

import com.webnobis.consumption.presentation.consumption.Report;

/**
 * Application menu selection
 * 
 * @author steffen
 *
 */
public interface ApplicationMenuSelection {

	/**
	 * Opens report specific consumption dialog
	 * 
	 * @param report report
	 */
	void openConsumption(Report report);

	/**
	 * Opens coverage dialog
	 */
	void openCoverage();

	/**
	 * Exits the application
	 */
	void exit();

}
