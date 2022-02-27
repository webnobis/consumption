package com.webnobis.consumption.business;

import java.time.Month;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.webnobis.consumption.model.Consumption;
import com.webnobis.consumption.model.Medium;

/**
 * Consumption service as business filter
 * 
 * @author steffen
 */
public interface ConsumptionService {

	/**
	 * Annual consumptions as sum of January until December each year
	 * 
	 * @param medium         medium
	 * @param years          years
	 * @param andLast12Month and sum of last 12 months, if true
	 * @return consumptions, sorted by years and optional followed by sum of last 12
	 *         months
	 */
	List<Consumption> getAnnualConsumptions(Medium medium, Collection<Integer> years, boolean andLast12Month);

	/**
	 * Monthly annual consumptions of January until December each year
	 * 
	 * @param medium                medium
	 * @param years                 years
	 * @param andDecemberYearBefore and December year before at 1st, if true
	 * @return consumptions, sorted by months and years
	 */
	List<Consumption> getMonthlyAnnualConsumptions(Medium medium, Collection<Integer> years,
			boolean andDecemberYearBefore);

	/**
	 * Monthly consumptions of the month each year
	 * 
	 * @param medium medium
	 * @param years  years
	 * @param month  month
	 * @return consumptions, sorted by years
	 */
	default List<Consumption> getMonthlyConsumptions(Medium medium, Collection<Integer> years, Month month) {
		Objects.requireNonNull(month);
		return Objects.requireNonNull(getMonthlyAnnualConsumptions(medium, years, false)).stream()
				.filter(consumption -> month.equals(consumption.month())).toList();
	}

}
