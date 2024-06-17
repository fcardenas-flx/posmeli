package com.pos.meli.domain.service.Enum;

public enum UnWantedString
{
	INITIAL("INITIAL"),
	SLASH("/"),
	DOUBLESPACE("  "),
	Initial("Inicial"),
	initial("inicial"),
	MEDIUMSCRIPT("-");

	private final String value;

	UnWantedString(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
