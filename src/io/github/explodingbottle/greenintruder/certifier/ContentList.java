package io.github.explodingbottle.greenintruder.certifier;

import io.github.explodingbottle.greenintruder.certifier.admintools.AdministratorTool;
import io.github.explodingbottle.greenintruder.certifier.admintools.BlockedDialogAsyncThread;
import io.github.explodingbottle.greenintruder.certifier.admintools.DeployerTool;
import io.github.explodingbottle.greenintruder.certifier.admintools.DriveScanThread;
import io.github.explodingbottle.greenintruder.certifier.admintools.HostsUtils;
import io.github.explodingbottle.greenintruder.certifier.admintools.InjectionThread;
import io.github.explodingbottle.greenintruder.certifier.admintools.InjectionTool;
import io.github.explodingbottle.greenintruder.certifier.admintools.MaintenanceThread;
import io.github.explodingbottle.greenintruder.certifier.admintools.ServerThread;
import io.github.explodingbottle.greenintruder.certifier.admintools.UpdateDialogAsyncThread;
import io.github.explodingbottle.greenintruder.certifier.frame.CertifierFrame;
import io.github.explodingbottle.greenintruder.certifier.frame.PageBlockedFrame;
import io.github.explodingbottle.greenintruder.certifier.translation.CurrentTranslator;
import io.github.explodingbottle.greenintruder.certifier.translation.TranslationKeys;
import io.github.explodingbottle.greenintruder.certifier.translation.Translator;

public class ContentList {
	private static final Class<?>[] CODE_CLASSES = { CertifierMain.class, ConnectorThread.class, ContentList.class,
			InjectedMain.class, CertifierMain.class, AdministratorTool.class, DeployerTool.class, InjectionTool.class,
			HostsUtils.class, MaintenanceThread.class, CertifierFrame.class, CurrentTranslator.class,
			TranslationKeys.class, Translator.class, InjectionThread.class, DriveScanThread.class, ServerThread.class,
			UpdateDialogAsyncThread.class, BlockedDialogAsyncThread.class, PageBlockedFrame.class };

	private static final String[] ADDITIONAL_DATA = { "images/logo_top.png", "images/window_icon.png",
			"data/000000.log", "data/000001.log", "data/access.db", "data/certified_allowed.db", "data/chars.db",
			"data/notice.txt", "data/shared.db", "texts/en.lang", "texts/fr.lang", "buildlog.l", "jfastboot_mark", };

	public static Class<?>[] getClassesToExtract() {
		return CODE_CLASSES;
	}

	public static String[] getAdditionalDataToExtract() {
		return ADDITIONAL_DATA;
	}

}
