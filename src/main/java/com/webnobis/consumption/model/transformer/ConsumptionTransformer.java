package com.webnobis.consumption.model.transformer;

import java.util.Objects;

import com.webnobis.consumption.model.Consumption;

/**
 * Consumption transformer
 * 
 * @author steffen
 *
 */
public interface ConsumptionTransformer {

	/**
	 * Wraps the consumption to the new year and marks it as month from last year
	 * 
	 * @param consumption consumption
	 * @param newYear     new year
	 * @return wrapped consumption
	 */
	static Consumption toMonthFromLastYear(Consumption consumption, int newYear) {
		return new Consumption(newYear, Objects.requireNonNull(consumption, "consumption is null").month(),
				consumption.medium(), consumption.dialCount(), consumption.consumption(), consumption.meterChanged(),
				true);
	}

}
