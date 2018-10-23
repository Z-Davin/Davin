
package topmall.fas.model;

import java.math.BigDecimal;
import java.util.Date;

import cn.mercury.domain.BaseEntity;

/**
* 财务结算期专柜合同其他池表
**/
public class ContractOtherPool  extends BaseEntity<String> { 
    /** auto generate start ,don't modify */
    private static final long serialVersionUID = 636383153364576882L;
    
    /**
    *单据编码
    **/
    private String billNo;
    public String getBillNo(){
        return  billNo;
    }    
    public void setBillNo(String val ){
        billNo = val;
    }
    
    /**
    *条款来源ID
    **/
    private String refId;
    public String getRefId(){
        return  refId;
    }    
    public void setRefId(String val ){
        refId = val;
    }
    
    /**
    *结算期id
    **/
    private String balanceDateId;
    public String getBalanceDateId(){
        return  balanceDateId;
    }    
    public void setBalanceDateId(String val ){
        balanceDateId = val;
    }
    
    /**
    *专柜费用id
    **/
    private String counterCostId;
    public String getCounterCostId(){
        return  counterCostId;
    }    
    public void setCounterCostId(String val ){
        counterCostId = val;
    }
    
    /**
    *扣费项目编码
    **/
    private String costNo;
    public String getCostNo(){
        return  costNo;
    }    
    public void setCostNo(String val ){
        costNo = val;
    }
    
    /**
    *扣费项目名称
    **/
    private String costName;
    public String getCostName(){
        return  costName;
    }    
    public void setCostName(String val ){
        costName = val;
    }
    
    /**
    *计费模式: 1-金额模式, 2-费率模式, 3-单价
    **/
    private Short chargingType;
    public Short getChargingType(){
        return  chargingType;
    }    
    public void setChargingType(Short val ){
        chargingType = val;
    }
    
    /**
    *金额
    **/
    private BigDecimal amount;
    public BigDecimal getAmount(){
        return  amount;
    }    
    public void setAmount(BigDecimal val ){
        amount = val;
    }
    
    /**
    *计费基数：1-毛收入,2-净收入,3-支付方式销售,4-会员卡折扣,5-团购折扣
    **/
    private Short chargingBase;
    public Short getChargingBase(){
        return  chargingBase;
    }    
    public void setChargingBase(Short val ){
        chargingBase = val;
    }
    
    /**
     *支付方式代号
     **/
     private String payNo;
     public String getPayNo(){
         return  payNo;
     }    
     public void setPayNo(String val ){
         payNo = val;
     }
    
    /**
    *数量(计算模式=单价时输入)
    **/
    private Integer qty;
    public Integer getQty(){
        return  qty;
    }    
    public void setQty(Integer val ){
        qty = val;
    }
    
    /**
    *单价
    **/
    private BigDecimal unitPrice;
    public BigDecimal getUnitPrice(){
        return  unitPrice;
    }    
    public void setUnitPrice(BigDecimal val ){
        unitPrice = val;
    }
    
    /**
    *额度阶梯管理编码
    **/
    private String quotaNo;
    public String getQuotaNo(){
        return  quotaNo;
    }    
    public void setQuotaNo(String val ){
        quotaNo = val;
    }
    
    /**
    *其他费用计算单位(例如：水费用吨,电费用度)
    **/
    private String unitName;
    public String getUnitName(){
        return  unitName;
    }    
    public void setUnitName(String val ){
        unitName = val;
    }
    
    /**
     *是否统一折扣(0:否;1:是)
     **/
     private Short uniformDiscountFlag;
     public Short getUniformDiscountFlag(){
         return  uniformDiscountFlag;
     }    
     public void setUniformDiscountFlag(Short val ){
         uniformDiscountFlag = val;
     }
    
    /**
    *票扣标识
    **/
    private Integer billDebit;
    public Integer getBillDebit(){
        return  billDebit;
    }    
    public void setBillDebit(Integer val ){
        billDebit = val;
    }
    
