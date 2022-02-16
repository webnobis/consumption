package com.webnobis.consumption.presentation.coverage;

import java.awt.GridLayout;
import java.time.Month;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.SortedSet;
import java.util.stream.Collectors;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.webnobis.consumption.model.Coverage;
import com.webnobis.consumption.model.Medium;
import com.webnobis.consumption.model.transformer.DialCountTransformer;

public class CoveragePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final int year;

	private final Month month;

	private final Map<Medium, MediumPanel> mediums;

	public CoveragePanel(SortedSet<Coverage> coverages, ShouldStore shouldStore) {
		super(new GridLayout(Objects.requireNonNull(coverages, "coverages is null").size() + 3, 1, 0, 2));
		if (coverages.isEmpty()) {
			throw new IllegalArgumentException("coverages is empty");
		}

		this.add(new JLabel("Erfassungsmonat"));
		Coverage firstCoverage = coverages.first();
		year = firstCoverage.year();
		month = firstCoverage.month();
		this.add(new MonthYearPanel(month, year));

		this.add(new JLabel("Zählerstände"));
		mediums = coverages.stream().collect(Collectors.toMap(coverage -> coverage.medium(), coverage -> {
			MediumPanel panel = new MediumPanel(coverage.medium(), DialCountTransformer.toText(coverage.dialCount()),
					shouldStore);
			this.add(panel);
			return panel;
		}));
	}

	public Collection<Coverage> getCoverages() {
		return mediums.entrySet().stream().map(entry -> new Coverage(year, month, entry.getKey(),
				DialCountTransformer.toFloat(entry.getValue().getDialCount()))).collect(Collectors.toSet());
	}

}
