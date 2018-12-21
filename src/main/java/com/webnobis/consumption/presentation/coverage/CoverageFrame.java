package com.webnobis.consumption.presentation.coverage;

import java.awt.Container;
import java.awt.GridLayout;
import java.time.Month;
import java.time.YearMonth;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import com.webnobis.consumption.business.CoverageService;
import com.webnobis.consumption.business.YearService;
import com.webnobis.consumption.presentation.Updateable;

public class CoverageFrame extends JInternalFrame implements CoverageMenuSelection {

	private static final long serialVersionUID = 1L;
	
	private static final int CHANGEABLE_MONTHS = 6;
	
	private static final YearMonth firstChangeableYearMonth = YearMonth.now().minusMonths(CHANGEABLE_MONTHS);
	
	private final CoverageService coverageService;
	
	private final Updateable updateable;
	
	private final Updateable menuUpdateable;
	
	private final Storable storable;
	
	private final AtomicBoolean shouldStore;
	
	private CoveragePanel coveragePanel;

	public CoverageFrame(YearService yearService, CoverageService coverageService, Updateable updateable) {
		super("Erfassung", false, true, false);
		this.coverageService = Objects.requireNonNull(coverageService, "coverageService is null");
		this.updateable = Objects.requireNonNull(updateable, "updateable is null");

		Container container = super.getContentPane();
		container.setLayout(new GridLayout(1, 1));
		
		CoverageMenu coverageMenu = new CoverageMenu(Objects.requireNonNull(yearService, "yearService is null"), this);
		menuUpdateable = coverageMenu;
		storable = coverageMenu;
		super.setJMenuBar(coverageMenu);
		
		shouldStore = new AtomicBoolean();
		super.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
		super.addInternalFrameListener(new InternalFrameAdapter() {

			@Override
			public void internalFrameClosing(InternalFrameEvent event) {
				if (shouldStore.get() && JOptionPane.showConfirmDialog(getDesktopPane(), "Änderungen speichern?", "Speichern", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
					store();
				}
			}
		});
		
		// new for current month at first 
		create(false);
		
		super.setVisible(true);
	}

	@Override
	public void create(boolean lastMonth) {
		if (lastMonth) {
			change(new CoveragePanel(coverageService.createNewCoveragesOfLastMonth(), new ShouldStore() {
				
				@Override
				public void shouldStore() {
					shouldStore.set(true);
				}
			}));
		} else {
			change(new CoveragePanel(coverageService.createNewCoveragesOfCurrentMonth(), new ShouldStore() {
				
				@Override
				public void shouldStore() {
					shouldStore.set(true);
				}
			}));
		}
		storable.setStorable(true);
	}

	@Override
	public void open(int year, Month month) {
		YearMonth yearMonth = YearMonth.of(year, Objects.requireNonNull(month, "month is null"));
		boolean readonly = yearMonth.isBefore(firstChangeableYearMonth);
		change(new CoveragePanel(coverageService.getCoverages(year, month), (readonly)? null: new ShouldStore() {
			
			@Override
			public void shouldStore() {
				shouldStore.set(true);
			}
		}));
		storable.setStorable(!readonly);
	}

	private void change(CoveragePanel coveragePanel) {
		if (coveragePanel != null) {
			Container container = super.getContentPane();
			container.removeAll();
			container.add(coveragePanel);
			this.pack();
			this.coveragePanel = coveragePanel;
		}
	}

	@Override
	public void store() {
		if (coveragePanel != null) {
			coverageService.storeCoverages(coveragePanel.getCoverages());
			menuUpdateable.update();
			updateable.update();
			shouldStore.set(false);
		}
	}

}
