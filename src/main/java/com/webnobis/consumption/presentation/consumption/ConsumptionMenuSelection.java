package com.webnobis.consumption.presentation.consumption;

import java.time.Month;
import java.util.Collection;

import com.webnobis.consumption.model.Medium;

/**
 * Consumption menu selection
 * 
 * @author steffen
 *
 */
public interface ConsumptionMenuSelection {

	/**
	 * Selected month
	 * 
	 * @return month, otherwise null
	 */
	Month getSelectedMonth();

	/**
	 * Selected medium
	 * 
	 * @return medium, otherwise null
	 */
	Medium getSelectedMedium();

	/**
	 * Last 12 months flag
	 * 
	 * @return true, if selected
	 */
	boolean isLast12MonthSelected();

	/**
	 * December last year flag
	 * 
	 * @return true, if selected
	 */
	boolean isDecemberLastYearSelected();

	/**
	 * Selected years
	 * 
	 * @return years, otherwise empty
	 */
	Collection<Integer> getSelectedYears();

}
