package com.webnobis.consumption.presentation.coverage;

import java.time.Month;

/**
 * Coverage menu selection
 * 
 * @author steffen
 *
 */
public interface CoverageMenuSelection {

	/**
	 * Create new empty current or last month
	 * 
	 * @param lastMonth true, if last month
	 */
	void create(boolean lastMonth);

	/**
	 * Open stored
	 * 
	 * @param year  year
	 * @param month month
	 */
	void open(int year, Month month);

	/**
	 * Store
	 */
	void store();

}
