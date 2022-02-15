package com.webnobis.consumption.repository.file.migrator.v1;

import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webnobis.consumption.model.Coverage;
import com.webnobis.consumption.repository.file.FileRepositoryService;

/**
 * Old filename with line to coverage transformer
 * 
 * @author steffen
 *
 */
public class FileNameWithLineToCoverageTransformer implements Function<String, Coverage> {

	private static final Pattern patternMedium = Pattern.compile("^([A-Z]+)" + FileRepositoryService.FILE_EXT + "$");

	private static final Pattern patternDateDialCount = Pattern.compile("^([0-9\\.]{10});([0-9\\.]+),([0-9]+).+$");

	private static final Logger log = LoggerFactory.getLogger(FileNameWithLineToCoverageTransformer.class);

	private final String medium;

	/**
	 * Filename depending transformer
	 * 
	 * @param fileName filename
	 */
	public FileNameWithLineToCoverageTransformer(String fileName) {
		Matcher m = patternMedium.matcher(Objects.requireNonNull(fileName, "fileName is null"));
		if (m.find()) {
			medium = m.group(1);
		} else {
			medium = null;
			log.info("fileName not matched: " + fileName);
		}
	}

	@Override
	public Coverage apply(String line) {
		Matcher m = patternDateDialCount.matcher(Objects.requireNonNull(line, "line is null"));
		if (m.find()) {
			String dialCount = m.group(2) + '.' + m.group(3);
			return new Coverage(m.group(1), medium, dialCount);
		} else if (log.isDebugEnabled()) {
			log.debug("line not matched: " + line);
		}
		return null;
	}

}
