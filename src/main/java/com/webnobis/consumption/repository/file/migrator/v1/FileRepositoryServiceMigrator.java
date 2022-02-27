package com.webnobis.consumption.repository.file.migrator.v1;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webnobis.consumption.model.Coverage;
import com.webnobis.consumption.repository.file.FileRepositoryService;

/**
 * Migrator of old files with coverages
 * 
 * @author steffen
 *
 */
public class FileRepositoryServiceMigrator extends FileRepositoryService {

	private static final Logger log = LoggerFactory.getLogger(FileRepositoryServiceMigrator.class);

	/**
	 * Folder with coverages
	 * 
	 * @param folder folder
	 */
	public FileRepositoryServiceMigrator(Path folder) {
		super(folder);
	}

	@Override
	protected Stream<Coverage> readFile(Path file) {
		try {
			return Files.readAllLines(file, StandardCharsets.UTF_8).parallelStream()
					.map(new FileNameWithLineToCoverageTransformer(file.getFileName().toString()))
					.filter(Objects::nonNull);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw new UncheckedIOException(e);
		}
	}

}
