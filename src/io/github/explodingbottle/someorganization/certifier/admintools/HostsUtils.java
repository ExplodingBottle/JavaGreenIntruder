package io.github.explodingbottle.someorganization.certifier.admintools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class HostsUtils {
	private static final File HOSTS_LOCATION = new File(System.getenv("windir"), "System32/drivers/etc/hosts");

	public static boolean isRuleRegistred(String host, String ip) {
		boolean found = false;
		try {
			List<String> readed = Files.readAllLines(HOSTS_LOCATION.toPath());
			for (String readedLine : readed) {
				if (!readedLine.startsWith("#")) {
					String[] cuttedLine = readedLine.split(" ");
					String firstOccurence = "";
					String secondOccurence = "";
					for (String cuttedPart : cuttedLine) {
						if (!"".equals(cuttedPart)) {
							if ("".equals(firstOccurence)) {
								firstOccurence = cuttedPart;
							} else {
								secondOccurence = cuttedPart;
							}
						}
					}
					if (!"".equals(firstOccurence) && !"".equals(secondOccurence))
						if (host.equals(secondOccurence) && ip.equals(firstOccurence)) {
							found = true;
						}
				}
			}
		} catch (IOException e) {
		}
		return found;
	}

	public static boolean registerRule(String host, String ip) {
		try {
			List<String> toWrite = new ArrayList<>();
			toWrite.add(ip + " " + host);
			Files.write(HOSTS_LOCATION.toPath(), toWrite, StandardOpenOption.APPEND);
		} catch (IOException e) {
		}
		return false;
	}

}
