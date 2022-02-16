package com.webnobis.consumption.model;

import java.time.Month;
import java.util.Objects;

/**
 * Coverage
 * 
 * @author steffen
 *
 */
public record Coverage(int year, Month month, Medium medium, float dialCount) implements Comparable<Coverage> {

	@Override
	public int compareTo(Coverage other) {
		if (year < other.year) {
			return -1;
		}
		if (year > other.year) {
			return 1;
		}
		int i = month.compareTo(other.month);
		return i == 0 ? medium.compareTo(other.medium) : i;
	}

	@Override
	public int hashCode() {
		return Objects.hash(medium, month, year);
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
		Coverage other = (Coverage) obj;
		return Objects.equals(medium, other.medium) && Objects.equals(month, other.month)
				&& Objects.equals(year, other.year);
	}

}
