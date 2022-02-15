package com.webnobis.consumption.repository;

import java.util.Collection;
import java.util.Set;

import com.webnobis.consumption.model.Coverage;

/**
 * Coverage repository service
 * 
 * @author steffen
 *
 */
public interface RepositoryService {

	/**
	 * Finds all coverages
	 * 
	 * @return coverages
	 */
	Set<Coverage> findCoverages();

	/**
	 * Stores all coverages
	 * 
	 * @param coverages coverages
	 */
	void storeCoverages(Collection<Coverage> coverages);

}
