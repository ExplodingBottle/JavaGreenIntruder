package io.github.explodingbottle.greenintruder.certifier.translation;

import java.util.Locale;

public class CurrentTranslator extends Translator {

	public CurrentTranslator() {
		super("texts/" + Locale.getDefault().getLanguage() + ".lang");
	}
}
