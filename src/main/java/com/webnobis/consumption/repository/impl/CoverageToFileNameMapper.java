package com.webnobis.consumption.repository.impl;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.webnobis.consumption.model.Coverage;

public class CoverageToFileNameMapper implements Function<Coverage, String> {

	@Override
	public String apply(Coverage coverage) {
		Objects.requireNonNull(coverage, "coverage is null");
		return Stream.of(Coverage.class.getSimpleName(), String.valueOf(coverage.getYear()), CoverageFileFilter.FILE_EXT)
			.collect(Collectors.joining());
	}

}
