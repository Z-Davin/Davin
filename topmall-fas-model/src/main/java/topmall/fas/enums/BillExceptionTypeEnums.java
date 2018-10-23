package topmall.fas.enums;

/**
 * 异常类型枚举
 * @author dai.xw
 *
 */
public enum BillExceptionTypeEnums {
	STATUSEXCEPTION(1000, "单据状态不对"),
	DATAEXCEPTION(1001, "数据库异常"),
	APIEXCEPTION(1002, "接口异常"), 
	SYSTEMEXCEPTION(1003, "系统异常"), 
	INVENTORYEXCEPTION(1004,"库存异常");

	private Integer status;
	private String text;

	private BillExceptionTypeEnums(Integer status, String text) {
		this.status = status;
		this.text = text;
	}

	public Integer getStatus() {
		return this.status;
	}

	public String getText() {
		return this.text;
	}

	public static String getTextByStatus(Integer status) {
		BillExceptionTypeEnums[] statusArr = values();
		for (BillExceptionTypeEnums billStatusEnums : statusArr) {
			if (billStatusEnums.getStatus().equals(status)) {
				return billStatusEnums.getText();
			}
		}
		return null;
	}

}
