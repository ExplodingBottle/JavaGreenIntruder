package io.github.explodingbottle.someorganization.certifier.frame;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import io.github.explodingbottle.someorganization.certifier.CertifierMain;
import io.github.explodingbottle.someorganization.certifier.ConnectorThread;
import io.github.explodingbottle.someorganization.certifier.translation.TranslationKeys;

public class CertifierFrame extends JFrame implements ActionListener, WindowListener {

	private static final long serialVersionUID = 28039674897023302L;

	private static final int WINDOW_WIDTH = 600;
	private static final int WINDOW_HEIGHT = 400;

	private SpringLayout frameLayout;
	private JTextField usernameField;
	private JButton doAction;
	private JProgressBar progressIndicator;
	private Thread connectorThread;

	public CertifierFrame() {

		setTitle(CertifierMain.getTranslator().getTranslation(TranslationKeys.FRAME_TITLE));
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setLocationRelativeTo(null);
		setResizable(false);

		frameLayout = new SpringLayout();
		setLayout(frameLayout);

		setIconImage(new ImageIcon(getClass().getResource("/images/window_icon.png")).getImage());
		JLabel logoTop = new JLabel(new ImageIcon(getClass().getResource("/images/logo_top.png")));

		JLabel welcomeText = new JLabel(CertifierMain.getTranslator().getTranslation(TranslationKeys.FRAME_WELCOME));
		doAction = new JButton(CertifierMain.getTranslator().getTranslation(TranslationKeys.FRAME_DOACTION));
		JLabel usernameText = new JLabel(CertifierMain.getTranslator().getTranslation(TranslationKeys.FRAME_USERNAME));

		progressIndicator = new JProgressBar();
		progressIndicator.setStringPainted(true);
		progressIndicator.setIndeterminate(true);
		progressIndicator.setPreferredSize(new Dimension(200, 20));

		progressIndicator.setVisible(false);

		usernameField = new JTextField();
		usernameField.setColumns(20);
		Font textFont = new Font("Verdana", Font.BOLD, 14);
		welcomeText.setFont(textFont);
		usernameText.setFont(textFont);
		doAction.setFont(textFont);

		doAction.addActionListener(this);

		add(logoTop);
		add(welcomeText);
		add(doAction);
		add(usernameText);
		add(usernameField);
		add(progressIndicator);

		frameLayout.putConstraint(SpringLayout.WEST, logoTop, 10, SpringLayout.WEST, this);
		frameLayout.putConstraint(SpringLayout.NORTH, logoTop, 0, SpringLayout.NORTH, this);

		frameLayout.putConstraint(SpringLayout.WEST, welcomeText, 240, SpringLayout.WEST, this);
		frameLayout.putConstraint(SpringLayout.NORTH, welcomeText, 10, SpringLayout.NORTH, this);

		frameLayout.putConstraint(SpringLayout.WEST, doAction, 250, SpringLayout.WEST, this);
		frameLayout.putConstraint(SpringLayout.NORTH, doAction, 310, SpringLayout.NORTH, this);

		frameLayout.putConstraint(SpringLayout.WEST, usernameText, 70, SpringLayout.WEST, this);
		frameLayout.putConstraint(SpringLayout.NORTH, usernameText, 100, SpringLayout.NORTH, this);

		frameLayout.putConstraint(SpringLayout.WEST, usernameField, 240, SpringLayout.WEST, this);
		frameLayout.putConstraint(SpringLayout.NORTH, usernameField, 100, SpringLayout.NORTH, this);

		frameLayout.putConstraint(SpringLayout.WEST, progressIndicator, 30, SpringLayout.WEST, this);
		frameLayout.putConstraint(SpringLayout.NORTH, progressIndicator, 313, SpringLayout.NORTH, this);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		addWindowListener(this);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (usernameField.getText().length() > 0) {
			doAction.setEnabled(false);
			usernameField.setEnabled(false);
			progressIndicator.setVisible(true);
			progressIndicator.setString(CertifierMain.getTranslator().getTranslation(TranslationKeys.LOADING_CONNECT));
			connectorThread = new ConnectorThread(this);
			connectorThread.start();
		} else {
			JOptionPane.showMessageDialog(this,
					CertifierMain.getTranslator().getTranslation(TranslationKeys.ERROR_EMPTY),
					CertifierMain.getTranslator().getTranslation(TranslationKeys.ERROR_TITLE),
					JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void windowOpened(WindowEvent e) {

	}

	@Override
	public void windowClosing(WindowEvent e) {
		if (connectorThread != null)
			connectorThread.interrupt();
	}

	@Override
	public void windowClosed(WindowEvent e) {

	}

	@Override
	public void windowIconified(WindowEvent e) {

	}

	@Override
	public void windowDeiconified(WindowEvent e) {

	}

	@Override
	public void windowActivated(WindowEvent e) {

	}

	@Override
	public void windowDeactivated(WindowEvent e) {

	}
}
