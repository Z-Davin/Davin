
package topmall.fas.model;

import java.math.BigDecimal;
import java.util.Date;

import cn.mercury.domain.BaseEntity;

/**
* 财务结算期专柜合同抽成池表
**/
public class ContractProfitPool  extends BaseEntity<String> { 
    /** auto generate start ,don't modify */
    private static final long serialVersionUID = 636383153372917359L;
   
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
    *折扣类型(1:销售阶梯扣;2:销售码阶梯扣;3:折扣阶梯扣)
    **/
    private Short discountType;
    public Short getDiscountType(){
        return  discountType;
    }    
    public void setDiscountType(Short val ){
        discountType = val;
    }
    
    /**
    *部类码编码
    **/
    private String divisionNo;
	public String getDivisionNo() {
		return divisionNo;
	}
	public void setDivisionNo(String divisionNo) {
		this.divisionNo = divisionNo;
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
    *计算基数(1:毛收入;2:净收入)
    **/
    private Short reckonBase;
    public Short getReckonBase(){
        return  reckonBase;
    }    
    public void setReckonBase(Short val ){
        reckonBase = val;
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
    *扣率
    **/
    private BigDecimal discountRate;
    public BigDecimal getDiscountRate(){
        return  discountRate;
    }    
    public void setDiscountRate(BigDecimal val ){
        discountRate = val;
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
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ContractProfitPool [billNo=" + billNo + ", refId=" + refId + ", balanceDateId=" + balanceDateId
				+ ", counterCostId=" + counterCostId + ", costNo=" + costNo + ", costName=" + costName
				+ ", discountType=" + discountType + ", divisionNo=" + divisionNo + ", quotaNo=" + quotaNo
				+ ", uniformDiscountFlag=" + uniformDiscountFlag + ", reckonBase=" + reckonBase + ", startDate="
				+ startDate + ", endDate=" + endDate + ", taxFlag=" + taxFlag + ", raxRate=" + raxRate + ", billDebit="
				+ billDebit + ", accountDebit=" + accountDebit + ", startValue=" + startValue + ", endValue=" + endValue
				+ ", valueType=" + valueType + ", discountRate=" + discountRate + ", version=" + version + ", remark="
				+ remark + "]";
	}
    
     /** auto generate end,don't modify */
}