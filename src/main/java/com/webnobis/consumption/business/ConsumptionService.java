package com.webnobis.consumption.business;

import java.util.Collection;
import java.util.SortedSet;

import com.webnobis.consumption.model.Consumption;
import com.webnobis.consumption.model.Medium;

public interface ConsumptionService {
	
	public SortedSet<Consumption> getConsumptions(Medium medium, Collection<Integer> years, boolean monthly);

}
