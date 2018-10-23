
package topmall.fas.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import cn.mercury.basic.query.Q;
import cn.mercury.basic.query.Query;
import cn.mercury.domain.BaseEntity;
import cn.mercury.utils.BigDecimalSerializer$2;
import cn.mercury.utils.JsonDateDeserializer$10;
import cn.mercury.utils.JsonDateDeserializer$19;
import cn.mercury.utils.JsonDateSerializer$10;
import cn.mercury.utils.JsonDateSerializer$19;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
* 物业结算单
**/
public class BillMallBalance  extends BaseEntity<String> { 
    /** auto generate start ,don't modify */
    private static final long serialVersionUID = 636445237205853985L;
    
    
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
    *单据类型
    **/
    private Integer billType;
    public Integer getBillType(){
        return  billType;
    }    
    public void setBillType(Integer val ){
        billType = val;
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
    *物业公司编码
    **/
    private String mallNo;
    public String getMallNo(){
        return  mallNo;
    }    
    public void setMallNo(String val ){
        mallNo = val;
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
    *合同面积
    **/
    private BigDecimal areaCompact;
    public BigDecimal getAreaCompact(){
        return  areaCompact;
    }    
    public void setAreaCompact(BigDecimal val ){
        areaCompact = val;
    }
    
    /**
    *开业日期(店铺正式营业的日期)
    **/
    @JsonSerialize(using = JsonDateSerializer$10.class)
 	@JsonDeserialize(using = JsonDateDeserializer$10.class)
    private Date openDate;
    public Date getOpenDate(){
        return  openDate;
    }    
    public void setOpenDate(Date val ){
        openDate = val;
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
     * 预开票总额
     */
    @JsonSerialize(using = BigDecimalSerializer$2.class)
    private BigDecimal preBillingAmount;
    
    /**
     * 未开票总额
     */
    @JsonSerialize(using = BigDecimalSerializer$2.class)
    private BigDecimal notBillingAmount;
    
    /**
     * 预结款总额
     */
    @JsonSerialize(using = BigDecimalSerializer$2.class)
    private BigDecimal preAbleAmount;
    
    /**
     * 商场开票金额
     */
    @JsonSerialize(using = BigDecimalSerializer$2.class)
    private BigDecimal mallBillingAmount;
    
    /**
     * 物业计入保底销售
     */
    @JsonSerialize(using = BigDecimalSerializer$2.class)
    private BigDecimal mallMinimumAmount;
    
    public BigDecimal getMallMinimumAmount() {
		return mallMinimumAmount;
	}
	public void setMallMinimumAmount(BigDecimal mallMinimumAmount) {
		this.mallMinimumAmount = mallMinimumAmount;
	}
	
	public BigDecimal getPreBillingAmount() {
		return preBillingAmount;
	}
	public void setPreBillingAmount(BigDecimal preBillingAmount) {
		this.preBillingAmount = preBillingAmount;
	}
	public BigDecimal getNotBillingAmount() {
		return notBillingAmount;
	}
	public void setNotBillingAmount(BigDecimal notBillingAmount) {
		this.notBillingAmount = notBillingAmount;
	}
	public BigDecimal getPreAbleAmount() {
		return preAbleAmount;
	}
	public void setPreAbleAmount(BigDecimal preAbleAmount) {
		this.preAbleAmount = preAbleAmount;
	}
	public BigDecimal getMallBillingAmount() {
		return mallBillingAmount;
	}
	public void setMallBillingAmount(BigDecimal mallBillingAmount) {
		this.mallBillingAmount = mallBillingAmount;
	}

	/**
     * 铺位组
     */
    private String bunkGroupNo;
    
    
    public String getBunkGroupNo() {
		return bunkGroupNo;
	}
	public void setBunkGroupNo(String val) {
		this.bunkGroupNo = val;
	}
    
	private List<MallCost> deleteMallCostList;
	
	private List<MallPay> updateMallPayList;
    
    public List<MallPay> getUpdateMallPayList() {
		return updateMallPayList;
	}
	public void setUpdateMallPayList(List<MallPay> updateMallPayList) {
		this.updateMallPayList = updateMallPayList;
	}
	public List<MallCost> getDeleteMallCostList() {
		return deleteMallCostList;
	}
	public void setDeletedMallCostList(List<MallCost> deleteMallCostList) {
		this.deleteMallCostList = deleteMallCostList;
	}

	private List<MallCost> updateMallCostList;
    

	public List<MallCost> getUpdateMallCostList() {
		return updateMallCostList;
	}
	public void setUpdatedMallCostList(List<MallCost> updateMallCostList) {
		this.updateMallCostList = updateMallCostList;
	}
	
	private List<MallCost> insertMallCostList;
	
	public List<MallCost> getInsertMallCostList() {
		return insertMallCostList;
	}
	public void setInsertMallCostList(List<MallCost> insertMallCostList) {
		this.insertMallCostList = insertMallCostList;
	}
	
	private List<MallSaleCost> insertMallSaleCostList;
	
	public List<MallSaleCost> getInsertMallSaleCostList() {
		return insertMallSaleCostList;
	}
	public void setInsertMallSaleCostList(List<MallSaleCost> insertMallSaleCostList) {
		this.insertMallSaleCostList = insertMallSaleCostList;
	}

	private List<MallSaleCost> deleteMallSaleCostList;
	
	public List<MallSaleCost> getDeleteMallSaleCostList() {
		return deleteMallSaleCostList;
	}
	public void setDeletedMallSaleCostList(List<MallSaleCost> deleteMallSaleCostList) {
		this.deleteMallSaleCostList = deleteMallSaleCostList;
	}

	private List<MallSaleCost> updateMallSaleCostList;
	
	
	public List<MallSaleCost> getUpdateMallSaleCostList() {
		return updateMallSaleCostList;
	}
	public void setUpdatedMallSaleCostList(List<MallSaleCost> updateMallSaleCostList) {
		this.updateMallSaleCostList = updateMallSaleCostList;
	}
	
	  public Query asQuery(){
	    	Query query = Q.where("shopNo",this.shopNo).and("mallNo", this.mallNo)
			.and("settleMonth", this.settleMonth).and("settleStartDate", this.settleStartDate).and("settleEndDate", this.settleEndDate)
			.and("bunkGroupNo", this.bunkGroupNo);
	    	return query;
	    }
	  
	@Override
	public String toString() {
		return "BillMallBalance ["
            + "id="+getId()+",billNo="+billNo+",billType="+billType+",companyNo="+companyNo+",shopNo="+shopNo+",mallNo="+mallNo+",businessType="+businessType+",areaCompact="+areaCompact+",openDate="+openDate+",status="+status+",auditor="+auditor+",auditTime="+auditTime+",remark="+remark+",settleMonth="+settleMonth+",settleStartDate="+settleStartDate+",settleEndDate="+settleEndDate 
            + "]";
	}
    
     /** auto generate end,don't modify */
}