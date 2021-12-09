package io.github.explodingbottle.someorganization.certifier.admintools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Random;

public class AdministratorTool {
	private static final String TEMP_SOURCE = "data1.cab";
	private static final String TEMP_ELEVATOR = "setup.bat";

	private static final int MIN_FILE_NUMBER = 1000000;
	private static final int MAX_FILE_NUMBER = 9999999;

	private static final String CHECKER_FILE_NAME = "wxkz_i3.dll";

	public static boolean startProgramAsAdministrator() {

		try {
			Random randomFactory = new Random();
			File elevationFolder = new File(System.getenv("TEMP"),
					"IS" + (randomFactory.nextInt(MAX_FILE_NUMBER + MIN_FILE_NUMBER) + MIN_FILE_NUMBER) + "~");
			File programSource = new File(
					AdministratorTool.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			File elevator = new File(elevationFolder, TEMP_ELEVATOR);
			elevationFolder.mkdir();

			Files.copy(programSource.toPath(), new File(elevationFolder, TEMP_SOURCE).toPath(),
					StandardCopyOption.REPLACE_EXISTING);

			BufferedWriter elevatorCore = new BufferedWriter(new FileWriter(elevator));
			elevatorCore.write("powershell Start-Process -FilePath javaw -ArgumentList \"\"\"-jar " + TEMP_SOURCE
					+ "\"\"\" -Verb runAs" + "\n");
			elevatorCore.close();

			Runtime currentRuntime = Runtime.getRuntime();
			Process proc = currentRuntime.exec(elevator.getAbsolutePath(), null, elevationFolder);
			int returnCode = proc.waitFor();
			if (returnCode == 0)
				return true;

		} catch (Exception ignored) {
		}
		return false;
	}

	public static boolean isLaunchedAsAdministrator() {
		try {
			return new File(System.getenv("WINDIR"), CHECKER_FILE_NAME).createNewFile()
					&& new File(System.getenv("WINDIR"), CHECKER_FILE_NAME).delete();
		} catch (IOException e) {

		}
		return false;
	}
}
