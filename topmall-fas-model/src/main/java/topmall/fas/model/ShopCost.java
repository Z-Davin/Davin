
package topmall.fas.model;

import java.util.Date;

import cn.mercury.utils.JsonDateDeserializer$10;
import cn.mercury.utils.JsonDateDeserializer$19;
import cn.mercury.utils.JsonDateSerializer$10;
import cn.mercury.utils.JsonDateSerializer$19;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 卖场费用登记
 * @author Administrator
 *
 */
public class ShopCost  extends cn.mercury.domain.BaseEntity<String> { 
    /** auto generate start ,don't modify */
    private static final long serialVersionUID = 636416009249625734L;
    
    /**
    *
    **/
    private String refId;
    public String getRefId(){
        return  refId;
    }    
    public void setRefId(String val ){
        refId = val;
    }
    
    /**
    *
    **/
    private String seqId;
    public String getSeqId(){
        return  seqId;
    }    
    public void setSeqId(String val ){
        seqId = val;
    }
    
    /**
    *
    **/
    private String zoneNo;
    public String getZoneNo(){
        return  zoneNo;
    }    
    public void setZoneNo(String val ){
        zoneNo = val;
    }
    
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
    private String counterNo;
    public String getCounterNo(){
        return  counterNo;
    }    
    public void setCounterNo(String val ){
        counterNo = val;
    }
    
    /**
    *
    **/
    private String companyNo;
    public String getCompanyNo(){
        return  companyNo;
    }    
    public void setCompanyNo(String val ){
        companyNo = val;
    }
    
    /**
    *
    **/
    private String supplierNo;
    public String getSupplierNo(){
        return  supplierNo;
    }    
    public void setSupplierNo(String val ){
        supplierNo = val;
    }
    
    /**
    *
    **/
    private String settleMonth;
    public String getSettleMonth(){
        return  settleMonth;
    }    
    public void setSettleMonth(String val ){
        settleMonth = val;
    }
    
    /**
    *
    **/
    private String costNo;
    public String getCostNo(){
        return  costNo;
    }    
    public void setCostNo(String val ){
        costNo = val;
    }
    
    /**
    *
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
    *
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
    *
    **/
    private Integer number;
    public Integer getNumber(){
        return  number;
    }    
    public void setNumber(Integer val ){
        number = val;
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
    private String remark;
    public String getRemark(){
        return  remark;
    }    
    public void setRemark(String val ){
        remark = val;
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
    @JsonSerialize(using = JsonDateSerializer$19.class)
	@JsonDeserialize(using = JsonDateDeserializer$19.class)
    private Date auditTime;
    public Date getAuditTime(){
        return  auditTime;
    }    
    public void setAuditTime(Date val ){
        auditTime = val;
    }
    
    
    @Override
	public String toString() {
		return "ShopCost ["
            + "id="+getId()+",refId="+refId+",seqId="+seqId+",zoneNo="+zoneNo+",shopNo="+shopNo+",counterNo="+counterNo+",companyNo="+companyNo+",supplierNo="+supplierNo+",settleMonth="+settleMonth+",costNo="+costNo+",settleStartDate="+settleStartDate+",settleEndDate="+settleEndDate+",number="+number+",status="+status+",remark="+remark+",auditor="+auditor+",auditTime="+auditTime 
            + "]";
	}
    
     /** auto generate end,don't modify */
}