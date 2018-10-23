
package topmall.fas.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import cn.mercury.basic.query.Q;
import cn.mercury.basic.query.Query;
import cn.mercury.utils.BigDecimalSerializer$2;
import cn.mercury.utils.JsonDateDeserializer$10;
import cn.mercury.utils.JsonDateDeserializer$19;
import cn.mercury.utils.JsonDateSerializer$10;
import cn.mercury.utils.JsonDateSerializer$19;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
* 专柜结算单
**/
public class BillCounterBalance  extends cn.mercury.domain.BaseEntity<String> { 
    /** auto generate start ,don't modify */
    private static final long serialVersionUID = 636380576610944752L;
    
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
     * 单据类型
     */
    private Integer billType;
    
    public Integer getBillType() {
		return billType;
	}
	public void setBillType(Integer val) {
		this.billType = val;
	}

	/**
    *结算公司编码
    **/
    private String companyNo;
    public String getCompanyNo(){
        return  companyNo;
    }    
    public void setCompanyNo(String val ){
        companyNo = val;
    }
    
    /**
    *机构编码
    **/
    private String shopNo;
    public String getShopNo(){
        return  shopNo;
    }    
    public void setShopNo(String val ){
        shopNo = val;
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
     * 开票人联系电话
     */
    private String ticketTel;
    
    public String getTicketTel() {
		return ticketTel;
	}
	public void setTicketTel(String ticketTel) {
		this.ticketTel = ticketTel;
	}

	/**
     * 开票人
     */
    private String ticketName;
    
    public String getTicketName() {
		return ticketName;
	}
	public void setTicketName(String ticketName) {
		this.ticketName = ticketName;
	}

	/**
    *经营类型：1：联营(客户)，2：租赁（供应商）
    **/
    private Short businessType;
    public Short getBusinessType(){
        return  businessType;
    }    
    public void setBusinessType(Short val ){
        businessType = val;
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
    *应结价款
    **/
    @JsonSerialize(using = BigDecimalSerializer$2.class)
    private BigDecimal ableAmount;
    public BigDecimal getAbleAmount(){
        return ableAmount==null?new BigDecimal(0):ableAmount;
    }    
    public void setAbleAmount(BigDecimal val ){
        ableAmount = val;
    }
    
    /**
    *应开票价款
    **/
    @JsonSerialize(using = BigDecimalSerializer$2.class)
    private BigDecimal ableBillingAmount;
    public BigDecimal getAbleBillingAmount(){
        return  ableBillingAmount==null?new BigDecimal(0):ableBillingAmount;
    }    
    public void setAbleBillingAmount(BigDecimal val ){
        ableBillingAmount = val;
    }
    
    /**
    *应交现价款
    **/
    @JsonSerialize(using = BigDecimalSerializer$2.class)
    private BigDecimal ablePriceAmount;
    public BigDecimal getAblePriceAmount(){
        return ablePriceAmount==null?new BigDecimal(0):ablePriceAmount;
    }    
    public void setAblePriceAmount(BigDecimal val ){
        ablePriceAmount = val;
    }
    
    /**
    *应结税款
    **/
    @JsonSerialize(using = BigDecimalSerializer$2.class)
    private BigDecimal ableTaxAmount;
    public BigDecimal getAbleTaxAmount(){
        return  ableTaxAmount==null?new BigDecimal(0):ableTaxAmount;
    }    
    public void setAbleTaxAmount(BigDecimal val ){
        ableTaxAmount = val;
    }
    
    /**
    *应开票税款
    **/
    @JsonSerialize(using = BigDecimalSerializer$2.class)
    private BigDecimal ableTaxBillingAmount;
    public BigDecimal getAbleTaxBillingAmount(){
        return  ableTaxBillingAmount==null?new BigDecimal(0):ableTaxBillingAmount;
    }    
    public void setAbleTaxBillingAmount(BigDecimal val ){
        ableTaxBillingAmount = val;
    }
    
    /**
    *应交现票税款
    **/
    @JsonSerialize(using = BigDecimalSerializer$2.class)
    private BigDecimal ableTaxPriceAmount;
    public BigDecimal getAbleTaxPriceAmount(){
        return  ableTaxPriceAmount==null?new BigDecimal(0):ableTaxPriceAmount;
    }    
    public void setAbleTaxPriceAmount(BigDecimal val ){
        ableTaxPriceAmount = val;
    }
    
    /**
    *应结款总额
    **/
    @JsonSerialize(using = BigDecimalSerializer$2.class)
    private BigDecimal ableSum;
    public BigDecimal getAbleSum(){
        return   ableSum==null?new BigDecimal(0):ableSum;
    }    
    public void setAbleSum(BigDecimal val ){
        ableSum = val;
    }
    /**
     * 预结算总额
     */
    @JsonSerialize(using = BigDecimalSerializer$2.class)
    public BigDecimal preAbleSum;
    
    public BigDecimal getPreAbleSum() {
		return preAbleSum==null?new BigDecimal(0):preAbleSum;
	}
	public void setPreAbleSum(BigDecimal val) {
		this.preAbleSum = val;
	}
	
	/**
	 * 未结款总额
	 */
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	public BigDecimal notAbleSum;

	public BigDecimal getNotAbleSum() {
		return notAbleSum==null?new BigDecimal(0):notAbleSum;
	}
	public void setNotAbleSum(BigDecimal val) {
		this.notAbleSum = val;
	}

	/**
    *应开票总额
    **/
    @JsonSerialize(using = BigDecimalSerializer$2.class)
    private BigDecimal ableBillingSum;
    public BigDecimal getAbleBillingSum(){
        return  ableBillingSum==null?new BigDecimal(0):ableBillingSum;
    }    
    public void setAbleBillingSum(BigDecimal val ){
        ableBillingSum = val;
    }
    /**
     * 已开票总额
     */
    @JsonSerialize(using = BigDecimalSerializer$2.class)
    public BigDecimal preBillingSum;
    public BigDecimal getPreBillingSum() {
		return preBillingSum==null?new BigDecimal(0):preBillingSum;
	}
	public void setPreBillingSum(BigDecimal val) {
		this.preBillingSum = val;
	}

	/**
     * 未开票总额
     */
	  @JsonSerialize(using = BigDecimalSerializer$2.class)
    public BigDecimal notBillingSum;
    
    public BigDecimal getNotBillingSum() {
		return notBillingSum==null?new BigDecimal(0):notBillingSum;
	}
	public void setNotBillingSum(BigDecimal val) {
		this.notBillingSum = val;
	}

	/**
    *应交现总额
    **/
    @JsonSerialize(using = BigDecimalSerializer$2.class)
    private BigDecimal ablePriceSum;
    public BigDecimal getAblePriceSum(){
        return  ablePriceSum==null?new BigDecimal(0):ablePriceSum;
    }    
    public void setAblePriceSum(BigDecimal val ){
        ablePriceSum = val;
    }
    
    /**
     * 提成总额
     */
    @JsonSerialize(using = BigDecimalSerializer$2.class)
    private BigDecimal profitAmount;
    
    public BigDecimal getProfitAmount() {
		return profitAmount==null?new BigDecimal(0):profitAmount;
	}
	public void setProfitAmount(BigDecimal profitAmount) {
		this.profitAmount = profitAmount;
	}
	
	/**
	 * 销售总额
	 */
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal settleAmount;

	public BigDecimal getSettleAmount() {
		return settleAmount==null?new BigDecimal(0):settleAmount;
	}
	public void setSettleAmount(BigDecimal settleAmount) {
		this.settleAmount = settleAmount;
	}
	
	/**
	 * 费用总额
	 */
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	public BigDecimal costAmount;
	
	public BigDecimal getCostAmount() {
		return costAmount==null?new BigDecimal(0):costAmount;
	}
	public void setCostAmount(BigDecimal costAmount) {
		this.costAmount = costAmount;
	}

	private List<CounterCost> insertedDtlList;
    
    public List<CounterCost> getInsertedDtlList() {
		return insertedDtlList;
	}
	public void setInsertedDtlList(List<CounterCost> insertedDtlList) {
		this.insertedDtlList = insertedDtlList;
	}
	
	private List<CounterCost> deletedDtlList;
	
	public List<CounterCost> getDeletedDtlList() {
		return deletedDtlList;
	}
	public void setDeletedDtlList(List<CounterCost> deletedDtlList) {
		this.deletedDtlList = deletedDtlList;
	}
    
    private List<CounterCost> updatedDtlList;
    
    public List<CounterCost> getUpdatedDtlList() {
		return updatedDtlList;
	}
	public void setUpdatedDtlList(List<CounterCost> updatedDtlList) {
		this.updatedDtlList = updatedDtlList;
	}
	/**
	 * 打印次数
	 */
	private Integer printCount;
    
    public Integer getPrintCount() {
		return printCount;
	}
	public void setPrintCount(Integer printCount) {
		this.printCount = printCount;
	}
	
	private Integer type;
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "BillCounterBalance ["
            + "id="+getId()+",billNo="+billNo+",companyNo="+companyNo+",shopNo="+shopNo+",counterNo="+counterNo+",supplierNo="+supplierNo+",businessType="+businessType+",status="+status+",auditor="+auditor+",auditTime="+auditTime+",remark="+remark+",settleMonth="+settleMonth+",settleStartDate="+settleStartDate+",settleEndDate="+settleEndDate+",ableAmount="+ableAmount+",ableBillingAmount="+ableBillingAmount+",ablePriceAmount="+ablePriceAmount+",ableTaxAmount="+ableTaxAmount+",ableTaxBillingAmount="+ableTaxBillingAmount+",ableTaxPriceAmount="+ableTaxPriceAmount+",ableSum="+ableSum+",ableBillingSum="+ableBillingSum+",ablePriceSum="+ablePriceSum 
            +",preAbleSum="+preAbleSum+",notAbleSum="+notAbleSum+",preBillingSum="+preBillingSum+",notBillingSum="+notBillingSum+  "]";
	}
    
    
    
    public Query asQuery(){
    	return Q.where("counterNo",this.counterNo).and("supplierNo", this.supplierNo)
		.and("settleMonth", this.settleMonth).and("shopNo", this.shopNo)
		.and("settleStartDate", this.settleStartDate).and("settleEndDate", this.settleEndDate)
		.and("companyNo", this.companyNo);
    }
    
     /** auto generate end,don't modify */
}