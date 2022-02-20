package com.webnobis.consumption.presentation.consumption;

import java.awt.Container;
import java.awt.GridLayout;
import java.util.List;
import java.util.Objects;

import javax.swing.JInternalFrame;

import com.webnobis.consumption.business.ConsumptionService;
import com.webnobis.consumption.business.YearService;
import com.webnobis.consumption.model.Consumption;
import com.webnobis.consumption.presentation.Updateable;

public class ConsumptionFrame extends JInternalFrame implements Updateable {

	private static final long serialVersionUID = 1L;

	private final boolean yearReport;

	private final ConsumptionService consumptionService;

	private final ConsumptionDiagramPanel panel;

	private final ConsumptionMenuSelection menuSelection;

	private final Updateable menuUpdateable;

	public ConsumptionFrame(Report report, YearService yearService, ConsumptionService consumptionService) {
		super(Objects.requireNonNull(report, "report is null").getTitle(), false, true, true);
		yearReport = Report.YEAR.equals(report);
		this.consumptionService = Objects.requireNonNull(consumptionService, "consumptionService is null");
		this.panel = new ConsumptionDiagramPanel();
		Container container = super.getContentPane();
		container.setLayout(new GridLayout(1, 1));
		container.add(panel);

		ConsumptionMenu consumptionMenu = new ConsumptionMenu(report, yearService, this);
		menuSelection = consumptionMenu;
		menuUpdateable = consumptionMenu;
		super.setJMenuBar(consumptionMenu);

		super.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);

		setSize(800, 600);

		update();
		super.setVisible(true);
	}

	@Override
	public void update() {
		menuUpdateable.update();
		List<Consumption> consumptions;
		if (yearReport) {
			consumptions = consumptionService.getAnnualConsumptions(menuSelection.getSelectedMedium(),
					menuSelection.getSelectedYears(), menuSelection.isLast12MonthSelected());
			panel.update(consumptions, Report.YEAR);
		} else if (menuSelection.getSelectedMonth() == null) {
			consumptions = consumptionService.getMonthlyAnnualConsumptions(menuSelection.getSelectedMedium(),
					menuSelection.getSelectedYears(), menuSelection.isDecemberLastYearSelected());
			panel.update(consumptions, Report.ALL_MONTH);
		} else {
			consumptions = consumptionService.getMonthlyConsumptions(menuSelection.getSelectedMedium(),
					menuSelection.getSelectedYears(), menuSelection.getSelectedMonth());
			panel.update(consumptions, Report.ONE_MONTH);
		}
	}

}
