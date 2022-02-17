package com.webnobis.consumption.presentation.consumption;

import java.util.Objects;

import javax.swing.JRadioButtonMenuItem;

import com.webnobis.consumption.model.Medium;

public class MediumMenuItem extends JRadioButtonMenuItem {

	private static final long serialVersionUID = 1L;

	private final Medium medium;

	public MediumMenuItem(Medium medium) {
		super(Objects.requireNonNull(medium.name(), "medium is null"));
		this.medium = medium;
	}

	public Medium getMedium() {
		return medium;
	}

}
