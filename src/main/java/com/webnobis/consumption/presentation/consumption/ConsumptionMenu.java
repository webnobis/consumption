package com.webnobis.consumption.presentation.consumption;

import java.time.Month;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import com.webnobis.consumption.business.YearService;
import com.webnobis.consumption.model.Medium;
import com.webnobis.consumption.presentation.Updateable;

public class ConsumptionMenu extends JMenuBar implements ConsumptionMenuSelection, Updateable {

	private static final long serialVersionUID = 1L;

	private static final Medium DEFAULT_SELECTED_MEDIUM = Medium.STROM;

	private static final int DEFAULT_SELECTED_LAST_YEARS = 5;

	private final Report report;

	private final transient YearService yearService;

	private final transient Updateable diagramUpdateable;

	private final JMenu yearMenu;

	private final JMenuItem last12MonthItem;

	private final JMenu monthMenu;

	private final JMenuItem decemberLastYearItem;

	private final JMenu mediumMenu;

	public ConsumptionMenu(Report report, YearService yearService, Updateable diagramUpdateable) {
		super();
		this.report = Objects.requireNonNull(report, "report is null");
		this.yearService = Objects.requireNonNull(yearService, "yearService is null");
		this.diagramUpdateable = Objects.requireNonNull(diagramUpdateable, "diagramUpdateable is null");

		yearMenu = new JMenu("Jahre");
		super.add(yearMenu);

		if (Report.YEAR.equals(report)) {
			last12MonthItem = new JCheckBoxMenuItem("Letzte 12 Monate");
			last12MonthItem.addActionListener(event -> diagramUpdateable.update());
			last12MonthItem.setSelected(true);

			monthMenu = null;
			decemberLastYearItem = null;
		} else {
			last12MonthItem = null;

			monthMenu = new JMenu("Monate");
			ButtonGroup monthGroup = new ButtonGroup();
			decemberLastYearItem = new JCheckBoxMenuItem("Beginnend mit Dezember des Vorjahres");
			decemberLastYearItem.addActionListener(event -> diagramUpdateable.update());
			decemberLastYearItem.setSelected(true);
			monthMenu.add(decemberLastYearItem);
			monthMenu.addSeparator();
			JMenuItem allMonthItem = new JRadioButtonMenuItem("Alle Monate");
			allMonthItem.addActionListener(event -> diagramUpdateable.update());
			allMonthItem.addActionListener(event -> decemberLastYearItem.setEnabled(true));
			allMonthItem.setSelected(true);
			monthGroup.add(allMonthItem);
			monthMenu.add(allMonthItem);
			monthMenu.addSeparator();
			Arrays.stream(Month.values()).sorted().map(MonthMenuItem::new).forEach(item -> {
				item.addActionListener(event -> diagramUpdateable.update());
				item.addActionListener(event -> decemberLastYearItem.setEnabled(false));
				monthGroup.add(item);
				monthMenu.add(item);
			});
			super.add(monthMenu);
		}
		update(yearService.getYears().stream().sorted(Comparator.reverseOrder()).limit(DEFAULT_SELECTED_LAST_YEARS)
				.toList());

		mediumMenu = new JMenu("Medium");
		ButtonGroup mediumGroup = new ButtonGroup();
		Arrays.stream(Medium.values()).sorted().map(MediumMenuItem::new).forEach(item -> {
			item.addActionListener(event -> diagramUpdateable.update());
			item.setSelected(DEFAULT_SELECTED_MEDIUM.equals(item.getMedium()));
			mediumGroup.add(item);
			mediumMenu.add(item);
		});
		super.add(mediumMenu);
	}

	private void update(Collection<Integer> selectedYears) {
		yearMenu.removeAll();
		if (Report.YEAR.equals(report)) {
			yearMenu.add(last12MonthItem);
		}
		yearService.getYears().stream().sorted(Comparator.reverseOrder()).map(YearMenuItem::new).forEach(item -> {
			item.addActionListener(event -> diagramUpdateable.update());
			item.setSelected(selectedYears.contains(item.getYear()));
			yearMenu.add(item);
		});
	}

	@Override
	public void update() {
		update(getSelectedYears());
	}

	@Override
	public Month getSelectedMonth() {
		return Optional.ofNullable(monthMenu)
				.flatMap(menu -> IntStream.range(0, menu.getItemCount()).mapToObj(menu::getItem)
						.filter(Objects::nonNull).filter(item -> MonthMenuItem.class.equals(item.getClass()))
						.filter(JMenuItem::isSelected).map(item -> ((MonthMenuItem) item).getMonth()).findFirst())
				.orElse(null);
	}

	@Override
	public Medium getSelectedMedium() {
		return IntStream.range(0, mediumMenu.getItemCount()).mapToObj(mediumMenu::getItem).filter(JMenuItem::isSelected)
				.map(item -> ((MediumMenuItem) item).getMedium()).findFirst().orElse(DEFAULT_SELECTED_MEDIUM);
	}

	@Override
	public boolean isLast12MonthSelected() {
		return Optional.ofNullable(last12MonthItem).map(JMenuItem::isSelected).orElse(false);
	}

	@Override
	public boolean isDecemberLastYearSelected() {
		return Optional.ofNullable(decemberLastYearItem).map(JMenuItem::isSelected).orElse(false);
	}

	@Override
	public Collection<Integer> getSelectedYears() {
		return IntStream.range(0, yearMenu.getItemCount()).mapToObj(yearMenu::getItem)
				.filter(item -> YearMenuItem.class.equals(item.getClass())).map(YearMenuItem.class::cast)
				.filter(YearMenuItem::isSelected).map(YearMenuItem::getYear).toList();
	}

}
