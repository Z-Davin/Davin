
package topmall.fas.model;

import java.math.BigDecimal;
import java.util.Date;

import cn.mercury.domain.BaseEntity;

/**
* 财务结算期专柜合同保底池表
**/
public class ContractGuaraPool  extends BaseEntity<String> { 
    /** auto generate start ,don't modify */
    private static final long serialVersionUID = 636383153355966389L;
     
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
    * 保底基数类型(1:净收入;2:毛收入;3:利润保底;4:以其他专柜销售保底)
    **/
    private Short baseNumber;
    public Short getBaseNumber(){
        return  baseNumber;
    }    
    public void setBaseNumber(Short val ){
        baseNumber = val;
    }
    
    /**
    *数量
    **/
    private Integer number;
    public Integer getNumber(){
        return  number;
    }    
    public void setNumber(Integer val ){
        number = val;
    }
    
    /**
    *专柜编号
    **/
    private String counterNo;
    public String getCounterNo(){
        return  counterNo;
    }    
    public void setCounterNo(String val ){
        counterNo = val;
    }
    
    /**
    *专柜名称
    **/
    private String counterName;
    public String getCounterName(){
        return  counterName;
    }    
    public void setCounterName(String val ){
        counterName = val;
    }
    
    /**
    *保底周期(1:月保底;2:季度保底;3:半年保底;4:年保底)
    **/
    private Short baseCycle;
    public Short getBaseCycle(){
        return  baseCycle;
    }    
    public void setBaseCycle(Short val ){
        baseCycle = val;
    }
    
    /**
    *保底算法(1:每月核算,月末通算;2:月末核算)
    **/
    private Short baseAlgorithm;
    public Short getBaseAlgorithm(){
        return  baseAlgorithm;
    }    
    public void setBaseAlgorithm(Short val ){
        baseAlgorithm = val;
    }
    
    /**
    *保底金额
    **/
    private BigDecimal baseAmount;
    public BigDecimal getBaseAmount(){
        return  baseAmount;
    }    
    public void setBaseAmount(BigDecimal val ){
        baseAmount = val;
    }
    
    /**
    *保底扣率
    **/
    private BigDecimal baseDiscount;
    public BigDecimal getBaseDiscount(){
        return  baseDiscount;
    }    
    public void setBaseDiscount(BigDecimal val ){
        baseDiscount = val;
    }
    
    /**
    *扣率
    **/
    private BigDecimal discount;
    public BigDecimal getDiscount(){
        return  discount;
    }    
    public void setDiscount(BigDecimal val ){
        discount = val;
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
    * 保底计算基数（1:毛收入;2:保底金额-保底基数;3:保底金额;4:净收入;5:保底金额-利润;6:终端收入-保底;7：实收-保底金额）
    **/
    private Short baseCalculateNum;
    public Short getBaseCalculateNum(){
        return  baseCalculateNum;
    }    
    public void setBaseCalculateNum(Short val ){
        baseCalculateNum = val;
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
		return "ContractGuaraPool ["
            + "id="+getId()+",billNo="+billNo+",refId="+refId+",balanceDateId="+balanceDateId+",counterCostId="+counterCostId+",costNo="+costNo+",costName="+costName+",divisionNo="+divisionNo+",baseNumber="+baseNumber+",number="+number+",counterNo="+counterNo+",counterName="+counterName+",baseCycle="+baseCycle+",baseAlgorithm="+baseAlgorithm+",baseAmount="+baseAmount+",baseDiscount="+baseDiscount+",discount="+discount+",taxFlag="+taxFlag+",raxRate="+raxRate+",startDate="+startDate+",endDate="+endDate+",billDebit="+billDebit+",accountDebit="+accountDebit+",baseCalculateNum="+baseCalculateNum+",remark="+remark 
            + "]";
	}
    
     /** auto generate end,don't modify */
}