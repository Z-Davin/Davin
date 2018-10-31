
package topmall.fas.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import cn.mercury.domain.BaseEntity;
import cn.mercury.utils.BigDecimalSerializer$2;
import cn.mercury.utils.JsonDateDeserializer$10;
import cn.mercury.utils.JsonDateDeserializer$19;
import cn.mercury.utils.JsonDateSerializer$10;
import cn.mercury.utils.JsonDateSerializer$19;

/**
 * 物业预付款登记
 * @author Administrator
 *
 */
public class MallPrepay  extends BaseEntity<String> { 
	  /** auto generate start ,don't modify */
    private static final long serialVersionUID = 636591532675151504L;
    
    /**
    *
    **/
    private String shopNo;
    public String getShopNo(){
        return  shopNo;
    }    
    public void setShopNo(String val ){
        shopNo = val;
    }
    
    /**
    *
    **/
    private String bunkGroupNo;
    public String getBunkGroupNo(){
        return  bunkGroupNo;
    }    
    public void setBunkGroupNo(String val ){
        bunkGroupNo = val;
    }
    
    /**
    *
    **/
    private String mallNo;
    public String getMallNo(){
        return  mallNo;
    }    
    public void setMallNo(String val ){
        mallNo = val;
    }
    
    /**
    *
    **/
    private String billNo;
    public String getBillNo(){
        return  billNo;
    }    
    public void setBillNo(String val ){
        billNo = val;
    }
    
    private Integer billType;
    public Integer getBillType() {
		return billType;
	}
	public void setBillType(Integer billType) {
		this.billType = billType;
	}
	/**
    *
    **/
    private String payMonth;
    public String getPayMonth(){
        return  payMonth;
    }    
    public void setPayMonth(String val ){
    	payMonth = val;
    }
    
    /**
    *
    **/
    @JsonDeserialize(using = JsonDateDeserializer$10.class)
   	@JsonSerialize(using = JsonDateSerializer$10.class)
    private Date startDate;
    public Date getStartDate(){
        return  startDate;
    }    
    public void setStartDate(Date val ){
        startDate = val;
    }
    
    /**
    *
    **/
    @JsonDeserialize(using = JsonDateDeserializer$10.class)
   	@JsonSerialize(using = JsonDateSerializer$10.class)
    private Date endDate;
    public Date getEndDate(){
        return  endDate;
    }    
    public void setEndDate(Date val ){
        endDate = val;
    }
    
    /**
    *
    **/
    private Integer status;
    public Integer getStatus(){
        return  status;
    }    
    public void setStatus(Integer val ){
        status = val;
    }
    
    /**
    *
    **/
    private Integer billDebit;
    public Integer getBillDebit(){
        return  billDebit;
    }    
    public void setBillDebit(Integer val ){
        billDebit = val;
    }
    
    
    /**
    *
    **/
    @JsonSerialize(using = BigDecimalSerializer$2.class)
    private BigDecimal prepayAmount;
    public BigDecimal getPrepayAmount(){
        return  prepayAmount;
    }    
    public void setPrepayAmount(BigDecimal val ){
    	prepayAmount = val;
    }
    
    /**
    *
    **/
    private String auditor;
    public String getAuditor(){
        return  auditor;
    }    
    public void setAuditor(String val ){
        auditor = val;
    }
    
    /**
    *
    **/
    @JsonDeserialize(using = JsonDateDeserializer$19.class)
   	@JsonSerialize(using = JsonDateSerializer$19.class)
    private Date auditTime;
    public Date getAuditTime(){
        return  auditTime;
    }    
    public void setAuditTime(Date val ){
        auditTime = val;
    }
    
    /**
    *
    **/
    private String remark;
    public String getRemark(){
        return  remark;
    }    
    public void setRemark(String val ){
        remark = val;
    }
    
    private List<MallPrepayDtl> updateMallPrepayDtlList;
    
    private List<MallPrepayDtl> insertMallPrepayDtlList;
    
    private List<MallPrepayDtl> deleteMallPrepayDtlList;
    
    public List<MallPrepayDtl> getUpdateMallPrepayDtlList() {
		return updateMallPrepayDtlList;
	}
	public void setUpdateMallPrepayDtlList(List<MallPrepayDtl> updateMallPrepayDtlList) {
		this.updateMallPrepayDtlList = updateMallPrepayDtlList;
	}
	public List<MallPrepayDtl> getInsertMallPrepayDtlList() {
		return insertMallPrepayDtlList;
	}
	public void setInsertMallPrepayDtlList(List<MallPrepayDtl> insertMallPrepayDtlList) {
		this.insertMallPrepayDtlList = insertMallPrepayDtlList;
	}
	public List<MallPrepayDtl> getDeleteMallPrepayDtlList() {
		return deleteMallPrepayDtlList;
	}
	public void setDeleteMallPrepayDtlList(List<MallPrepayDtl> deleteMallPrepayDtlList) {
		this.deleteMallPrepayDtlList = deleteMallPrepayDtlList;
	}
	@Override
	public String toString() {
		return "MallPrepay ["
            + "id="+getId()+",shopNo="+shopNo+",bunkGroupNo="+bunkGroupNo+",mallNo="+mallNo+",billNo="+billNo+",payMonth="+payMonth+",startDate="+startDate+",endDate="+endDate+",status="+status+",billDebit="+billDebit+",prePayAmount="+prepayAmount+",auditor="+auditor+",auditTime="+auditTime+",remark="+remark 
            + "]";
	}
    
     /** auto generate end,don't modify */
}