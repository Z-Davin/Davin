
package topmall.fas.model;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import cn.mercury.utils.BigDecimalSerializer$2;
import cn.mercury.utils.JsonDateDeserializer$10;
import cn.mercury.utils.JsonDateDeserializer$19;
import cn.mercury.utils.JsonDateSerializer$10;
import cn.mercury.utils.JsonDateSerializer$19;
import topmall.fas.enums.TaxFlagEnums;

/**
* 专柜费用表
**/
public class CounterCost  extends cn.mercury.domain.BaseEntity<String> { 
    /** auto generate start ,don't modify */
    private static final long serialVersionUID = 636374421227828057L;
    
    /**
     * 系统生成费用的合同条款id
     **/
     private String refId;
     public String getRefId(){
         return  refId;
     }    
     public void setRefId(String val ){
         refId = val;
     }
     
     /**
      * 来源类型："计费用" : 0,
				"租金" : 1,
				"抽成" : 2,
				"保底" : 3,
				"其他" : 4,
				"计成本" : 5
      */
     private Short refType;
	 public Short getRefType() {
		 return refType;
	 }
	 public void setRefType(Short refType) {
		 this.refType = refType;
	 }

	/**
    * 结算公司编码
    **/
    private String companyNo;
    public String getCompanyNo(){
        return  companyNo;
    }    
    public void setCompanyNo(String val ){
        companyNo = val;
    }
    
    /**
    *结算公司名称
    **/
    private String companyName;
    public String getCompanyName(){
        return  companyName;
    }    
    public void setCompanyName(String val ){
        companyName = val;
    }
    
    /**
    *供应商编码
    **/
    private String supplierNo;
    public String getSupplierNo(){
        return  supplierNo;
    }    
    public void setSupplierNo(String val ){
        supplierNo = val;
    }
    
    /**
    *供应商名称
    **/
    private String supplierName;
    public String getSupplierName(){
        return  supplierName;
    }    
    public void setSupplierName(String val ){
        supplierName = val;
    }
    
    /**
    *销售门店编号
    **/
    private String shopNo;
    public String getShopNo(){
        return  shopNo;
    }    
    public void setShopNo(String val ){
        shopNo = val;
    }
    
    /**
    *销售门店名称
    **/
    private String shopName;
    public String getShopName(){
        return  shopName;
    }    
    public void setShopName(String val ){
        shopName = val;
    }
    
    /**
    *专柜编码
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
    *结算月
    **/
    private String settleMonth;
    public String getSettleMonth(){
        return  settleMonth;
    }    
    public void setSettleMonth(String val ){
        settleMonth = val;
    }
    
    /**
    *结算开始时间
    **/
    @JsonSerialize(using = JsonDateSerializer$10.class)
	@JsonDeserialize(using = JsonDateDeserializer$10.class)
    private Date settleStartDate;
    public Date getSettleStartDate(){
        return  settleStartDate;
    }    
    public void setSettleStartDate(Date val ){
        settleStartDate = val;
    }
    
    /**
    *结算结束时间
    **/
	@JsonSerialize(using = JsonDateSerializer$10.class)
	@JsonDeserialize(using = JsonDateDeserializer$10.class)
    private Date settleEndDate;
    public Date getSettleEndDate(){
        return  settleEndDate;
    }    
    public void setSettleEndDate(Date val ){
        settleEndDate = val;
    }
    
    /**
     * 实际结算月
     **/
     private String actualMonth;
     public String getActualMonth(){
         return  actualMonth;
     }    
     public void setActualMonth(String val ){
    	 actualMonth = val;
     }
     
     /**
     * 实际结算开始时间
     **/
     @JsonSerialize(using = JsonDateSerializer$10.class)
 	 @JsonDeserialize(using = JsonDateDeserializer$10.class)
     private Date actualStartDate;
     public Date getActualStartDate(){
         return  actualStartDate;
     }    
     public void setActualStartDate(Date val ){
    	 actualStartDate = val;
     }
     
     /**
     * 实际结算结束时间
     **/
 	@JsonSerialize(using = JsonDateSerializer$10.class)
 	@JsonDeserialize(using = JsonDateDeserializer$10.class)
     private Date actualEndDate;
     public Date getActualEndDate(){
         return  actualEndDate;
     }    
     public void setActualEndDate(Date val ){
    	 actualEndDate = val;
     }
    
