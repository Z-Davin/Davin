package topmall.fas.dto;

import java.math.BigDecimal;

/**
 * 
 * @author Administrator
 *
 */
public class BalanceSummary  extends PriceTypeDto {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6317575239475906732L;
	
	/**
	 * 卖场
	 */
	private String shopNo;
	/**
	 * 专柜
	 */
	private String counterNo;
	/**
	 * 供应商
	 */
	private String supplierNo;
	
	 /**
	    *经营类型：1：联营(客户)，2：租赁（供应商）
	 **/
	private Short businessType;
	/**
	 * 合同面积
	 */
	private  BigDecimal areaCounter;
	/**
	 * 销售总额
	 */
	private BigDecimal settleSum;
	
	/**
	 * 保底销售
	 */
	private BigDecimal guaraSaleSum;
	
	/**
	 * 保底利润
	 */
	private BigDecimal guaraProfitSum;
	/**
	 * 礼券
	 */
	private BigDecimal carSum;
	/**
	 * 会员券
	 */
	private BigDecimal vipSum;
	
	
	public String getShopNo() {
		return shopNo;
	}
	public void setShopNo(String shopNo) {
		this.shopNo = shopNo;
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
	public Short getBusinessType() {
		return businessType;
	}
	public void setBusinessType(Short businessType) {
		this.businessType = businessType;
	}
	public BigDecimal getAreaCounter() {
		return areaCounter;
	}
	public void setAreaCounter(BigDecimal areaCounter) {
		this.areaCounter = areaCounter;
	}
	public BigDecimal getSettleSum() {
		return settleSum;
	}
	public void setSettleSum(BigDecimal settleSum) {
		this.settleSum = settleSum;
	}
	public BigDecimal getGuaraSaleSum() {
		return guaraSaleSum;
	}
	public void setGuaraSaleSum(BigDecimal guaraSaleSum) {
		this.guaraSaleSum = guaraSaleSum;
	}
	public BigDecimal getGuaraProfitSum() {
		return guaraProfitSum;
	}
	public void setGuaraProfitSum(BigDecimal guaraProfitSum) {
		this.guaraProfitSum = guaraProfitSum;
	}
	public BigDecimal getCarSum() {
		return carSum;
	}
	public void setCarSum(BigDecimal carSum) {
		this.carSum = carSum;
	}
	public BigDecimal getVipSum() {
		return vipSum;
	}
	public void setVipSum(BigDecimal vipSum) {
		this.vipSum = vipSum;
	}
	
	

}
