package topmall.fas.enums;

public enum StatusEnums {
	MAKEBILL(0, "制单"),
	INEFFECTIVE(1,"未生效"),
	EFFECTIVE(2,"生效"),
	CONFIRM(3,"确认"),
	verify(4,"审核"),
	GENERATE_BALANCE(5,"生成结算单"),
	BALANCE(6,"已结算"),
	GENERATE_COST(7,"生成费用"),
	CANCEL(99,"作废"),
	OVER(100,"完结"),
	DISABLE(8,"未启用"),
	ENABLE(9,"启用");
	
	
	private Integer status;
	private String text;

	private StatusEnums(Integer status, String text) {
		this.status = status;
		this.text = text;
	}

	public Integer getStatus() {
		return this.status;
	}

	public String getText() {
		return this.text;
	}

}
