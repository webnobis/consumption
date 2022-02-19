package com.webnobis.consumption.model.transformer;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import com.webnobis.consumption.model.Consumption;
import com.webnobis.consumption.model.Coverage;

/**
 * Coverage to consumption transformer
 * 
 * @author steffen
 *
 */
public class CoverageToConsumptionTransformer implements Function<Coverage, Consumption> {

	private final AtomicReference<Float> dialCountBeforeRef;

	/**
	 * Sets the initial dial count
	 * 
	 * @param initialDialCount initial dial count
	 */
	public CoverageToConsumptionTransformer(float initialDialCount) {
		dialCountBeforeRef = new AtomicReference<>(initialDialCount);
	}

	/**
	 * Calculates consumption, based on dial count of month before.<br>
	 * If the dial count will be lesser than the month before, the meter changed
	 * flag is set to true and the consumption is set to the dial count value.
	 */
	@Override
	public Consumption apply(Coverage coverage) {
		Objects.requireNonNull(coverage, "coverage is null");
		float consumption = coverage.dialCount() - dialCountBeforeRef.getAndSet(coverage.dialCount());
		if (consumption < 0) {
			// assume, the consumption is the same as new meter dial count
			return new Consumption(coverage.year(), coverage.month(), coverage.medium(), coverage.dialCount(),
					coverage.dialCount(), true, false);
		} else {
			return new Consumption(coverage.year(), coverage.month(), coverage.medium(), coverage.dialCount(),
					consumption);
		}
	}

}
