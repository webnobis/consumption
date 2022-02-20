package com.webnobis.consumption.presentation.consumption;

import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.PlotOrientation;

import com.webnobis.consumption.model.Consumption;
import com.webnobis.consumption.model.Medium;
import com.webnobis.consumption.presentation.spi.jfreechart.ColumnSortedDefaultCategoryDataset;
import com.webnobis.consumption.presentation.spi.jfreechart.MonthComparable;

public class ConsumptionDiagramPanel extends ChartPanel {

	private static final long serialVersionUID = 1L;

	private static final String TITLE_END = "-Verbrauch";

	private static final String CATEGORY = "Monat";

	public ConsumptionDiagramPanel() {
		super(null, false, true, false, false, false);
	}

	public void update(List<Consumption> consumptions, Report report) {
		if (Optional.ofNullable(consumptions).filter(col -> !col.isEmpty()).isPresent()) {
			ColumnSortedDefaultCategoryDataset chartDataSet = new ColumnSortedDefaultCategoryDataset();
			Medium medium = Objects.requireNonNull(consumptions.get(0).medium());
			switch (Objects.requireNonNull(report, "report is null")) {
			case YEAR:
				consumptions.forEach(consumption -> {
					String barText = consumption.year() > 0 ? String.valueOf(consumption.year()) : "Letzte 12 Monate";
					chartDataSet.addValue(consumption.consumption(), barText, "Summe");
				});
				super.setChart(ChartFactory.createBarChart(medium.name() + TITLE_END, CATEGORY, medium.getUnit(),
						chartDataSet, PlotOrientation.VERTICAL, true, true, false));
				break;
			case ALL_MONTH:
				// fill missing months of first year
				String firstYear = String.valueOf(consumptions.get(0).year());
				Arrays.stream(Month.values()).filter(month -> (month.compareTo(consumptions.get(0).month()) < 0))
						.forEach(month -> chartDataSet.addValue(0, firstYear, new MonthComparable(month, false)));
				// all months
				consumptions.forEach(consumption -> chartDataSet.addValue(consumption.consumption(),
						String.valueOf(consumption.year()),
						new MonthComparable(consumption.month(), consumption.monthFromLastYear())));
				// correct order of months
				chartDataSet.sort();
				super.setChart(ChartFactory.createLineChart(medium.name() + TITLE_END, CATEGORY, medium.getUnit(),
						chartDataSet, PlotOrientation.VERTICAL, true, true, false));
				break;
			default:
				consumptions.forEach(consumption -> {
					chartDataSet.addValue(consumption.consumption(), String.valueOf(consumption.year()),
							new MonthComparable(consumption.month(), false));
				});
				super.setChart(ChartFactory.createBarChart(medium.name() + TITLE_END, CATEGORY, medium.getUnit(),
						chartDataSet, PlotOrientation.VERTICAL, true, true, false));
			}
			super.revalidate();
		}
	}

}
