
package topmall.fas.model;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import cn.mercury.domain.BaseEntity;
import cn.mercury.utils.JsonDateDeserializer$19;
import cn.mercury.utils.JsonDateSerializer$19;


/**
* 
**/
public class SupplierAccount  extends BaseEntity<String> { 
    /** auto generate start ,don't modify */
    private static final long serialVersionUID = 636631247382061516L;
    
    
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
    private String bankName;
    public String getBankName(){
        return  bankName;
    }    
    public void setBankName(String val ){
        bankName = val;
    }
    
    /**
    *
    **/
    private String bankAccount;
    public String getBankAccount(){
        return  bankAccount;
    }    
    public void setBankAccount(String val ){
        bankAccount = val;
    }
    
    /**
    *
    **/
    private String bankAccountName;
    public String getBankAccountName(){
        return  bankAccountName;
    }    
    public void setBankAccountName(String val ){
        bankAccountName = val;
    }
    
    /**
    *
    **/
    private String accountSetNo;
    public String getAccountSetNo(){
        return  accountSetNo;
    }    
    public void setAccountSetNo(String val ){
        accountSetNo = val;
    }
    
    /**
    *
    **/
    private String accountSetName;
    public String getAccountSetName(){
        return  accountSetName;
    }    
    public void setAccountSetName(String val ){
        accountSetName = val;
    }
    
    /**
    *
    **/
    private String oldAccountSetNo;
    public String getOldAccountSetNo(){
        return  oldAccountSetNo;
    }    
    public void setOldAccountSetNo(String val ){
        oldAccountSetNo = val;
    }
    
    /**
    *
    **/
    private String oldAccountSetName;
    public String getOldAccountSetName(){
        return  oldAccountSetName;
    }    
    public void setOldAccountSetName(String val ){
        oldAccountSetName = val;
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
    private String settleMonth;
    public String getSettleMonth(){
        return  settleMonth;
    }    
    public void setSettleMonth(String val ){
        settleMonth = val;
    }
    
    private String taxNo;
    
    public String getTaxNo() {
		return taxNo;
	}
	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
	}

	private String address;
    
    
    public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	private String suCompanyName;
	
	public String getSuCompanyName() {
		return suCompanyName;
	}
	public void setSuCompanyName(String suCompanyName) {
		this.suCompanyName = suCompanyName;
	}
	@Override
	public String toString() {
		return "SupplierAccount ["
            + "id="+getId()+",zoneNo="+zoneNo+",companyNo="+companyNo+",shopNo="+shopNo+",counterNo="+counterNo+",supplierNo="+supplierNo+",bankName="+bankName+",bankAccount="+bankAccount+",bankAccountName="+bankAccountName+",accountSetNo="+accountSetNo+",accountSetName="+accountSetName+",oldAccountSetNo="+oldAccountSetNo+",oldAccountSetName="+oldAccountSetName+",status="+status+",auditor="+auditor+",auditTime="+auditTime+",remark="+remark+",settleMonth="+settleMonth+"]";
	}
    
     /** auto generate end,don't modify */
}