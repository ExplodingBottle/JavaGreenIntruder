package io.github.explodingbottle.someorganization.certifier.translation;

import java.io.InputStream;
import java.util.Properties;

public class Translator {
	private Properties loadedProperties;
	private static final String FALLBACK_LANGUAGE_PATH = "texts/en.lang";

	public Translator(String language) {
		loadedProperties = new Properties();

		try {
			InputStream languageFile = getClass().getResourceAsStream("/" + language);
			if (languageFile != null)
				getClass().getResourceAsStream("/" + FALLBACK_LANGUAGE_PATH);
			loadedProperties.load(languageFile);
			languageFile.close();
		} catch (Exception ignored) {

		}

	}

	public String getTranslation(TranslationKeys key) {
		if (loadedProperties.containsKey(key.getTranslationKey()))
			return loadedProperties.getProperty(key.getTranslationKey());
		return key.getTranslationKey();
	}
}
