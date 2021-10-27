package com.webnobis.consumption.migrator.v1;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webnobis.consumption.model.Coverage;

public class RepositoryServiceImpl extends com.webnobis.consumption.repository.impl.RepositoryServiceImpl {

	private static final Logger log = LoggerFactory.getLogger(RepositoryServiceImpl.class);

	public RepositoryServiceImpl(Path folder) {
		super(folder);
	}

	@Override
	protected List<Coverage> findCoverages(Path file) {
		try {
			return Files.readAllLines(file, StandardCharsets.UTF_8)
					.parallelStream()
					.map(new FileNameWithLineToCoverageTransformer(file.getFileName().toString()))
					.filter(coverage -> (coverage != null))
					.collect(Collectors.toList());
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw new UncheckedIOException(e);
		}
	}

}
