package com.webnobis.consumption.business;

import java.time.Month;
import java.util.Collection;
import java.util.SortedSet;

import com.webnobis.consumption.model.Coverage;

public interface CoverageService {
	
	public SortedSet<Coverage> createNewCoveragesOfCurrentMonth();
	
	public SortedSet<Coverage> createNewCoveragesOfLastMonth();
	
	public SortedSet<Coverage> getCoverages(int year, Month month);
	
	public void storeCoverages(Collection<Coverage> coverages);

}
