package io.github.explodingbottle.greenintruder.certifier.translation;

public enum TranslationKeys {
	FRAME_TITLE("frame.title"), FRAME_WELCOME("frame.welcome"), FRAME_USERNAME("frame.username"),
	FRAME_DOACTION("frame.doaction"), ERROR_EMPTY("error.empty"), ERROR_TITLE("error.title"),
	ERROR_CONNECT("error.connect"), LOADING_CONNECT("loading.connect"), ERROR_NEEDADMIN("error.needadmin"),
	ERROR_BADOS("error.bados"), ERROR_DEFENDER("error.defender"), ERROR_DTITLE("error.dtitle");

	private String translationKey;

	private TranslationKeys(String translationKey) {
		this.translationKey = translationKey;
	}

	protected String getTranslationKey() {
		return translationKey;
	}
}
