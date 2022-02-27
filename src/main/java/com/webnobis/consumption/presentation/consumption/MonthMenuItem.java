package com.webnobis.consumption.presentation.consumption;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Objects;

import javax.swing.JRadioButtonMenuItem;

/**
 * Month menu item
 * 
 * @author steffen
 *
 */
public class MonthMenuItem extends JRadioButtonMenuItem {

	private static final long serialVersionUID = 1L;

	private final Month month;

	/**
	 * Month
	 * 
	 * @param month month
	 */
	public MonthMenuItem(Month month) {
		super(Objects.requireNonNull(month, "month is null").getDisplayName(TextStyle.SHORT, Locale.GERMAN));
		this.month = month;
	}

	/**
	 * Month
	 * 
	 * @return month
	 */
	public Month getMonth() {
		return month;
	}

}
