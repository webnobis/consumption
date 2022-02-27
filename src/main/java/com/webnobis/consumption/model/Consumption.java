package com.webnobis.consumption.model;

import java.time.Month;
import java.util.Objects;

/**
 * Consumption
 * 
 * @author steffen
 *
 */
public record Consumption(int year, Month month, Medium medium, float dialCount, float consumption,
		boolean meterChanged, boolean monthFromLastYear) implements Comparable<Consumption> {

	/**
	 * Calls full constructor with both flags false
	 * 
	 * @param year        year
	 * @param month       month
	 * @param medium      medium
	 * @param dialCount   dial count
	 * @param consumption consumption
	 * @see #Consumption(int, Month, Medium, float, float, boolean, boolean)
	 */
	public Consumption(int year, Month month, Medium medium, float dialCount, float consumption) {
		this(year, month, medium, dialCount, consumption, false, false);
	}

	@Override
	public int compareTo(Consumption other) {
		if (year < other.year) {
			return -1;
		}
		if (year > other.year) {
			return 1;
		}
		if (monthFromLastYear && !other.monthFromLastYear) {
			return -1;
		}
		if (!monthFromLastYear && other.monthFromLastYear) {
			return 1;
		}
		int i = month.compareTo(other.month);
		return i == 0 ? medium.compareTo(other.medium) : i;
	}

	@Override
	public int hashCode() {
		return Objects.hash(medium, month, year, monthFromLastYear);
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
		Consumption other = (Consumption) obj;
		return Objects.equals(medium, other.medium) && Objects.equals(month, other.month)
				&& Objects.equals(year, other.year) && monthFromLastYear == other.monthFromLastYear;
	}

}
