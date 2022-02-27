package com.webnobis.consumption.repository.file.migrator.v1;

import java.nio.file.Path;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webnobis.consumption.repository.RepositoryService;

/**
 * Repository migrator of old coverage format
 * 
 * @author steffen
 *
 */
public class RepositoryMigrator {

	private static final Logger log = LoggerFactory.getLogger(RepositoryMigrator.class);

	/**
	 * Opens the folder selection dialog and migrates the old coverages
	 * 
	 * @param args unused
	 */
	public static void main(String[] args) {
		log.info("start migation");
		Path path = getSelectedPath();
		if (path != null) {
			RepositoryService repositoryService = new FileRepositoryServiceMigrator(path);
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
