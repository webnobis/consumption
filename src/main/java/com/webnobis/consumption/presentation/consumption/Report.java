package com.webnobis.consumption.presentation.consumption;

/**
 * Report
 * 
 * @author steffen
 *
 */
public enum Report {

	YEAR("Jahresauswertung"), ALL_MONTH("Monatsauswertung"), ONE_MONTH("Monatsvergleich");

	private final String title;

	private Report(String title) {
		this.title = title;
	}

	/**
	 * Reports title
	 * 
	 * @return title
	 */
	public String getTitle() {
		return title;
	}

}
