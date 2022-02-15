package com.webnobis.consumption.repository.file;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webnobis.consumption.model.Coverage;
import com.webnobis.consumption.repository.RepositoryService;

/**
 * File based repository service
 * 
 * @author steffen
 *
 */
public class FileRepositoryService implements RepositoryService {

	/**
	 * File extension
	 */
	public static final String FILE_EXT = ".csv";

	/**
	 * Default folder
	 */
	public static final String DEFAULT_FOLDER = "example";

	private static final String HEADER = ResourceBundle.getBundle("repository").getString("repository.header");

	private static final Logger log = LoggerFactory.getLogger(FileRepositoryService.class);

	private static final Function<Coverage, String> filenameBuilder = coverage -> MessageFormat.format("{0}{1}{2}",
			Coverage.class.getSimpleName(), String.valueOf(Objects.requireNonNull(coverage).getYear()), FILE_EXT);

	private final Path folder;

	private final Function<String, Coverage> lineToCoverageTransformer;

	private final Function<Coverage, String> coverageToLineTransformer;

	/**
	 * Folder used file repository service
	 * 
	 * @param folder folder of coverage files
	 */
	public FileRepositoryService(Path folder) {
		this.folder = Optional.ofNullable(folder).orElse(Paths.get(DEFAULT_FOLDER));
		log.info("working folder is {}", this.folder.toAbsolutePath());
		lineToCoverageTransformer = new LineToCoverageTransformer();
		coverageToLineTransformer = new CoverageToLineTransformer();
	}

	@Override
	public Set<Coverage> findCoverages() {
		if (!Files.exists(folder)) {
			return Collections.emptySet();
		}
		Stream<Path> coveragesStream = null;
		try {
			coveragesStream = Files.walk(folder);
			return coveragesStream.filter(Files::isRegularFile).filter(file -> file.toString().endsWith(FILE_EXT))
					.flatMap(this::readFile).collect(Collectors.toCollection(CopyOnWriteArraySet::new));
		} catch (IOException e) {
			log.error("coverages of folder {} not readable: {}", folder, e);
			throw new UncheckedIOException(e);
		} finally {
			Optional.ofNullable(coveragesStream).ifPresent(Stream::close);
		}
	}

	/**
	 * Read all coverages of file
	 * 
	 * @param file file
	 * @return coverages
	 */
	protected Stream<Coverage> readFile(Path file) {
		log.debug("read coverages of file {}", file);
		try {
			return Files.readAllLines(file, StandardCharsets.UTF_8).parallelStream()
					.filter(line -> !HEADER.equals(line)).map(lineToCoverageTransformer).filter(Objects::nonNull);
		} catch (IOException e) {
			log.error("coverages of file {} not readable: {}", file, e);
			throw new UncheckedIOException(e);
		}
	}

	@Override
	public void storeCoverages(Collection<Coverage> coverages) {
		Optional.ofNullable(coverages).orElse(Collections.emptySet()).parallelStream().filter(Objects::nonNull)
				.collect(Collectors.groupingBy(filenameBuilder)).forEach(this::store);
	}

	private void store(String filename, List<Coverage> coverages) {
		Path file = folder.resolve(filename);
		List<String> coveragesToStore = Stream.concat(Optional.of(file).filter(Files::exists).map(this::readFile)
				.map(stream -> stream.filter(coverage -> !coverages.contains(coverage))).orElseGet(Stream::empty),
				coverages.parallelStream()).map(coverageToLineTransformer).toList();
		log.info("store file {} with {} coverages", filename, coveragesToStore.size());
		try {
			Files.write(file, coveragesToStore, StandardCharsets.UTF_8);
		} catch (IOException e) {
			log.error("coverages to file {} not storable: {}", file, e);
			throw new UncheckedIOException(e);
		}
	}

}
