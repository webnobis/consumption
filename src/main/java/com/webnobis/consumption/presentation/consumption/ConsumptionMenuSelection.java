package com.webnobis.consumption.presentation.consumption;

import java.util.Collection;

import com.webnobis.consumption.model.Medium;

public interface ConsumptionMenuSelection {
	
	public Medium getSelectedMedium();
	
	public Collection<Integer> getSelectedYears();

}
