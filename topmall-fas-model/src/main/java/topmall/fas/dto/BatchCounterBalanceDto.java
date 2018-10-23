package topmall.fas.dto;

import java.io.Serializable;


public class BatchCounterBalanceDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -407999031610634717L;
	/**
	 * 结算月
	 */
	private String settleMonth;
	/**
	 * 合作性质
	 */
	private Integer businessType;
	
	/**
	 * 专柜
	 */
	private String counterNo;
	
	/**
	 * 供应商
	 */
	private String supplierNo;
	/**
	 * 门店
	 */
	private String shopNo;
	/**
	 * 公司
	 */
	private String companyNo;
	
	public String getSettleMonth() {
		return settleMonth;
	}
	public void setSettleMonth(String settleMonth) {
		this.settleMonth = settleMonth;
	}
	public Integer getBusinessType() {
		return businessType;
	}
	public void setBusinessType(Integer businessType) {
		this.businessType = businessType;
	}
	public String getCounterNo() {
		return counterNo;
	}
	public void setCounterNo(String counterNo) {
		this.counterNo = counterNo;
	}
	public String getSupplierNo() {
		return supplierNo;
	}
	public void setSupplierNo(String supplierNo) {
		this.supplierNo = supplierNo;
	}
	public String getShopNo() {
		return shopNo;
	}
	public void setShopNo(String shopNo) {
		this.shopNo = shopNo;
	}
	public String getCompanyNo() {
		return companyNo;
	}
	public void setCompanyNo(String companyNo) {
		this.companyNo = companyNo;
	}
	
	

}