    /**
    *账扣标识
    **/
    private Integer accountDebit;
    public Integer getAccountDebit(){
        return  accountDebit;
    }    
    public void setAccountDebit(Integer val ){
        accountDebit = val;
    }
    
    /**
    *保证金
    **/
    private BigDecimal cashDeposit;
    public BigDecimal getCashDeposit(){
        return  cashDeposit;
    }    
    public void setCashDeposit(BigDecimal val ){
        cashDeposit = val;
    }
    
    /**
    *是否含税(0:不含税;1:含税)
    **/
    private Short taxFlag;
    public Short getTaxFlag(){
        return  taxFlag;
    }    
    public void setTaxFlag(Short val ){
        taxFlag = val;
    }
    
    /**
    *税率
    **/
    private BigDecimal raxRate;
    public BigDecimal getRaxRate(){
        return  raxRate;
    }    
    public void setRaxRate(BigDecimal val ){
        raxRate = val;
    }
    
    /**
    *生效开始日期
    **/
    private Date startDate;
    public Date getStartDate(){
        return  startDate;
    }    
    public void setStartDate(Date val ){
        startDate = val;
    }
    
    /**
    *生效结束日期
    **/
    private Date endDate;
    public Date getEndDate(){
        return  endDate;
    }    
    public void setEndDate(Date val ){
        endDate = val;
    }
    
    /**
    *起始值
    **/
    private BigDecimal startValue;
    public BigDecimal getStartValue(){
        return  startValue;
    }    
    public void setStartValue(BigDecimal val ){
        startValue = val;
    }
    
    /**
    *结束值
    **/
    private BigDecimal endValue;
    public BigDecimal getEndValue(){
        return  endValue;
    }    
    public void setEndValue(BigDecimal val ){
        endValue = val;
    }
    
    /**
     * 1：费率（%） 2：固定额
     */
    private Integer valueType;
    public Integer getValueType() {
		return valueType;
	}
	public void setValueType(Integer valueType) {
		this.valueType = valueType;
	}
    
    /**
    *费率
    **/
    private BigDecimal expensesRate;
    public BigDecimal getExpensesRate(){
        return  expensesRate;
    }    
    public void setExpensesRate(BigDecimal val ){
        expensesRate = val;
    }
    
    /**
    *版本号
    **/
    private String version;
    public String getVersion(){
        return  version;
    }    
    public void setVersion(String val ){
        version = val;
    }
    
    /**
    *备注
    **/
    private String remark;
    public String getRemark(){
        return  remark;
    }    
    public void setRemark(String val ){
        remark = val;
    }
    /** auto generate end,don't modify */
    
    
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ContractOtherPool [billNo=").append(billNo).append(", refId=").append(refId)
				.append(", balanceDateId=").append(balanceDateId).append(", counterCostId=").append(counterCostId)
				.append(", costNo=").append(costNo).append(", costName=").append(costName).append(", chargingType=")
				.append(chargingType).append(", amount=").append(amount).append(", chargingBase=").append(chargingBase)
				.append(", payNo=").append(payNo).append(", qty=").append(qty).append(", unitPrice=").append(unitPrice)
				.append(", quotaNo=").append(quotaNo).append(", unitName=").append(unitName)
				.append(", uniformDiscountFlag=").append(uniformDiscountFlag).append(", billDebit=").append(billDebit)
				.append(", accountDebit=").append(accountDebit).append(", cashDeposit=").append(cashDeposit)
				.append(", taxFlag=").append(taxFlag).append(", raxRate=").append(raxRate).append(", startDate=")
				.append(startDate).append(", endDate=").append(endDate).append(", startValue=").append(startValue)
				.append(", endValue=").append(endValue).append(", expensesRate=").append(expensesRate)
				.append(", version=").append(version).append(", remark=").append(remark).append(", getId()=")
				.append(getId()).append(", getCreateUser()=").append(getCreateUser()).append(", getUpdateUser()=")
				.append(getUpdateUser()).append(", getCreateTime()=").append(getCreateTime())
				.append(", getUpdateTime()=").append(getUpdateTime()).append("]");
		return builder.toString();
	}
}