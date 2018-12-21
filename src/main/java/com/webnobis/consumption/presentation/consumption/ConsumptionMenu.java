package com.webnobis.consumption.presentation.consumption;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Collectors;

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
	
	private static final int DEFAULT_SELECTED_LAST_YEARS = 3;

	private final YearService yearService;

	private final Updateable diagramUpdateable;

	private final JMenu yearSelection;

	private final Map<Integer, JMenuItem> yearItems;

	private final Map<Medium, JMenuItem> mediumItems;

	public ConsumptionMenu(YearService yearService, Updateable diagramUpdateable) {
		super();
		this.yearService = Objects.requireNonNull(yearService, "yearService is null");
		this.diagramUpdateable = Objects.requireNonNull(diagramUpdateable, "diagramUpdateable is null");
		
		yearSelection = new JMenu("Jahre");
		yearItems = new HashMap<>();
		
		update();

		JMenu mediumSelection = new JMenu("Medium");
		mediumItems = new HashMap<>();
		ButtonGroup group = new ButtonGroup();
		Arrays.stream(Medium.values()).forEach(medium -> {
			JMenuItem item = new JRadioButtonMenuItem(medium.name());
			item.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent event) {
					diagramUpdateable.update();
				}

			});
			if (DEFAULT_SELECTED_MEDIUM.equals(medium)) {
				item.setSelected(true);
			}
			mediumSelection.add(item);
			group.add(item);
			mediumItems.put(medium, item);
		});
		
		super.add(yearSelection);
		super.add(mediumSelection);
	}

	@Override
	public Medium getSelectedMedium() {
		return mediumItems.entrySet()
				.parallelStream()
				.filter(entry -> entry.getValue().isSelected())
				.map(entry -> entry.getKey())
				.findFirst().orElse(DEFAULT_SELECTED_MEDIUM);
	}

	@Override
	public Collection<Integer> getSelectedYears() {
		return yearItems.entrySet()
				.parallelStream()
				.filter(entry -> entry.getValue().isSelected())
				.map(entry -> entry.getKey())
				.collect(Collectors.toSet());
	}

	@Override
	public void update() {
		yearService.getYears()
			.stream()
			.filter(year -> !yearItems.containsKey(year))
			.forEach(year -> {
				JMenuItem item = new JCheckBoxMenuItem(String.valueOf(year));
				item.addActionListener(new ActionListener() {
	
					@Override
					public void actionPerformed(ActionEvent event) {
						diagramUpdateable.update();
					}	
				});
				yearItems.put(year, item);
			});
		
		yearSelection.removeAll();
		NavigableSet<Integer> years = new TreeSet<>(yearItems.keySet()).descendingSet();
		years.stream()
			.map(year -> yearItems.get(year))
			.forEach(item -> yearSelection.add(item));
		
		// if nothing selected, select newest years
		if (yearItems.values().stream().allMatch(item -> !item.isSelected())) {
			years.stream()
				.limit(DEFAULT_SELECTED_LAST_YEARS)
				.map(year -> yearItems.get(year))
				.forEach(item -> item.setSelected(true));
		}
	}

}