    /**
    *状态 0制单
    **/
    private Integer status;
    public Integer getStatus(){
        return  status;
    }    
    public void setStatus(Integer val ){
        status = val;
    }
    
    /**
    *应结价款
    **/
    @JsonSerialize(using = BigDecimalSerializer$2.class)
    private BigDecimal ableAmount;
    public BigDecimal getAbleAmount(){
        return  ableAmount;
    }    
    public void setAbleAmount(BigDecimal val ){
    	ableAmount = val;
    }
    
    /**
    *应结总额
    **/
    @JsonSerialize(using = BigDecimalSerializer$2.class)
    private BigDecimal ableSum;
    public BigDecimal getAbleSum(){
        return  ableSum;
    }    
    public void setAbleSum(BigDecimal val ){
    	ableSum = val;
    }
    
    /**
    *税额
    **/
    @JsonSerialize(using = BigDecimalSerializer$2.class)
    private BigDecimal taxAmount;
    public BigDecimal getTaxAmount(){
        return  taxAmount;
    }    
    public void setTaxAmount(BigDecimal val ){
        taxAmount = val;
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
    private BigDecimal taxRate;
    public BigDecimal getTaxRate(){
        return  taxRate;
    }    
    public void setTaxRate(BigDecimal val ){
        taxRate = val;
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
    *确认人姓名
    **/
    private String auditor;
    public String getAuditor(){
        return  auditor;
    }    
    public void setAuditor(String val ){
        auditor = val;
    }
    
    /**
    *确认时间
    **/
    @JsonSerialize(using = JsonDateSerializer$19.class)
	@JsonDeserialize(using = JsonDateDeserializer$19.class)
    private Date auditTime;
    public Date getAuditTime(){
        return  auditTime;
    }    
    public void setAuditTime(Date val ){
        auditTime = val;
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
    *生成方式
    **/
    private Short source;
    public Short getSource(){
        return  source;
    }    
    public void setSource(Short val ){
        source = val;
    }
    
    private String balanceBillNo;
    
    
    public String getBalanceBillNo() {
		return balanceBillNo;
	}
	public void setBalanceBillNo(String val) {
		this.balanceBillNo = val;
	}

	private String seqId;
    
    public String getSeqId() {
		return seqId;
	}
	public void setSeqId(String val) {
		this.seqId = val;
	}
	
	/**
	 * 费用金额： 如果是含税的返回的就是 应结总额；如果是不含税的返回的就是 应结价款
	 */
	private BigDecimal costAmount;
	 
	/**
	 * @return the costAmount
	 */
	public BigDecimal getCostAmount() {
		if (taxFlag != null && TaxFlagEnums.INCLUDE.getFlag() == taxFlag && ableSum != null){
			return ableSum;
		} else if (taxFlag != null && TaxFlagEnums.NOT_INCLUDE.getFlag() == taxFlag && ableAmount != null){
			return ableAmount;
		} else {
			return costAmount;
		}
	}
	
	/**
	 * @param costAmount the costAmount to set
	 */
	public void setCostAmount(BigDecimal costAmount) {
		this.costAmount = costAmount;
	}
	
	/**
	 * 获取真实输入的金额
	 */
	public BigDecimal getInputCostAmount() {
		return costAmount;
	}
	
	
	@Override
	public String toString() {
		return "CounterCost ["
            + "id="+getId()+",companyNo="+companyNo+",companyName="+companyName+",supplierNo="+supplierNo+",supplierName="+supplierName+",shopNo="+shopNo+",shopName="+shopName+",counterNo="+counterNo+",counterName="+counterName+",costNo="+costNo+",costName="+costName+",settleMonth="+settleMonth+",settleStartDate="+settleStartDate+",settleEndDate="+settleEndDate+",status="+status+",ableAmount="+ableAmount+",ableSum="+ableSum+",taxAmount="+taxAmount+",taxFlag="+taxFlag+",taxRate="+taxRate+",billDebit="+billDebit+",accountDebit="+accountDebit+",auditor="+auditor+",auditTime="+auditTime+",remark="+remark+",source="+source 
            +",seqId="+seqId+",balanceBillNo="+balanceBillNo+ "]";
	}
    
}