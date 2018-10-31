package topmall.fas.dto;

import java.math.BigDecimal;
import cn.mercury.domain.BaseEntity;
import cn.mercury.utils.BigDecimalSerializer$2;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 专柜结算单-销售明细打印对象
 **/
public class CounterSaleCostPrint extends BaseEntity<String> {

	/** auto generate start ,don't modify */
	private static final long serialVersionUID = 636385674082801358L;

	/**
	 * 批次号
	 **/
	private String balanceBillNo;

	public String getBalanceBillNo() {
		return balanceBillNo;
	}

	public void setBalanceBillNo(String val) {
		balanceBillNo = val;
	}

	/**
	 * 部类编码
	 **/
	private String divisionNo;

	public String getDivisionNo() {
		return divisionNo;
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
	 * 销售总额
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
	 * 分摊金额
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
	 * 销售成本 //销售成本: 销售总额-销售提成
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
	 * 销售提成
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
	 * 结算月
	 **/
	private String settleMonth;

	public String getSettleMonth() {
		return settleMonth;
	}

	public void setSettleMonth(String val) {
		settleMonth = val;
	}

	/**
	 * 折扣
	 */
	private BigDecimal discount;

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
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

}