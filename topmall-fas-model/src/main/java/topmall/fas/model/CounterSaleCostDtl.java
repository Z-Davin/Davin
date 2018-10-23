
package topmall.fas.model;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import cn.mercury.domain.BaseEntity;
import cn.mercury.utils.BigDecimalSerializer$2;
import cn.mercury.utils.JsonDateDeserializer$10;
import cn.mercury.utils.JsonDateSerializer$10;

/**
* 专柜结算单-销售明细
**/
public class CounterSaleCostDtl extends BaseEntity<String> {
	/** auto generate start ,don't modify */
	private static final long serialVersionUID = 636385674082801358L;

	/**
	*批次号
	**/
	private String seqId;

	public String getSeqId() {
		return seqId;
	}

	public void setSeqId(String val) {
		seqId = val;
	}

	/**
	 *批次号
	 **/
	private String balanceBillNo;

	public String getBalanceBillNo() {
		return balanceBillNo;
	}

	public void setBalanceBillNo(String val) {
		balanceBillNo = val;
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
	*部类编码
	**/
	private String divisionNo;

	public String getDivisionNo() {
		return divisionNo;
	}

	public void setDivisionNo(String val) {
		divisionNo = val;
	}

	/**
	*商品折数,如25.00代表25.00%
	**/
	private BigDecimal discount;

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal val) {
		discount = val;
	}

	/**
	*账扣标识
	**/
	private Integer accountDebit;

	public Integer getAccountDebit() {
		return accountDebit;
	}

	public void setAccountDebit(Integer val) {
		accountDebit = val;
	}

	/**
	*票扣标识
	**/
	private Integer billDebit;

	public Integer getBillDebit() {
		return billDebit;
	}

	public void setBillDebit(Integer val) {
		billDebit = val;
	}

	/**
	*税率
	**/
	private BigDecimal raxRate;

	public BigDecimal getRaxRate() {
		return raxRate;
	}

	public void setRaxRate(BigDecimal val) {
		raxRate = val;
	}

	/**
	*销售总额
	**/
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal settleSum;

	public BigDecimal getSettleSum() {
		return settleSum;
	}

	public void setSettleSum(BigDecimal val) {
		settleSum = val;
	}

	/**
	 *净收入总额
	 **/
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal netIncomeSum;

	public BigDecimal getNetIncomeSum() {
		return netIncomeSum;
	}

	public void setNetIncomeSum(BigDecimal val) {
		netIncomeSum = val;
	}

	/**
	*折扣金额
	**/
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal discountAmount;

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(BigDecimal val) {
		discountAmount = val;
	}

	/**
	*分摊金额
	**/
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal absorptionAmount;

	public BigDecimal getAbsorptionAmount() {
		return absorptionAmount;
	}

	public void setAbsorptionAmount(BigDecimal val) {
		absorptionAmount = val;
	}

	/**
	*销售成本
	**/
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal sellingCost;

	public BigDecimal getSellingCost() {
		return sellingCost;
	}

	public void setSellingCost(BigDecimal val) {
		sellingCost = val;
	}

	/**
	*销售提成
	**/
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal profitAmount;

	public BigDecimal getProfitAmount() {
		return profitAmount;
	}

	public void setProfitAmount(BigDecimal val) {
		profitAmount = val;
	}

	/**
	 *销售门店编号
	 **/
	private String shopNo;

	public String getShopNo() {
		return shopNo;
	}

	public void setShopNo(String val) {
		shopNo = val;
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
	*结算开始时间
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
	*结算结束时间
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
	* 实际结算月
	**/
	private String actualMonth;

	public String getActualMonth() {
		return actualMonth;
	}

	public void setActualMonth(String val) {
		actualMonth = val;
	}

	/**
	* 实际结算开始时间
	**/
	@JsonSerialize(using = JsonDateSerializer$10.class)
	@JsonDeserialize(using = JsonDateDeserializer$10.class)
	private Date actualStartDate;

	public Date getActualStartDate() {
		return actualStartDate;
	}

	public void setActualStartDate(Date val) {
		actualStartDate = val;
	}

	/**
	* 实际结算结束时间
	**/
	@JsonSerialize(using = JsonDateSerializer$10.class)
	@JsonDeserialize(using = JsonDateDeserializer$10.class)
	private Date actualEndDate;

	public Date getActualEndDate() {
		return actualEndDate;
	}

	public void setActualEndDate(Date val) {
		actualEndDate = val;
	}

	/**
	 * 状态
	 */
	private Integer status;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer val) {
		this.status = val;
	}

	/**
	* 扣率
	*/
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal rateValue;

	public BigDecimal getRateValue() {
		return rateValue;
	}

	public void setRateValue(BigDecimal val) {
		this.rateValue = val;
	}

	/**
	 * 销售日期
	 */
	@JsonDeserialize(using = JsonDateDeserializer$10.class)
	private Date saleDate;

	public Date getSaleDate() {
		return saleDate;
	}

	public void setSaleDate(Date val) {
		this.saleDate = val;
	}

	/**
	 * 类型(0:正常,1:重算)
	 */
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 销售数量
	 */
	private Integer saleQty;

	public Integer getSaleQty() {
		return saleQty;
	}

	public void setSaleQty(Integer saleQty) {
		this.saleQty = saleQty;
	}

	/**
	 * 供应商
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
		return "BillCounterSaleBalance [" + "id=" + getId() + ",seqId=" + seqId + ",counterNo=" + counterNo
				+ ",divisionNo=" + divisionNo + ",discount=" + discount + ",accountDebit=" + accountDebit
				+ ",billDebit=" + billDebit + ",raxRate=" + raxRate + ",settleSum=" + settleSum + ",discountAmount="
				+ discountAmount + ",absorptionAmount=" + absorptionAmount + ",sellingCost=" + sellingCost
				+ ",profitAmount=" + profitAmount + ",shopNo=" + shopNo + ",settleMonth=" + settleMonth
				+ ",settleStartDate=" + settleStartDate + ",settleEndDate=" + settleEndDate + ",balanceBillNo="
				+ balanceBillNo + ",type=" + type + "]";
	}

	/** auto generate end,don't modify */
}