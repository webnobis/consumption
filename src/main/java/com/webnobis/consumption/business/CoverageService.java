package com.webnobis.consumption.business;

import java.time.Month;
import java.util.Collection;
import java.util.List;

import com.webnobis.consumption.model.Coverage;

/**
 * Coverage service
 * 
 * @author steffen
 *
 */
public interface CoverageService {

	/**
	 * Create new coverages without preset dial count
	 * 
	 * @return coverages of current month
	 */
	List<Coverage> createNewCoveragesOfCurrentMonth();

	/**
	 * Create new coverages without preset dial count
	 * 
	 * @return coverages of last month
	 */
	List<Coverage> createNewCoveragesOfLastMonth();

	/**
	 * Get coverages
	 * 
	 * @param year  year
	 * @param month month
	 * @return coverages of year and month
	 */
	List<Coverage> getCoverages(int year, Month month);

	/**
	 * Stores the coverags
	 * 
	 * @param coverages coverages
	 */
	void storeCoverages(Collection<Coverage> coverages);

}
