package com.webnobis.consumption.business.impl;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import com.webnobis.consumption.model.Consumption;
import com.webnobis.consumption.model.Coverage;

public class CoverageToConsumptionTransformer implements Function<Coverage,Consumption>{
	
	private final AtomicReference<Float> dialCountBefore = new AtomicReference<>(0f);

	@Override
	public Consumption apply(Coverage coverage) {
		Objects.requireNonNull(coverage, "coverage is null");
		float consumption = coverage.getDialCount() - dialCountBefore.getAndSet(coverage.getDialCount());
		boolean meterChanged = consumption < 0;
		if (meterChanged) {
			// assume, the consumption is the same as new meter dial count
			consumption = coverage.getDialCount();
		} else if (consumption >= coverage.getDialCount()) {
			// the first value has no dial count before
			consumption = 0;
		}
		return new Consumption(coverage.getYear(), coverage.getMonth(), coverage.getMedium(), coverage.getDialCount(), (meterChanged) ? coverage.getDialCount() : consumption, meterChanged);
	}

}
