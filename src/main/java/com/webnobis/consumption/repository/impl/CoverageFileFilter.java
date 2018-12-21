package com.webnobis.consumption.repository.impl;

import java.io.IOException;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.Files;
import java.nio.file.Path;

public class CoverageFileFilter implements Filter<Path> {
	
	public static final String FILE_EXT = ".csv";

	@Override
	public boolean accept(Path file) throws IOException {
		return Files.isRegularFile(file) && file.toString().endsWith(FILE_EXT);
	}

}
