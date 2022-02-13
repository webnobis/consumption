package com.webnobis.consumption.repository.impl;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import com.webnobis.consumption.model.Coverage;

/**
 * Coverage extension
 * 
 * @author steffen
 *
 */
public class CoverageExtension
		implements ParameterResolver, BeforeAllCallback, BeforeEachCallback, AfterEachCallback, AfterAllCallback {

	private static final Function<String, Coverage> lineToCoverageTransformer = new LineToCoverageTransformer();

	private Path tmpFolder;

	private CoverageReader coverageReader;

	/**
	 * Creates a temporary folder
	 */
	@Override
	public void beforeAll(ExtensionContext context) throws Exception {
		tmpFolder = Files.createTempDirectory(CoverageExtension.class.getSimpleName());
	}

	/**
	 * Copies the test resources coverage files to the temporary folder
	 */
	@Override
	public void beforeEach(ExtensionContext context) throws Exception {
		IntStream.rangeClosed(2013, 2015).mapToObj(String::valueOf)
				.map(year -> MessageFormat.format("Coverage{0}.csv", year)).forEach(file -> {
					try {
						Files.copy(ClassLoader.getSystemResourceAsStream(file), tmpFolder.resolve(file));
					} catch (IOException e) {
						throw new UncheckedIOException(e);
					}
				});
		coverageReader = this::readCoverages;
	}

	private Map<Path, List<Coverage>> readCoverages() {
		try {
			return Files.walk(tmpFolder).filter(Files::isRegularFile).collect(Collectors.toMap(file -> file, file -> {
				try {
					return Files.readAllLines(file, StandardCharsets.UTF_8).stream().map(lineToCoverageTransformer)
							.toList();
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
			}));
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	/**
	 * Empties the temporary folder
	 */
	@Override
	public void afterEach(ExtensionContext context) throws Exception {
		Files.walk(tmpFolder).filter(Files::isRegularFile).forEach(t -> {
			try {
				Files.delete(t);
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		});
	}

	/**
	 * Deletes the temporary folder
	 */
	@Override
	public void afterAll(ExtensionContext context) throws Exception {
		Files.delete(tmpFolder);
	}

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
			throws ParameterResolutionException {
		Class<?> clazz = parameterContext.getParameter().getType();
		return Path.class.isAssignableFrom(clazz) || CoverageReader.class.isAssignableFrom(clazz);
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
			throws ParameterResolutionException {
		Class<?> clazz = parameterContext.getParameter().getType();
		if (Path.class.isAssignableFrom(clazz)) {
			return tmpFolder;
		}
		if (CoverageReader.class.isAssignableFrom(clazz)) {
			return coverageReader;
		}
		throw new ParameterResolutionException("only Path or CoverageReader expected");
	}

	/**
	 * Coverage reader
	 * 
	 * @author steffen
	 *
	 */
	@FunctionalInterface
	static interface CoverageReader {

		/**
		 * Reads all test coverages file separated
		 * 
		 * @return coverages file separated
		 */
		Map<Path, List<Coverage>> readToMap();

		/**
		 * Reads all test coverages
		 * 
		 * @return coverages
		 */
		default List<Coverage> readToList() {
			return readToMap().values().stream().flatMap(List::stream).toList();
		}

	}

}
