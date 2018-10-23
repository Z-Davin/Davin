
package topmall.fas.model;

import java.math.BigDecimal;
import java.util.Date;

import cn.mercury.domain.BaseEntity;

/**
* 财务结算期专柜合同租金池表
**/
public class ContractRentPool  extends BaseEntity<String> { 
    /** auto generate start ,don't modify */
    private static final long serialVersionUID = 636383153380747806L;
    
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
    *租金类别(1:按整月计算;2:按天计算)
    **/
    private Short rentType;
    public Short getRentType(){
        return  rentType;
    }    
    public void setRentType(Short val ){
        rentType = val;
    }
    
    /**
    *租金金额
    **/
    private BigDecimal rentCost;
    public BigDecimal getRentCost(){
        return  rentCost;
    }    
    public void setRentCost(BigDecimal val ){
        rentCost = val;
    }
    
    /**
    *不足一个月(1:按整月计算;2:按天计算)
    **/
    private Short lessMonth;
    public Short getLessMonth(){
        return  lessMonth;
    }    
    public void setLessMonth(Short val ){
        lessMonth = val;
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
    *月租天数:0,自然天数;1,28;2,29;3,30;4,31
    **/
    private Short monthDays;
    public Short getMonthDays(){
        return  monthDays;
    }    
    public void setMonthDays(Short val ){
        monthDays = val;
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
		return "ContractRentPool ["
            + "id="+getId()+",billNo="+billNo+",refId="+refId+",balanceDateId="+balanceDateId+",counterCostId="+counterCostId+",costNo="+costNo+",costName="+costName+",rentType="+rentType+",rentCost="+rentCost+",lessMonth="+lessMonth+",taxFlag="+taxFlag+",raxRate="+raxRate+",startDate="+startDate+",endDate="+endDate+",version="+version+",monthDays="+monthDays+",billDebit="+billDebit+",accountDebit="+accountDebit+",remark="+remark 
            + "]";
	}
    
     /** auto generate end,don't modify */
}