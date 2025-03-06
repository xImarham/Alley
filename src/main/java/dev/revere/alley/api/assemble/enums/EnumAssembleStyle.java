package dev.revere.alley.api.assemble.enums;

import lombok.Getter;

@Getter
public enum EnumAssembleStyle {
	KOHI(true, 15),
	VIPER(true, -1),
	MODERN(false, 1),
	CUSTOM(false, 0)

	;

	private boolean descending;
	private int startNumber;

	/**
	 * Assemble Style.
	 *
	 * @param descending  whether the positions are going down or up.
	 * @param startNumber from where to loop from.
	 */
	EnumAssembleStyle(boolean descending, int startNumber) {
		this.descending = descending;
		this.startNumber = startNumber;
	}

	public EnumAssembleStyle reverse() {
		return descending(!this.descending);
	}

	public EnumAssembleStyle descending(boolean descending) {
		this.descending = descending;
		return this;
	}

	public EnumAssembleStyle startNumber(int startNumber) {
		this.startNumber = startNumber;
		return this;
	}
}