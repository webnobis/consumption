package com.webnobis.consumption.presentation.consumption;

import java.util.Collection;

import com.webnobis.consumption.model.Medium;

public interface ConsumptionMenuSelection {
	
	Medium getSelectedMedium();
	
	boolean isLast12MonthSelected();
	
	Collection<Integer> getSelectedYears();

}
