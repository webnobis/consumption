package com.webnobis.consumption.presentation.consumption;

import java.util.Objects;

import javax.swing.JRadioButtonMenuItem;

import com.webnobis.consumption.model.Medium;

/**
 * Medium menu item
 * 
 * @author steffen
 *
 */
public class MediumMenuItem extends JRadioButtonMenuItem {

	private static final long serialVersionUID = 1L;

	private final Medium medium;

	/**
	 * Medium
	 * 
	 * @param medium medium
	 */
	public MediumMenuItem(Medium medium) {
		super(Objects.requireNonNull(medium, "medium is null").name());
		this.medium = medium;
	}

	/**
	 * Medium
	 * 
	 * @return medium
	 */
	public Medium getMedium() {
		return medium;
	}

}
