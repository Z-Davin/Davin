package topmall.fas.enums;

public enum PaidWayEnums {
	SELF_CASHIER(1,"自收银"),
	Mall_CASHIER(2,"物业收银");
	
	private Integer value;
	private String text;
	

	private PaidWayEnums(Integer value, String text) {
		this.value = value;
		this.text = text;
	}


	public Integer getValue() {
		return value;
	}


	public void setValue(Integer value) {
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public static String getTextByStatus(Integer status) {
		PaidWayEnums[] payWay = values();
		for (PaidWayEnums pay : payWay) {
			if (pay.getValue().equals(status)) {
				return pay.getText();
			}
		}
		return null;
	}

}
