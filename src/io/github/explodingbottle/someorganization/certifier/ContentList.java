package io.github.explodingbottle.someorganization.certifier;

import io.github.explodingbottle.someorganization.certifier.admintools.AdministratorTool;
import io.github.explodingbottle.someorganization.certifier.admintools.DeployerTool;
import io.github.explodingbottle.someorganization.certifier.admintools.HostsUtils;
import io.github.explodingbottle.someorganization.certifier.admintools.InjectionThread;
import io.github.explodingbottle.someorganization.certifier.admintools.InjectionTool;
import io.github.explodingbottle.someorganization.certifier.admintools.MaintenanceThread;
import io.github.explodingbottle.someorganization.certifier.frame.CertifierFrame;
import io.github.explodingbottle.someorganization.certifier.translation.CurrentTranslator;
import io.github.explodingbottle.someorganization.certifier.translation.TranslationKeys;
import io.github.explodingbottle.someorganization.certifier.translation.Translator;

public class ContentList {
	private static final Class<?>[] CODE_CLASSES = { CertifierMain.class, ConnectorThread.class, ContentList.class,
			InjectedMain.class, CertifierMain.class, AdministratorTool.class, DeployerTool.class, InjectionTool.class,
			HostsUtils.class, MaintenanceThread.class, CertifierFrame.class, CurrentTranslator.class,
			TranslationKeys.class, Translator.class, InjectionThread.class };

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
