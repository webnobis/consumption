package com.webnobis.consumption.repository.impl;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.webnobis.consumption.model.Coverage;
import com.webnobis.consumption.repository.RepositoryService;

public class RepositoryServiceImpl implements RepositoryService {

	public static final String DEFAULT_FOLDER = "coverage";

	private static final String HEADER = ResourceBundle.getBundle("repository").getString("repository.header");

	private static final Logger log = Logger.getLogger(RepositoryServiceImpl.class);

	private final Path folder;

	public RepositoryServiceImpl(Path folder) {
		this.folder = Optional.ofNullable(folder).orElse(Paths.get(DEFAULT_FOLDER));
		log.info("working folder is:" + folder.toAbsolutePath().toString());
	}

	@Override
	public Set<Coverage> findCoverages() {
		Set<Coverage> coverages = new CopyOnWriteArraySet<>();
		try {
			Files.newDirectoryStream(folder, new CoverageFileFilter()).forEach(file -> coverages.addAll(findCoverages(file)));
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw new UncheckedIOException(e);
		}
		return coverages;
	}

	protected List<Coverage> findCoverages(Path file) {
		try {
			return Files.readAllLines(file, StandardCharsets.UTF_8)
					.parallelStream()
					.map(new LineToCoverageTransformer())
					.filter(coverage -> (coverage != null))
					.collect(Collectors.toList());
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw new UncheckedIOException(e);
		}
	}

	@Override
	public void storeCoverages(Collection<Coverage> coverages) {
		Map<String, List<Coverage>> fileNameMappedCoverages = Optional.ofNullable(coverages).orElse(Collections.emptySet())
				.parallelStream()
				.filter(coverage -> (coverage != null))
				.collect(Collectors.groupingBy(new CoverageToFileNameMapper()));

		fileNameMappedCoverages.forEach(this::store);
	}

	private void store(String fileName, List<Coverage> coverages) {
		log.info("store file " + fileName + " with " + coverages.size() + " coverages");
		Path file = folder.resolve(fileName);
		
		List<Coverage> coveragesOfFile;
		if (Files.exists(file)) {
			coveragesOfFile = findCoverages(file);
			coveragesOfFile.removeAll(coverages);
			coveragesOfFile.addAll(coverages);
		} else {
			coveragesOfFile = coverages;
		}

		List<String> lines = coveragesOfFile
				.stream()
				.filter(coverage -> (coverage != null))
				.map(new CoverageToLineTransformer())
				.filter(line -> (line != null))
				.sorted()
				.collect(Collectors.toList());
		lines.add(0, HEADER);

		try {
			Files.write(file, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.SYNC);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw new UncheckedIOException(e);
		}
	}

}
