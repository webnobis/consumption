package com.webnobis.consumption.repository;

import java.util.Collection;
import java.util.Set;

import com.webnobis.consumption.model.Coverage;

public interface RepositoryService {
	
	public Set<Coverage> findCoverages();
	
	public void storeCoverages(Collection<Coverage> coverages);

}
