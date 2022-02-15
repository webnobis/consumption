package com.webnobis.consumption.repository.file;

import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webnobis.consumption.model.Coverage;

/**
 * Line to coverage transformer
 * 
 * @author steffen
 *
 */
public class LineToCoverageTransformer implements Function<String, Coverage> {

	private static final Pattern patternDateMediumDialCount = Pattern.compile("^([0-9\\.]{10});([A-Z]+);([0-9\\.]+)$");

	private static final Logger log = LoggerFactory.getLogger(LineToCoverageTransformer.class);

	/**
	 * Creates coverage from line representation of date, medium and dial count
	 * values
	 */
	@Override
	public Coverage apply(String line) {
		Matcher m = patternDateMediumDialCount.matcher(Objects.requireNonNull(line, "line is null"));
		if (m.find()) {
			return new Coverage(m.group(1), m.group(2), m.group(3));
		} else if (log.isDebugEnabled()) {
			log.debug("line {} not matched", line);
		}
		return null;
	}

}
