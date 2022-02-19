package com.webnobis.consumption.presentation.consumption;

public enum Report {
	
	YEAR("Jahresauswertung"), ALL_MONTH("Monatsauswertung"), ONE_MONTH("Monatsvergleich");

	private final String title;

	private Report(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}
	
}
