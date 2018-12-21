package com.webnobis.consumption.model;

import java.time.Month;

import com.webnobis.consumption.model.transformer.DialCountTransformer;

public class Consumption extends Coverage {

	private final float consumption;

	private final boolean meterChanged;

	public Consumption(int year, Month month, Medium medium, float dialCount, float consumption, boolean meterChanged) {
		super(year, month, medium, dialCount);
		this.consumption = consumption;
		this.meterChanged = meterChanged;
	}

	public float getConsumption() {
		return consumption;
	}

	public String getConsumptionText() {
		return DialCountTransformer.toText(consumption);
	}

	public boolean isMeterChanged() {
		return meterChanged;
	}

	@Override
	public String toString() {
		return "Consumption [date=" + getDate() + ", medium=" + getMedium() + ", consumption=" + consumption + "]";
	}

}
