
package topmall.fas.model;

import java.util.Date;
import java.math.BigDecimal; 
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import cn.mercury.domain.BasicEntity;
import cn.mercury.utils.JsonDateDeserializer$10;
import cn.mercury.utils.JsonDateDeserializer$19;
import cn.mercury.utils.JsonDateSerializer$10;
import cn.mercury.utils.JsonDateSerializer$19;

/**
 * 扣率调整单主表
 * @author Administrator
 *
 */
public class AdjustDiscount  extends BasicEntity { 
    /** auto generate start ,don't modify */
    private static final long serialVersionUID = 636402202211109594L;
    
    
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
    
    private String divisionNo;
    public String getDivisionNo() {
		return divisionNo;
	}
    public void setDivisionNo(String divisionNo) {
		this.divisionNo = divisionNo;
	}
    
    /**
    *
    **/
    private Short discountSource;
    public Short getDiscountSource(){
        return  discountSource;
    }    
    public void setDiscountSource(Short val ){
        discountSource = val;
    }
    
    /**
    *
    **/
    private Short discountType;
    public Short getDiscountType(){
        return  discountType;
    }    
    public void setDiscountType(Short val ){
        discountType = val;
    }
    
    /**
    *
    **/
    private BigDecimal discountRate;
    public BigDecimal getDiscountRate(){
        return  discountRate;
    }    
    public void setDiscountRate(BigDecimal val ){
        discountRate = val;
    }
    
    /**
    *
    **/
    private Short baseSettle;
    public Short getBaseSettle(){
        return  baseSettle;
    }    
    public void setBaseSettle(Short val ){
        baseSettle = val;
    }
    
    /**
    *
    **/
    @JsonSerialize(using = JsonDateSerializer$10.class)
   	@JsonDeserialize(using = JsonDateDeserializer$10.class)
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
    @JsonSerialize(using = JsonDateSerializer$10.class)
   	@JsonDeserialize(using = JsonDateDeserializer$10.class)
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
    private Short status;
    public Short getStatus(){
        return  status;
    }    
    public void setStatus(Short val ){
        status = val;
    }
    
    /**
    *
    **/
    private String confirmUser;
    public String getConfirmUser(){
        return  confirmUser;
    }    
    public void setConfirmUser(String val ){
        confirmUser = val;
    }
    
    /**
    *
    **/
    @JsonSerialize(using = JsonDateSerializer$19.class)
	@JsonDeserialize(using = JsonDateDeserializer$19.class)
    private Date confirmTime;
    public Date getConfirmTime(){
        return  confirmTime;
    }    
    public void setConfirmTime(Date val ){
        confirmTime = val;
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
    
    
    @Override
	public String toString() {
		return "MpsAdjustDiscount ["
            + "id="+getId()+",billNo="+billNo+",shopNo="+shopNo+",counterNo="+counterNo+",discountSource="+discountSource+",discountType="+discountType+",discountRate="+discountRate+",baseSettle="+baseSettle+",startDate="+startDate+",endDate="+endDate+",status="+status+",confirmUser="+confirmUser+",confirmTime="+confirmTime+",auditor="+auditor+",auditTime="+auditTime+",remark="+remark 
            + "]";
	}
    
     /** auto generate end,don't modify */
    
   	
   	private String shopName;
   	
   	private String counterName;
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getCounterName() {
		return counterName;
	}
	public void setCounterName(String counterName) {
		this.counterName = counterName;
	}
   	
	private String msg;
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
   	
}