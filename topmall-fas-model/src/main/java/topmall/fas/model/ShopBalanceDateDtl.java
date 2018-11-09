
package topmall.fas.model;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import cn.mercury.domain.BaseEntity;
import cn.mercury.utils.JsonDateDeserializer$10;
import cn.mercury.utils.JsonDateSerializer$10;

/**
 * 结算日期设置明细
 * dai.xw
 */
public class ShopBalanceDateDtl extends BaseEntity<String> {
	/** auto generate start ,don't modify */
	private static final long serialVersionUID = 636391930584661308L;

	/**
	*大区编码
	**/
	private String zoneNo;

	public String getZoneNo() {
		return zoneNo;
	}

	public void setZoneNo(String val) {
		zoneNo = val;
	}

	/**
	*结算公司编码
	**/
	private String companyNo;

	public String getCompanyNo() {
		return companyNo;
	}

	public void setCompanyNo(String val) {
		companyNo = val;
	}

	/**
	*门店编号
	**/
	private String shopNo;

	public String getShopNo() {
		return shopNo;
	}

	public void setShopNo(String val) {
		shopNo = val;
	}

	/**
	*专柜编码
	**/
	private String counterNo;

	public String getCounterNo() {
		return counterNo;
	}

	public void setCounterNo(String val) {
		counterNo = val;
	}

	/**
	*结算月
	**/
	private String settleMonth;

	public String getSettleMonth() {
		return settleMonth;
	}

	public void setSettleMonth(String val) {
		settleMonth = val;
	}

	/**
	*结算开始日期
	**/
	@JsonSerialize(using = JsonDateSerializer$10.class)
	@JsonDeserialize(using = JsonDateDeserializer$10.class)
	private Date settleStartDate;

	public Date getSettleStartDate() {
		return settleStartDate;
	}

	public void setSettleStartDate(Date val) {
		settleStartDate = val;
	}

	/**
	*结算结束日期
	**/
	@JsonSerialize(using = JsonDateSerializer$10.class)
	@JsonDeserialize(using = JsonDateDeserializer$10.class)
	private Date settleEndDate;

	public Date getSettleEndDate() {
		return settleEndDate;
	}

	public void setSettleEndDate(Date val) {
		settleEndDate = val;
	}

	/**
	*
	**/
	private String remark;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String val) {
		remark = val;
	}
	
	private Integer action;

	public Integer getAction() {
		return action;
	}

	public void setAction(Integer action) {
		this.action = action;
	}
	
	
	@JsonSerialize(using = JsonDateSerializer$10.class)
	@JsonDeserialize(using = JsonDateDeserializer$10.class)
	private Date splitDate;
	public Date getSplitDate() {
		return splitDate;
	}

	public void setSplitDate(Date splitDate) {
		this.splitDate = splitDate;
	}

	private Integer status;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer val) {
		this.status = val;
	}

	/**
	 * 供应商编码
	 */
	public String supplierNo;

	public String getSupplierNo() {
		return supplierNo;
	}

	public void setSupplierNo(String supplierNo) {
		this.supplierNo = supplierNo;
	}

	@Override
	public String toString() {
		return "ShopBalanceDateDtl [" + "id=" + getId() + ",zoneNo=" + zoneNo + ",companyNo=" + companyNo + ",shopNo="
				+ shopNo + ",counterNo=" + counterNo + ",settleMonth=" + settleMonth + ",settleStartDate="
				+ settleStartDate + ",settleEndDate=" + settleEndDate + ",remark=" + remark + "]";
	}

	public void copyProperties(CounterSaleCost counterSaleCost) {
		counterSaleCost.setCounterNo(this.counterNo);
		counterSaleCost.setShopNo(this.shopNo);
		counterSaleCost.setSettleMonth(this.settleMonth);
		counterSaleCost.setSettleEndDate(this.settleEndDate);
		counterSaleCost.setSettleStartDate(this.settleStartDate);
	}

	public void copyProperties(CounterSaleCostDtl counterSaleCostDtl) {
		counterSaleCostDtl.setCounterNo(this.counterNo);
		counterSaleCostDtl.setShopNo(this.shopNo);
		counterSaleCostDtl.setSettleMonth(this.settleMonth);
		counterSaleCostDtl.setSettleEndDate(this.settleEndDate);
		counterSaleCostDtl.setSettleStartDate(this.settleStartDate);
	}

	public void copyProperties(CounterCost counterCost) {
		counterCost.setCounterNo(this.counterNo);
		counterCost.setShopNo(this.shopNo);
		counterCost.setSettleMonth(this.settleMonth);
		counterCost.setSettleEndDate(this.settleEndDate);
		counterCost.setSettleStartDate(this.settleStartDate);
	}

}