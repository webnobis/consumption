package com.webnobis.consumption.model.transformer;

import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public abstract class DateTransformer {

	private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

	private static final int MONTH_OFFSET = 1;

	public static String toLastDayOfMonthDate(int year, Month month) {
		if (month == null) {
			return String.valueOf(year);
		} else {
			return YearMonth.of(year, month).atEndOfMonth().format(dateFormatter);
		}
	}

	public static YearMonth getYearAndLastMonth() {
		return toYearAndMonth(null);
	}

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
