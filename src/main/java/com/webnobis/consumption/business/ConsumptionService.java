package com.webnobis.consumption.business;

import java.time.Month;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

import com.webnobis.consumption.model.Consumption;
import com.webnobis.consumption.model.Medium;

/**
 * Consumption service as business filter
 * 
 * @author steffen
 */
public interface ConsumptionService {

	/**
	 * @deprecated use either
	 *             {@link #getAnnualConsumptions(Medium, Collection, boolean)} or
	 *             {@link #getMonthlyAnnualConsumptions(Medium, Collection)}
	 * @param medium  medium
	 * @param years   years
	 * @param monthly monthly
	 * @return consumptions, sorted by years and months
	 * @see #getAnnualConsumptions(Medium, Collection, boolean)
	 * @see #getMonthlyAnnualConsumptions(Medium, Collection)
	 */
	@Deprecated
	SortedSet<Consumption> getConsumptions(Medium medium, Collection<Integer> years, boolean monthly);

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
	 * @param medium medium
	 * @param years  years
	 * @return consumptions, sorted by months and years
	 */
	List<Consumption> getMonthlyAnnualConsumptions(Medium medium, Collection<Integer> years);

	/**
	 * Monthly annual consumptions of start month first year until the month before
	 * last year each year
	 * 
	 * @param medium     medium
	 * @param years      years
	 * @param startMonth start month
	 * @return consumptions, sorted by months and years
	 */
	default List<Consumption> getMonthlyAnnualConsumptions(Medium medium, Collection<Integer> years, Month startMonth) {
		Objects.requireNonNull(startMonth);
		SortedSet<Integer> sortedYears = new TreeSet<>(Objects.requireNonNull(years));
		return Objects.requireNonNull(getMonthlyAnnualConsumptions(medium, years)).stream().filter(consumption -> {
			if (sortedYears.first().equals(consumption.year())) {
				return startMonth.compareTo(consumption.month()) < 1;
			} else if (sortedYears.last().equals(consumption.year())) {
				return startMonth.compareTo(consumption.month()) > 0;
			} else {
				return years.contains(consumption.year());
			}
		}).toList();
	}

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
		return Objects.requireNonNull(getMonthlyAnnualConsumptions(medium, years)).stream()
				.filter(consumption -> month.equals(consumption.month())).toList();
	}

}
