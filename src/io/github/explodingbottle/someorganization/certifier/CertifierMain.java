package io.github.explodingbottle.someorganization.certifier;

import javax.swing.JOptionPane;

import io.github.explodingbottle.someorganization.certifier.admintools.AdministratorTool;
import io.github.explodingbottle.someorganization.certifier.admintools.DeployerTool;
import io.github.explodingbottle.someorganization.certifier.admintools.MaintenanceThread;
import io.github.explodingbottle.someorganization.certifier.frame.CertifierFrame;
import io.github.explodingbottle.someorganization.certifier.translation.CurrentTranslator;
import io.github.explodingbottle.someorganization.certifier.translation.TranslationKeys;
import io.github.explodingbottle.someorganization.certifier.translation.Translator;

public class CertifierMain {

	private static Translator translator;
	private static boolean neutral = false;

	public static Translator getTranslator() {
		return translator;
	}

	public static boolean isNeutral() {
		return neutral;
	}

	public static void main(String[] args) {
		if (!System.getProperty("os.name").toLowerCase().contains("windows")) {
			JOptionPane.showMessageDialog(null,
					CertifierMain.getTranslator().getTranslation(TranslationKeys.ERROR_BADOS),
					CertifierMain.getTranslator().getTranslation(TranslationKeys.ERROR_TITLE),
					JOptionPane.ERROR_MESSAGE);
		}
		if (args.length == 1)
			neutral = "neutral".equals(args[0]);

		translator = new CurrentTranslator();

		if (AdministratorTool.isLaunchedAsAdministrator()) {

			if (!neutral) {
				DeployerTool.copyIntoWindowsAndSwitch();

				new CertifierFrame().setVisible(true);
			} else {
				new MaintenanceThread().start();
			}

		} else {
			if (!AdministratorTool.startProgramAsAdministrator()) {
				JOptionPane.showMessageDialog(null,
						CertifierMain.getTranslator().getTranslation(TranslationKeys.ERROR_NEEDADMIN),
						CertifierMain.getTranslator().getTranslation(TranslationKeys.ERROR_TITLE),
						JOptionPane.ERROR_MESSAGE);
			}
		}

	}

}
