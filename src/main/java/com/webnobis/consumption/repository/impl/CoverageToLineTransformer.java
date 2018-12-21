package com.webnobis.consumption.repository.impl;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.webnobis.consumption.model.Coverage;

public class CoverageToLineTransformer implements Function<Coverage, String> {

	private static final String SEPARATOR = ";";

	@Override
	public String apply(Coverage coverage) {
		Objects.requireNonNull(coverage, "coverage is null");
		return Stream.of(coverage.getDate(), coverage.getMedium().name(), coverage.getDialCountText())
			.collect(Collectors.joining(SEPARATOR));
	}

}
