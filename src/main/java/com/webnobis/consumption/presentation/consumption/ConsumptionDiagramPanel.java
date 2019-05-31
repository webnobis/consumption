package com.webnobis.consumption.presentation.consumption;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import com.webnobis.consumption.model.Consumption;
import com.webnobis.consumption.model.Medium;

public class ConsumptionDiagramPanel extends ChartPanel {

	private static final long serialVersionUID = 1L;

	private static final String TITLE_END = "-Verbrauch";

	private static final String CATEGORY = "Monat";

	private final Report report;

	public ConsumptionDiagramPanel(Report report) {
		super(null, false, true, false, false, false);
		this.report = Objects.requireNonNull(report, "report is null");
	}

	public void update(Set<Consumption> consumptions) {
		if (consumptions != null && !consumptions.isEmpty()) {
			DefaultCategoryDataset chartDataSet = new DefaultCategoryDataset();
			Optional<Consumption> firstConsumption = consumptions.stream().sorted().findFirst();
			
			if (Report.MONTH.equals(report)) {
				// fill missing rows of first year
				firstConsumption.ifPresent(consumption -> {
					Arrays.stream(Month.values())
						.filter(month -> (month.compareTo(consumption.getMonth()) < 0))
						.forEach(month -> {
							chartDataSet.addValue(0, String.valueOf(consumption.getYear()), month.getDisplayName(TextStyle.SHORT, Locale.GERMAN));
						});
				});
			}
			
			consumptions.forEach(consumption -> {
					String barText = (Month.DECEMBER.equals(consumption.getMonth()))? String.valueOf(consumption.getYear()): "Letzte 12 Monate";
					chartDataSet.addValue(consumption.getConsumption(), barText, "Summe");
			});
			
			firstConsumption.ifPresent(consumption -> {
				Medium medium = consumption.getMedium();
				switch (report) {
				case MONTH:
					super.setChart(ChartFactory.createLineChart(medium.name() + TITLE_END, CATEGORY, medium.getUnit(), chartDataSet, PlotOrientation.VERTICAL, true, true, false));
					break;
				case YEAR:
					super.setChart(ChartFactory.createBarChart(medium.name() + TITLE_END, CATEGORY, medium.getUnit(), chartDataSet, PlotOrientation.VERTICAL, true, true, false));
					break;
				default:
				}
				super.revalidate();
			});
		}
	}

}
