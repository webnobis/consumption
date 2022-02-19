package com.webnobis.consumption.presentation.consumption;

import java.time.Month;
import java.util.Collection;

import com.webnobis.consumption.model.Medium;

public interface ConsumptionMenuSelection {
	
	Month getSelectedMonth();
	
	Medium getSelectedMedium();
	
	boolean isLast12MonthSelected();
	
	Collection<Integer> getSelectedYears();

}
