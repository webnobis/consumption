package com.webnobis.consumption.business;

import java.time.Month;
import java.util.List;
import java.util.Map;

/**
 * Year service
 * 
 * @author steffen
 *
 */
public interface YearService {

	/**
	 * Get the years
	 * 
	 * @return years
	 */
	List<Integer> getYears();

	/**
	 * Get the years with their months
	 * 
	 * @return years with month
	 */
	Map<Integer, List<Month>> getYearsWithMonths();

}
