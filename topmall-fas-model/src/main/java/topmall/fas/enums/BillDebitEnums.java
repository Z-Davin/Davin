package topmall.fas.enums;

public enum BillDebitEnums {
	NO(0, "非票扣"),
	YES(1, "票扣");
	
	private Integer value;
	private String text;

	private BillDebitEnums(Integer value, String text) {
		this.value = value;
		this.text = text;
	}

	public Integer getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}

}
