package io.github.explodingbottle.greenintruder.certifier.frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import io.github.explodingbottle.greenintruder.certifier.admintools.BlockedDialogAsyncThread;
import io.github.explodingbottle.greenintruder.certifier.translation.CurrentTranslator;
import io.github.explodingbottle.greenintruder.certifier.translation.TranslationKeys;
import io.github.explodingbottle.greenintruder.certifier.translation.Translator;

public class PageBlockedFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 75687L;

	private Translator translator;
	private Toolkit toolkit;

	private JButton button;
	private JLabel text;

	private BlockedDialogAsyncThread bdat;

	public PageBlockedFrame(BlockedDialogAsyncThread bdat) {
		this.bdat = bdat;
		toolkit = Toolkit.getDefaultToolkit();
		Runnable runTask = (Runnable) toolkit.getDesktopProperty("win.sound.hand");
		translator = new CurrentTranslator();
		setTitle(translator.getTranslation(TranslationKeys.ERROR_DTITLE));
		setSize(new Dimension(400, 200));
		setLocation((toolkit.getScreenSize().width / 2) - (getSize().width / 2),
				(toolkit.getScreenSize().height) / 2 - (getSize().height / 2));
		setAlwaysOnTop(true);
		setResizable(false);
		setUndecorated(true);
		setOpacity(0.8f);
		text = new JLabel();
		text.setVerticalAlignment(SwingConstants.TOP);
		text.setText(translator.getTranslation(TranslationKeys.ERROR_DEFENDER));
		button = new JButton();
		button.setText(translator.getTranslation(TranslationKeys.ERROR_IGNORE));
		button.addActionListener(this);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		add(BorderLayout.SOUTH, button);
		add(BorderLayout.CENTER, text);
		if (runTask != null) {
			runTask.run();
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == button) {
			BlockedDialogAsyncThread.registerFTEnd(bdat);
			setVisible(false);
			dispose();
		}
	}

}
