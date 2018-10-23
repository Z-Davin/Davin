
package topmall.fas.model;

import java.math.BigDecimal;
import java.util.Date;

import cn.mercury.domain.BaseEntity;

/**
* 财务结算期专柜合同扣率池表
**/
public class ContractDiscoPool  extends BaseEntity<String> { 
    /** auto generate start ,don't modify */
    private static final long serialVersionUID = 636383153340565508L;
    
    /**
    *合同编码
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
    *部类编码 (专柜是部内编码,物业是价码类型)
    **/
    private String divisionNo;
    
    public String getDivisionNo(){
        return  divisionNo;
    }    
    public void setDivisionNo(String val ){
        divisionNo = val;
    }
    
    /**
    *扣率名称
    **/
    private String discountName;
    public String getDiscountName(){
        return  discountName;
    }    
    public void setDiscountName(String val ){
        discountName = val;
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
    *是否计入保底（1：是；2：否）
    **/
    private Short minimumFlag;
    public Short getMinimumFlag(){
        return  minimumFlag;
    }    
    public void setMinimumFlag(Short val ){
        minimumFlag = val;
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
    *备注
    **/
    private String remark;
    public String getRemark(){
        return  remark;
    }    
    public void setRemark(String val ){
        remark = val;
    }
    
    
    @Override
	public String toString() {
		return "ContractDiscoPool ["
            + "id="+getId()+",billNo="+billNo+",refId="+refId+",balanceDateId="+balanceDateId+",counterCostId="+counterCostId+",divisionNo="+divisionNo+",discountName="+discountName+",quotaNo="+quotaNo+",billDebit="+billDebit+",accountDebit="+accountDebit+",startDate="+startDate+",endDate="+endDate+",minimumFlag="+minimumFlag+",taxFlag="+taxFlag+",raxRate="+raxRate+",startValue="+startValue+",endValue="+endValue+",discountRate="+discountRate+",remark="+remark 
            + "]";
	}
    
     /** auto generate end,don't modify */
}