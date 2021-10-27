package com.webnobis.consumption.migrator.v1;

import java.io.IOException;
import java.nio.file.Path;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webnobis.consumption.repository.RepositoryService;

public class RepositoryMigrator {

	private static final Logger log = LoggerFactory.getLogger(RepositoryMigrator.class);
	
	public static void main(String args[]) throws IOException {
		log.info("start migation");
		Path path = getSelectedPath();
		if (path != null) {
			RepositoryService repositoryService = new RepositoryServiceImpl(path);
			repositoryService.storeCoverages(repositoryService.findCoverages());
			log.info("migration finish: " + path.toString());
		} else {
			log.warn("migration abort");
		}
	}
	
	private static Path getSelectedPath() {
		JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(null)) {
			return fileChooser.getSelectedFile().toPath();
		} else {
			return null;
		}
	}
	
}
