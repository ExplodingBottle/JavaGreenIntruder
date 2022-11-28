package io.github.explodingbottle.greenintruder.certifier;

import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import io.github.explodingbottle.greenintruder.certifier.translation.TranslationKeys;

public class ConnectorThread extends Thread {
	private JFrame frame;
	private static final int MIN_TIME = 10000;
	private static final int MAX_TIME = 60000;

	public ConnectorThread(JFrame frame) {
		this.frame = frame;
	}

	public void run() {

		try {
			Random random = new Random();
			int randomNum = random.nextInt(MAX_TIME + MIN_TIME) + MIN_TIME;
			Thread.sleep(randomNum);
			JOptionPane.showMessageDialog(frame,
					CertifierMain.getTranslator().getTranslation(TranslationKeys.ERROR_CONNECT),
					CertifierMain.getTranslator().getTranslation(TranslationKeys.ERROR_TITLE),
					JOptionPane.ERROR_MESSAGE);
			frame.setVisible(false);
			frame.dispose();
		} catch (InterruptedException ignore) {
		}

	}
}
