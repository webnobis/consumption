package com.webnobis.consumption.presentation.consumption;

import javax.swing.JCheckBoxMenuItem;

public class YearMenuItem extends JCheckBoxMenuItem {

	private static final long serialVersionUID = 1L;

	private final int year;

	public YearMenuItem(int year) {
		super(String.valueOf(year));
		this.year = year;
	}

	public int getYear() {
		return year;
	}

}
