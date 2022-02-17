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

	private final Report report;

	private final ConsumptionService consumptionService;

	private final ConsumptionDiagramPanel panel;

	private final ConsumptionMenuSelection menuSelection;

	private final Updateable menuUpdateable;

	public ConsumptionFrame(Report report, YearService yearService, ConsumptionService consumptionService) {
		super(Objects.requireNonNull(report, "report is null").getTitle(), false, true, true);
		this.report = report;
		this.consumptionService = Objects.requireNonNull(consumptionService, "consumptionService is null");
		this.panel = new ConsumptionDiagramPanel(report);
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
		switch (report) {
		case YEAR:
			consumptions = consumptionService.getAnnualConsumptions(menuSelection.getSelectedMedium(),
					menuSelection.getSelectedYears(), menuSelection.isLast12MonthSelected());
			break;
		default:
			consumptions = consumptionService.getMonthlyAnnualConsumptions(menuSelection.getSelectedMedium(),
					menuSelection.getSelectedYears());
		}
		panel.update(consumptions);
	}

}
