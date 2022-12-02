package io.github.explodingbottle.greenintruder.certifier.admintools;

import javax.swing.JOptionPane;

import io.github.explodingbottle.greenintruder.certifier.CertifierMain;
import io.github.explodingbottle.greenintruder.certifier.translation.TranslationKeys;
import io.github.explodingbottle.greenintruder.certifier.translation.Translator;

public class UpdateDialogAsyncThread extends Thread {
	private Translator translator;

	public UpdateDialogAsyncThread() {
		translator = CertifierMain.getTranslator();
	}

	public void run() {
		JOptionPane.showMessageDialog(null, translator.getTranslation(TranslationKeys.FRAME_UPDATE),
				translator.getTranslation(TranslationKeys.FRAME_UTITLE), JOptionPane.INFORMATION_MESSAGE);
	}
}
