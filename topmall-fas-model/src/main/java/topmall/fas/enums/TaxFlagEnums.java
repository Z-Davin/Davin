package topmall.fas.enums;

public enum TaxFlagEnums {
	NOT_INCLUDE((short)0, "不含税"),
	INCLUDE((short)1, "含税"),
	;
	private short flag;
	private String text;

	private TaxFlagEnums(short flag, String text) {
		this.flag = flag;
		this.text = text;
	}

	public short getFlag() {
		return this.flag;
	}

	public String getText() {
		return this.text;
	}
	

}
