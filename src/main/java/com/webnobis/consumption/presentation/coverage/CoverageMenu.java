package com.webnobis.consumption.presentation.coverage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.webnobis.consumption.business.YearService;
import com.webnobis.consumption.presentation.Updateable;

public class CoverageMenu extends JMenuBar implements Storable, Updateable {

	private static final long serialVersionUID = 1L;

	private final YearService yearService;

	private final CoverageMenuSelection coverageMenuSelection;

	private final JMenu open;

	private final JMenuItem store;

	public CoverageMenu(YearService yearService, CoverageMenuSelection coverageMenuSelection) {
		super();
		this.yearService = Objects.requireNonNull(yearService, "yearService is null");
		this.coverageMenuSelection = Objects.requireNonNull(coverageMenuSelection, "coverageMenuSelection is null");

		JMenu edit = new JMenu("Bearbeiten");
		
		JMenu create = new JMenu("Neu");
		JMenuItem item = new JMenuItem("Letzter Monat");
		item.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				coverageMenuSelection.create(true);
			}

		});
		create.add(item);
		item = new JMenuItem("Aktueller Monat");
		item.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				coverageMenuSelection.create(false);
			}

		});
		create.add(item);
		edit.add(create);

		open = new JMenu("Öffnen");
		edit.add(open);
		update();
		
		edit.addSeparator();
		store = new JMenuItem("Speichern");
		store.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				coverageMenuSelection.store();
			}

		});
		edit.add(store);
		super.add(edit);
	}

	@Override
	public void update() {
		open.removeAll();
		Map<Integer,List<Month>> yearsWithMonths = yearService.getYearsWithMonths();
		yearsWithMonths.keySet().stream().sorted().forEach(year -> {
			JMenu menu = new JMenu(String.valueOf(year));
			yearsWithMonths.get(year).stream().sorted().forEach(month -> {
				JMenuItem item = new JMenuItem(month.getDisplayName(TextStyle.SHORT, Locale.GERMAN));
				item.addActionListener(new ActionListener() {

					private final int y = year;

					private final Month m = month;

					@Override
					public void actionPerformed(ActionEvent event) {
						coverageMenuSelection.open(y, m);
					}

				});
				menu.add(item);
			});
			open.add(menu);
		});
	}

	@Override
	public void setStorable(boolean storable) {
		store.setEnabled(storable);
	}

}
