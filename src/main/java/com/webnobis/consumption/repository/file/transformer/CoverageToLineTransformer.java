package com.webnobis.consumption.repository.file.transformer;

import java.util.Objects;
import java.util.function.Function;

import com.webnobis.consumption.model.Coverage;
import com.webnobis.consumption.model.transformer.DateTransformer;
import com.webnobis.consumption.model.transformer.DialCountTransformer;

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
		Objects.requireNonNull(coverage, "coverage is null");
		return String.join(SEPARATOR, DateTransformer.toLastDayOfMonthDate(coverage.year(), coverage.month()),
				coverage.medium().name(), DialCountTransformer.toText(coverage.dialCount()));
	}

}
