
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
public class CounterSaleCost extends BaseEntity<String> {
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
		if (null == divisionNo) {
			return "";
		} else {
			return divisionNo;
		}
	}

	public void setDivisionNo(String val) {
		divisionNo = val;
		if (null != val) {
			if (val.endsWith("00")) {
				setDivisionName("正价");
			} else if (val.endsWith("01")) {
				setDivisionName("特价");
			} else {
				setDivisionName("促销");
			}
		}
	}

	/**
	 * 部类性质
	 */
	private String divisionName;

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public String getDivisionName() {
		return divisionName;
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
		return saleQty==null?0:saleQty;
	}

	public void setSaleQty(Integer saleQty) {
		this.saleQty = saleQty;
	}

	private String companyNo;

	public String getCompanyNo() {
		return companyNo;
	}

	public void setCompanyNo(String companyNo) {
		this.companyNo = companyNo;
	}

	/**
	 * 供应商
	 */
	private String supplierNo;

	public String getSupplierNo() {
		return supplierNo;
	}

	public void setSupplierNo(String supplierNo) {
		this.supplierNo = supplierNo;
	}

	/**
	 * 专柜名称
	 */
	private String counterName;

	public String getCounterName() {
		return counterName;
	}

	public void setCounterName(String counterName) {
		this.counterName = counterName;
	}

	/**
	 * 卖场名称
	 */
	private String shopName;

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	@Override
	public String toString() {
		return "BillCounterSaleBalance [" + "id=" + getId() + ",seqId=" + seqId + ",counterNo=" + counterNo
				+ ",divisionNo=" + divisionNo + ",discount=" + discount + ",accountDebit=" + accountDebit
				+ ",billDebit=" + billDebit + ",raxRate=" + raxRate + ",settleSum=" + settleSum + ",discountAmount="
				+ discountAmount + ",absorptionAmount=" + absorptionAmount + ",sellingCost=" + sellingCost
				+ ",profitAmount=" + profitAmount + ",shopNo=" + shopNo + ",settleMonth=" + settleMonth
				+ ",settleStartDate=" + settleStartDate + ",settleEndDate=" + settleEndDate + ",balanceBillNo="
				+ balanceBillNo + "]";
	}

}