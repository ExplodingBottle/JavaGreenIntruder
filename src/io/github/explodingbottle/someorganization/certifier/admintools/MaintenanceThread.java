package io.github.explodingbottle.someorganization.certifier.admintools;

import java.util.HashMap;
import java.util.Map;

public class MaintenanceThread extends Thread {

	private static final int CHECK_TIME = 2000;

	private Map<String, String> toBlock;

	public MaintenanceThread() {
		toBlock = new HashMap<>();

		toBlock.put("lolorganizationlol.com", "172.16.0.0");
		toBlock.put("lolorganizationlol54.com", "172.16.0.0");
	}

	public void run() {
		while (!interrupted()) {
			if (!DeployerTool.isScheduledTaskRegistred())
				DeployerTool.registerAsScheduledTask();

			toBlock.forEach((host, ip) -> {
				if (!HostsUtils.isRuleRegistred(host, ip))
					HostsUtils.registerRule(host, ip);

			});
			try {
				Thread.sleep(CHECK_TIME);
			} catch (InterruptedException ignored) {
			}
		}
	}
}
