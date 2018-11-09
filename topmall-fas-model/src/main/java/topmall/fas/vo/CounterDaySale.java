package topmall.fas.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import cn.mercury.utils.BigDecimalSerializer$2;
import cn.mercury.utils.JsonDateDeserializer$10;
import cn.mercury.utils.JsonDateSerializer$10;
import topmall.fas.model.CounterSaleCost;
import topmall.fas.model.CounterSaleCostDtl;

/**
 * 用于计算销售费用的对象
 * 
 * @author zengxa
 * 
 */
@SuppressWarnings("rawtypes")
public class CounterDaySale extends cn.mercury.domain.BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6373597622740294232L;

	/**
	 * 专柜编码
	 */
	private String counterNo;

	public String getCounterNo() {
		return counterNo;
	}

	public void setCounterNo(String counterNo) {
		this.counterNo = counterNo;
	}

	/**
	 * 部类编码
	 */
	private String divisionNo;

	public String getDivisionNo() {
		return divisionNo;
	}

	public void setDivisionNo(String divisionNo) {
		this.divisionNo = divisionNo;
	}

	/**
	 * 活动编码
	 */
	private String proNo;

	public String getProNo() {
		return proNo;
	}

	public void setProNo(String proNo) {
		this.proNo = proNo;
	}

	/**
	 * 商品扣率
	 */
	private BigDecimal discount;

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	/**
	 * 扣率(营促销)
	 */
	private BigDecimal rateValue;

	public BigDecimal getRateValue() {
		return rateValue==null?new BigDecimal(0):rateValue;
	}

	public void setRateValue(BigDecimal rateValue) {
		this.rateValue = rateValue;
	}

	/**
	 * 销售日期
	 */
	@JsonSerialize(using = JsonDateSerializer$10.class)
	@JsonDeserialize(using = JsonDateDeserializer$10.class)
	private Date saleDate;

	public Date getSaleDate() {
		return saleDate;
	}

	public void setSaleDate(Date saleDate) {
		this.saleDate = saleDate;
	}

	/**
	 * 分摊率
	 */
	private BigDecimal absorptionRate;

	public BigDecimal getAbsorptionRate() {
		return absorptionRate;
	}

	public void setAbsorptionRate(BigDecimal absorptionRate) {
		this.absorptionRate = absorptionRate;
	}

	/**
	 * 结算价
	 */
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal settleAmount;

	public BigDecimal getSettleAmount() {
		return settleAmount;
	}

	public void setSettleAmount(BigDecimal settleAmount) {
		this.settleAmount = settleAmount;
	}

	/**
	 * 牌价
	 */
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal tagPriceAmount;

	public BigDecimal getTagPriceAmount() {
		return tagPriceAmount;
	}

	public void setTagPriceAmount(BigDecimal tagPriceAmount) {
		this.tagPriceAmount = tagPriceAmount;
	}
	
	/**
     *净收入总金额
     **/
     private BigDecimal netIncomeAmount;
    
     public BigDecimal getNetIncomeAmount() {
    	return netIncomeAmount;
	 }
	
	 public void setNetIncomeAmount(BigDecimal netIncomeAmount) {
		this.netIncomeAmount = netIncomeAmount;
	 }

	/**
	 * 票扣标识
	 **/
	private Integer billDebit;

	public Integer getBillDebit() {
		return billDebit;
	}

	public void setBillDebit(Integer val) {
		billDebit = val;
	}

	/**
	 * 账扣标识
	 **/
	private Integer accountDebit;

	public Integer getAccountDebit() {
		return accountDebit;
	}

	public void setAccountDebit(Integer val) {
		accountDebit = val;
	}

	/**
	 * 税率
	 **/
	private BigDecimal raxRate;

	public BigDecimal getRaxRate() {
		return raxRate;
	}

	public void setRaxRate(BigDecimal val) {
		raxRate = val;
	}
	/**
	 * 0-正常，1-重算抵充，2-重算新的日结'
	 */
	private Integer type;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 *  备注
	 */
	private String remark;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	/**
	 * 销售码类型（正价/特价/促销价）0:正价，1：特价
	 */
	private String priceType;

	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}
	
	/**
	 * 销售数量
	 */
	private  Integer saleQty;

	public Integer getSaleQty() {
		return saleQty;
	}

	public void setSaleQty(Integer val) {
		this.saleQty = val;
	}
	
	/**
	 * 积分低现金额
	 */
	private BigDecimal pointsAmount;
	
	public BigDecimal getPointsAmount() {
		return pointsAmount;
	}

	public void setPointsAmount(BigDecimal pointsAmount) {
		this.pointsAmount = pointsAmount;
	}
	
	/**
	 * 计入保底的金额
	 */
	private BigDecimal minimumAmount;

	public BigDecimal getMinimumAmount() {
		return minimumAmount;
	}

	public void setMinimumAmount(BigDecimal minimumAmount) {
		this.minimumAmount = minimumAmount;
	}
	
	/**
	 * 未计入保底金额
	 */
	private BigDecimal notMinimumAmount;

	public BigDecimal getNotMinimumAmount() {
		return notMinimumAmount;
	}

	public void setNotMinimumAmount(BigDecimal notMinimumAmount) {
		this.notMinimumAmount = notMinimumAmount;
	}

	public void copyProperties(CounterSaleCostDtl counterSaleCostDtl){
		counterSaleCostDtl.setSaleDate(this.saleDate);
		counterSaleCostDtl.setCounterNo(this.counterNo);
		counterSaleCostDtl.setDivisionNo(this.divisionNo);
		counterSaleCostDtl.setBillDebit(this.billDebit);
		counterSaleCostDtl.setAccountDebit(this.accountDebit);
		counterSaleCostDtl.setRaxRate(this.raxRate);
		counterSaleCostDtl.setRateValue(this.rateValue);
	}
	
	public void copyProperties(CounterSaleCost counterSaleCost){
		counterSaleCost.setCounterNo(this.counterNo);
		counterSaleCost.setDivisionNo(this.divisionNo);
		counterSaleCost.setBillDebit(this.billDebit);
		counterSaleCost.setAccountDebit(this.accountDebit);
		counterSaleCost.setRaxRate(this.raxRate);
		counterSaleCost.setRateValue(this.rateValue);
	}
	
	public String getSaleCostKey(){
		StringBuilder buffer=new StringBuilder();
		return buffer.append(this.divisionNo).append(this.rateValue).append(this.raxRate).append(this.billDebit).append(this.accountDebit)
				.append(this.type).toString();
	}
	
	public String getSaleCostDtlKey(){
		return getSaleCostKey()+this.saleDate;
	}

	public String getMallSaleCostKey(){
		StringBuilder buffer=new StringBuilder();
		return buffer.append(this.priceType).append(this.rateValue).append(this.raxRate).append(this.billDebit)
				.append(this.accountDebit).append(this.type).toString();
		
	}
	
	

}
