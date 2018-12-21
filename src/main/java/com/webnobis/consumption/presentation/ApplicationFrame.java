package com.webnobis.consumption.presentation;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.util.Arrays;
import java.util.ResourceBundle;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

import com.webnobis.consumption.business.ConsumptionService;
import com.webnobis.consumption.business.CoverageService;
import com.webnobis.consumption.business.YearService;
import com.webnobis.consumption.presentation.consumption.ConsumptionFrame;
import com.webnobis.consumption.presentation.consumption.Report;
import com.webnobis.consumption.presentation.coverage.CoverageFrame;

public class ApplicationFrame extends JFrame implements ApplicationMenuSelection, Updateable {

	private static final long serialVersionUID = 1L;

	/*
	 * The title of the media planner window
	 */
	private static final String TITLE = "Verbrauch - Gegenüberstellung und Erfassung" + " (V " + ResourceBundle.getBundle("version").getString("version") + ", by Steffen Nobis)";
	
	private final YearService yearService;
	
	private final ConsumptionService consumptionService;
	
	private final CoverageService coverageService;

	public ApplicationFrame(YearService yearService, ConsumptionService consumptionService, CoverageService coverageService) throws HeadlessException {
		super(TITLE);
		this.yearService = yearService;
		this.consumptionService = consumptionService;
		this.coverageService = coverageService;

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		super.setSize(screen);

		final JDesktopPane desktop = new JDesktopPane();
		desktop.setDragMode(JDesktopPane.LIVE_DRAG_MODE);
		super.setContentPane(desktop);
		super.setJMenuBar(new ApplicationMenu(this));
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		super.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				Arrays.stream(desktop.getComponents()).forEach(component -> {
					if (JInternalFrame.class.isAssignableFrom(component.getClass())) {
						// allow other frames to run close operations
						((JInternalFrame)component).doDefaultCloseAction();
					}
				});
			}
		});

		super.setVisible(true);
	}

	@Override
	public void openConsumption(Report report) {
		if (report != null) {
			Container container = super.getContentPane();
			JInternalFrame frame = new ConsumptionFrame(report, yearService, consumptionService);
			container.add(frame);
			try {
				frame.setSelected(true);
			} catch (PropertyVetoException e) {
				// ignore
			}
		}
	}

	@Override
	public void openCoverage() {
		Container container = super.getContentPane();
		JInternalFrame frame = new CoverageFrame(yearService, coverageService, this);
		container.add(frame);
		try {
			frame.setSelected(true);
		} catch (PropertyVetoException e) {
			// ignore
		}
	}

	@Override
	public void exit() {
		super.dispose();
	}

	@Override
	public void update() {
		Component c;
		Container container = super.getContentPane();
		for (int i = 0; i < container.getComponentCount(); i++) {
			c = container.getComponent(i);
			if (Updateable.class.isAssignableFrom(c.getClass())) {
				((Updateable)c).update();
			}
		}
	}

}
