package com.webnobis.consumption.presentation.consumption;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.SortedSet;

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

	public void update(SortedSet<Consumption> consumptions) {
		if (Optional.ofNullable(consumptions).filter(col -> col.size() > 1).isPresent()) {
			DefaultCategoryDataset chartDataSet = new DefaultCategoryDataset();
			Medium medium = Objects.requireNonNull(consumptions.first().getMedium());
			switch (report) {
			case MONTH:
				// fill missing months of first year
				String firstYear = String.valueOf(consumptions.first().getYear());
				Arrays.stream(Month.values())
				.filter(month -> (month.compareTo(consumptions.first().getMonth()) < 0))
				.forEach(month -> {
					chartDataSet.addValue(0, firstYear, month.getDisplayName(TextStyle.SHORT, Locale.GERMAN));
				});
				// all months
				consumptions.forEach(consumption -> {
					chartDataSet.addValue(consumption.getConsumption(), String.valueOf(consumption.getYear()), consumption.getMonth().getDisplayName(TextStyle.SHORT, Locale.GERMAN));
				});
				super.setChart(ChartFactory.createLineChart(medium.name() + TITLE_END, CATEGORY, medium.getUnit(), chartDataSet, PlotOrientation.VERTICAL, true, true, false));
				break;
			case YEAR:
				consumptions.forEach(consumption -> {
					String barText = (Month.DECEMBER.equals(consumption.getMonth())) ? String.valueOf(consumption.getYear()) : "Letzte 12 Monate";
					chartDataSet.addValue(consumption.getConsumption(), barText, "Summe");
				});
				super.setChart(ChartFactory.createBarChart(medium.name() + TITLE_END, CATEGORY, medium.getUnit(), chartDataSet, PlotOrientation.VERTICAL, true, true, false));
				break;
			default:
			}
			super.revalidate();
		}
	}

}
