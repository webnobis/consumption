package com.webnobis.consumption.business;

import java.util.Collection;
import java.util.Set;

import com.webnobis.consumption.model.Consumption;
import com.webnobis.consumption.model.Medium;

public interface ConsumptionService {
	
	public Set<Consumption> getConsumptions(Medium medium, Collection<Integer> years, boolean monthly);

}
