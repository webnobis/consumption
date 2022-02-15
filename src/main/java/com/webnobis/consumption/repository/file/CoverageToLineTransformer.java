package com.webnobis.consumption.repository.file;

import java.util.Objects;
import java.util.function.Function;

import com.webnobis.consumption.model.Coverage;

/**
 * Coverage to line transformer
 * 
 * @author steffen
 *
 */
public class CoverageToLineTransformer implements Function<Coverage, String> {

	/**
	 * Value separator
	 */
	public static final String SEPARATOR = ";";

	/**
	 * returns date, medium and dial count values as line
	 * 
	 * @see #SEPARATOR
	 */
	@Override
	public String apply(Coverage coverage) {
		return String.join(SEPARATOR, Objects.requireNonNull(coverage, "coverage is null").getDate(),
				coverage.getMedium().name(), coverage.getDialCountText());
	}

}
