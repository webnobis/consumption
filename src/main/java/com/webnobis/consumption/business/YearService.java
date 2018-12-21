package com.webnobis.consumption.business;

import java.time.Month;
import java.util.Map;
import java.util.Set;

public interface YearService {
	
	public Set<Integer> getYears();
	
	public Map<Integer,Set<Month>> getYearsWithMonths();

}
