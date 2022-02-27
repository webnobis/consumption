package com.webnobis.consumption.presentation.consumption;

import javax.swing.JCheckBoxMenuItem;

/**
 * Year menu item
 * 
 * @author steffen
 *
 */
public class YearMenuItem extends JCheckBoxMenuItem {

	private static final long serialVersionUID = 1L;

	private final int year;

	/**
	 * Year
	 * 
	 * @param year year
	 */
	public YearMenuItem(int year) {
		super(String.valueOf(year));
		this.year = year;
	}

	/**
	 * Year
	 * 
	 * @return year
	 */
	public int getYear() {
		return year;
	}

}
