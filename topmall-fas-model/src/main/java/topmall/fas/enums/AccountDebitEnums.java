package topmall.fas.enums;

public enum AccountDebitEnums {
	ACCOUNT(1,"账扣"),
	CASH(2,"现金");
	
	private Integer value;
	private String text;
	
	private AccountDebitEnums(Integer value, String text) {
		this.value = value;
		this.text = text;
	}

	public Integer getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}
	
	public static String getTextByStatus(Integer status) {
		AccountDebitEnums[] accountDebitS = values();
		for (AccountDebitEnums accountDebit : accountDebitS) {
			if (accountDebit.getValue().equals(status)) {
				return accountDebit.getText();
			}
		}
		return null;
	}



}
