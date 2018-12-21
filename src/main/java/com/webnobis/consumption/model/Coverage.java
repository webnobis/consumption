package com.webnobis.consumption.model;

import java.time.Month;
import java.time.YearMonth;
import java.util.Objects;

import com.webnobis.consumption.model.transformer.DateTransformer;
import com.webnobis.consumption.model.transformer.DialCountTransformer;

public class Coverage implements Comparable<Coverage> {

	private final YearMonth yearMonth;

	private final Medium medium;

	private final float dialCount;

	// from stored
	public Coverage(String date, String medium, String dialCount) {
		this(DateTransformer.toYearAndMonth(Objects.requireNonNull(date, "date is null")), Medium.valueOf(Objects.requireNonNull(medium, "medium is null")), DialCountTransformer.toFloat(Objects.requireNonNull(dialCount, "dialCount is null")));
	}

	// from dialog
	public Coverage(int year, Month month, Medium medium, String dialCount) {
		this(year, Objects.requireNonNull(month, "month is null"), Objects.requireNonNull(medium, "medium is null"), DialCountTransformer.toFloat(Objects.requireNonNull(dialCount, "dialCount is null")));
	}

	// from consumption and tests
	public Coverage(int year, Month month, Medium medium, float dialCount) {
		this(YearMonth.of(year, Objects.requireNonNull(month, "month is null")), Objects.requireNonNull(medium, "medium is null"), dialCount);
	}
	
	// as new
	public Coverage(YearMonth yearMonth, Medium medium) {
		this(Objects.requireNonNull(yearMonth, "yearMonth is null"), Objects.requireNonNull(medium, "medium is null"), 0);
	}

	private Coverage(YearMonth yearMonth, Medium medium, float dialCount) {
		this.yearMonth = yearMonth;
		this.medium = medium;
		this.dialCount = dialCount;
	}

	public String getDate() {
		return DateTransformer.toLastDayOfMonthDate(yearMonth.getYear(), yearMonth.getMonth());
	}

	public int getYear() {
		return yearMonth.getYear();
	}

	public Month getMonth() {
		return yearMonth.getMonth();
	}

	public Medium getMedium() {
		return medium;
	}

	public float getDialCount() {
		return dialCount;
	}

	public String getDialCountText() {
		return DialCountTransformer.toText(dialCount);
	}

	@Override
	public int compareTo(Coverage other) {
		int i = yearMonth.compareTo(other.yearMonth);
		if (i != 0) {
			return i;
		}
		return medium.compareTo(other.medium);
	}

	@Override
	public int hashCode() {
		return yearMonth.hashCode() ^ medium.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		return compareTo((Coverage) obj) == 0;
	}

	@Override
	public String toString() {
		return "Coverage [date=" + getDate() + ", medium=" + medium + ", dialCount=" + dialCount + "]";
	}

}
