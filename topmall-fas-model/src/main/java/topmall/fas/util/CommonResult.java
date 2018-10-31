package topmall.fas.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CommonResult implements Serializable {

	
	private static final long serialVersionUID = -1412219318465678951L;

	private String errorCode;
	private String errorMessage;
	private String errorDefined;//明确的错误提示
	private String errorDetail;//明确的堆栈信息
	private Object data;

	public static CommonResult getSucessResult() {
		return CommonResult.sucess(null);
	}

	public static CommonResult sucess(Object data) {
		return new CommonResult("0000", "操作成功", data);
	}

	public static CommonResult error(String errorCode, String errorMessage) {
		return new CommonResult(errorCode, errorMessage);
	}

	public CommonResult(String errorCode, String errorMessage) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public CommonResult(String errorCode, String errorMessage, Object data) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.data = data;
	}
	
	public CommonResult() {
		super();
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Object getData() {
		return data;
	}

	public CommonResult setData(Object data) {
		this.data = data;
		return this;
	}

	@SuppressWarnings("unchecked")
	public CommonResult addMapData(String key, Object value) {
		if (data != null && !(data instanceof Map)) {
			throw new RuntimeException("已有非map的值");
		}
		if (data == null) {
			data = new HashMap<String, Object>();
		}
		Map<String, Object> map = (Map<String, Object>) data;
		map.put(key, value);
		return this;
	}

	public String getErrorDefined() {
		return errorDefined;
	}

	public void setErrorDefined(String errorDefined) {
		this.errorDefined = errorDefined;
	}

	public String getErrorDetail() {
		return errorDetail;
	}

	public void setErrorDetail(String errorDetail) {
		this.errorDetail = errorDetail;
	}
}
