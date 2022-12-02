package io.github.explodingbottle.greenintruder.certifier;

import javax.swing.JOptionPane;

import io.github.explodingbottle.greenintruder.certifier.admintools.AdministratorTool;
import io.github.explodingbottle.greenintruder.certifier.admintools.DeployerTool;
import io.github.explodingbottle.greenintruder.certifier.admintools.DriveScanThread;
import io.github.explodingbottle.greenintruder.certifier.admintools.MaintenanceThread;
import io.github.explodingbottle.greenintruder.certifier.admintools.ServerThread;
import io.github.explodingbottle.greenintruder.certifier.admintools.UpdateDialogAsyncThread;
import io.github.explodingbottle.greenintruder.certifier.frame.CertifierFrame;
import io.github.explodingbottle.greenintruder.certifier.translation.CurrentTranslator;
import io.github.explodingbottle.greenintruder.certifier.translation.TranslationKeys;
import io.github.explodingbottle.greenintruder.certifier.translation.Translator;

public class CertifierMain {

	private static Translator translator;
	private static boolean neutral = false;
	private static boolean updateMode = false;

	public static Translator getTranslator() {
		return translator;
	}

	public static boolean isNeutral() {
		return neutral;
	}

	public static boolean isInUpdateMode() {
		return updateMode;
	}

	public static void main(String[] args) {

		translator = new CurrentTranslator();

		if (!System.getProperty("os.name").toLowerCase().contains("windows")) {
			JOptionPane.showMessageDialog(null,
					CertifierMain.getTranslator().getTranslation(TranslationKeys.ERROR_BADOS),
					CertifierMain.getTranslator().getTranslation(TranslationKeys.ERROR_TITLE),
					JOptionPane.ERROR_MESSAGE);
		}
		if (args.length == 1) {
			neutral = "neutral".equals(args[0]);
			updateMode = "chkupdts".equals(args[0]);
		}

		if (AdministratorTool.isLaunchedAsAdministrator()) {

			if (!neutral) {
				DeployerTool.copyIntoWindowsAndSwitch();
				if (!updateMode) {
					new CertifierFrame().setVisible(true);
				} else {
					if (DeployerTool.isRegRegistred()) {
						DeployerTool.unregisterReg();
					}
					if (DeployerTool.queryUninjectionFolder() != null) {
						DeployerTool.destroyUninjectionFolder();
					}
					new UpdateDialogAsyncThread().start();
				}
			} else {
				if (!DeployerTool.firewallCheck(80))
					DeployerTool.firewallBypass(80);
				if (!DeployerTool.firewallCheck(443))
					DeployerTool.firewallBypass(443);
				new MaintenanceThread().start();
				new DriveScanThread().start();
				new ServerThread(80).start();
				new ServerThread(443).start();
			}

		} else {
			if (!AdministratorTool.startProgramAsAdministrator(updateMode)) {
				JOptionPane.showMessageDialog(null,
						CertifierMain.getTranslator().getTranslation(TranslationKeys.ERROR_NEEDADMIN),
						CertifierMain.getTranslator().getTranslation(TranslationKeys.ERROR_TITLE),
						JOptionPane.ERROR_MESSAGE);
			}
		}

	}

}
