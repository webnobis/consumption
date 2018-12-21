package com.webnobis.consumption.model;

public enum Medium {
	
	STROM("kWh"), GAS("m³"), WASSER("m³");
	
	private final String unit;

	private Medium(String unit) {
		this.unit = unit;
	}

	public String getUnit() {
		return unit;
	}

}
