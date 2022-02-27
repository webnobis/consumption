package com.webnobis.consumption.model;

/**
 * Medium
 * 
 * @author steffen
 *
 */
public enum Medium {

	STROM("kWh"), GAS("m³"), WASSER("m³");

	private final String unit;

	private Medium(String unit) {
		this.unit = unit;
	}

	/**
	 * Gets the unit
	 * 
	 * @return unit
	 */
	public String getUnit() {
		return unit;
	}

}
