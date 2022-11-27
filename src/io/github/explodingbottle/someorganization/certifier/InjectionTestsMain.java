package io.github.explodingbottle.someorganization.certifier;

import java.io.File;

import javax.swing.JOptionPane;

import io.github.explodingbottle.someorganization.certifier.admintools.InjectionTool;

public class InjectionTestsMain {

	public static void main(String[] args) {
		// new File("C:\\LOL").delete();
		// InjectionThread it = new InjectionThread();
		// it.start();
		JOptionPane.showMessageDialog(null, InjectionTool.injectInJar(new File("C:\\LOL\\lol.jar")));
	}

}
