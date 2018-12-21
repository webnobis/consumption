package com.webnobis.consumption.presentation.coverage;

import java.time.Month;

public interface CoverageMenuSelection {
	
	public void create(boolean lastMonth);
	
	public void open(int year, Month month);
	
	public void store();

}
