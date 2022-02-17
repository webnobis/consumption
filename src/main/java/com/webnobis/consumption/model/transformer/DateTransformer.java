package com.webnobis.consumption.model.transformer;

import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

/**
 * Date transformer
 * 
 * @author steffen
 *
 */
public abstract class DateTransformer {

	private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

	private static final int MONTH_OFFSET = 1;

	/**
	 * Creates the last day of the month date
	 * 
	 * @param year  year
	 * @param month month
	 * @return date
	 */
	public static String toLastDayOfMonthDate(int year, Month month) {
		if (month == null) {
			return String.valueOf(year);
		} else {
			return YearMonth.of(year, month).atEndOfMonth().format(dateFormatter);
		}
	}

	/**
	 * Gets the last month based on now
	 * 
	 * @return last month
	 */
	public static YearMonth getYearAndLastMonth() {
		return toYearAndMonth(null);
	}

	/**
	 * Transforms the date to month
	 * 
	 * @param date date
	 * @return month
	 */
	public static YearMonth toYearAndMonth(String date) {
		if (date == null) {
			return YearMonth.now().minusMonths(MONTH_OFFSET);
		} else {
			return YearMonth.parse(date, dateFormatter);
		}
	}

	private DateTransformer() {
	}

}
