package com.webnobis.consumption.presentation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.webnobis.consumption.presentation.consumption.Report;

/**
 * Consumption application menu
 * 
 * @author steffen
 *
 */
public class ApplicationMenu extends JMenuBar {

	private static final long serialVersionUID = 1L;

	/**
	 * Application menu
	 * 
	 * @param menuSelection menu selection
	 */
	public ApplicationMenu(ApplicationMenuSelection menuSelection) {
		super();
		Objects.requireNonNull(menuSelection, "menuSelection is null");

		JMenu applicationMenu = new JMenu("Anwendung");
		JMenuItem item = new JMenuItem("Monatsverbrauch");
		item.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				menuSelection.openConsumption(Report.ALL_MONTH);
			}

		});
		applicationMenu.add(item);
		item = new JMenuItem("Jahresverbrauch");
		item.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				menuSelection.openConsumption(Report.YEAR);
			}

		});
		applicationMenu.add(item);
		applicationMenu.addSeparator();
		item = new JMenuItem("Zählerstand erfassen");
		item.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				menuSelection.openCoverage();
			}

		});
		applicationMenu.add(item);
		applicationMenu.addSeparator();
		item = new JMenuItem("Beenden");
		item.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				menuSelection.exit();
			}

		});
		applicationMenu.add(item);
		super.add(applicationMenu);
	}

}
