package com.webnobis.consumption.presentation.coverage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Objects;

import javax.swing.JPanel;
import javax.swing.JTextField;

public class MonthYearPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public MonthYearPanel(Month month, int year) {
		super(new BorderLayout(2,0));
		JTextField field = new JTextField(Objects.requireNonNull(month, "month is null").getDisplayName(TextStyle.FULL, Locale.GERMAN), 12);
		field.setEditable(false);
		field.setBackground(Color.LIGHT_GRAY);
		this.add(field, BorderLayout.CENTER);
		field = new JTextField(String.valueOf(year), 4);
		field.setEditable(false);
		field.setBackground(Color.LIGHT_GRAY);
		this.add(field, BorderLayout.EAST);
	}

}
