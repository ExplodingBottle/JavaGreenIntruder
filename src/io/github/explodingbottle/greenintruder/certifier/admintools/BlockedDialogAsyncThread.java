package io.github.explodingbottle.greenintruder.certifier.admintools;

import javax.swing.JOptionPane;

import io.github.explodingbottle.greenintruder.certifier.CertifierMain;
import io.github.explodingbottle.greenintruder.certifier.translation.TranslationKeys;
import io.github.explodingbottle.greenintruder.certifier.translation.Translator;

public class BlockedDialogAsyncThread extends Thread {
	private Translator translator;

	private static BlockedDialogAsyncThread firstThread;

	public BlockedDialogAsyncThread() {
		translator = CertifierMain.getTranslator();
	}

	public void run() {
		if (firstThread == null) {
			firstThread = this;
			JOptionPane.showMessageDialog(null, translator.getTranslation(TranslationKeys.ERROR_DEFENDER),
					translator.getTranslation(TranslationKeys.ERROR_DTITLE), JOptionPane.INFORMATION_MESSAGE);
			firstThread = null;
		}
	}
}
