package com.webnobis.consumption.model.transformer;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import com.webnobis.consumption.model.Consumption;
import com.webnobis.consumption.model.Coverage;

public class CoverageToConsumptionTransformer implements Function<Coverage, Consumption> {

	private final AtomicReference<Float> dialCountBefore = new AtomicReference<>(0f);

	@Override
	public Consumption apply(Coverage coverage) {
		Objects.requireNonNull(coverage, "coverage is null");
		float consumption = coverage.dialCount() - dialCountBefore.getAndSet(coverage.dialCount());
		boolean meterChanged = consumption < 0;
		if (meterChanged) {
			// assume, the consumption is the same as new meter dial count
			consumption = coverage.dialCount();
		} else if (consumption >= coverage.dialCount()) {
			// the first value has no dial count before
			consumption = 0;
		}
		return new Consumption(coverage.year(), coverage.month(), coverage.medium(), coverage.dialCount(), consumption,
				meterChanged);
	}

}
