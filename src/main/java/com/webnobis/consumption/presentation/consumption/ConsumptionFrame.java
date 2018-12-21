package com.webnobis.consumption.presentation.consumption;

import java.awt.Container;
import java.awt.GridLayout;
import java.util.Objects;
import java.util.Set;

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
		
		ConsumptionMenu consumptionMenu = new ConsumptionMenu(Objects.requireNonNull(yearService, "yearService is null"), this);
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
		Set<Consumption> consumptions = consumptionService.getConsumptions(menuSelection.getSelectedMedium(), menuSelection.getSelectedYears(), Report.MONTH.equals(report));
		panel.update(consumptions);
	}

}
