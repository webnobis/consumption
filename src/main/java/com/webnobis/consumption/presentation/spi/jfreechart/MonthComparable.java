package com.webnobis.consumption.presentation.spi.jfreechart;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Objects;

public class MonthComparable implements Comparable<MonthComparable> {

	private final Month month;

	private final boolean monthFromLastYear;

	public MonthComparable(Month month, boolean monthFromLastYear) {
		this.month = Objects.requireNonNull(month, "month is null");
		this.monthFromLastYear = monthFromLastYear;
	}

	@Override
	public int hashCode() {
		return Objects.hash(month, monthFromLastYear);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		MonthComparable other = (MonthComparable) obj;
		return month == other.month && monthFromLastYear == other.monthFromLastYear;
	}

	@Override
	public int compareTo(MonthComparable other) {
		if (monthFromLastYear && !other.monthFromLastYear) {
			return -1;
		}
		if (!monthFromLastYear && other.monthFromLastYear) {
			return 1;
		}
		return month.compareTo(other.month);
	}

	@Override
	public String toString() {
		return month.getDisplayName(TextStyle.SHORT, Locale.GERMAN).concat(monthFromLastYear ? " (Vorjahr)" : "");
	}

}