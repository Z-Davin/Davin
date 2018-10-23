
package topmall.fas.model;

import java.math.BigDecimal;
import java.util.Date;

import cn.mercury.domain.BaseEntity;
import cn.mercury.utils.JsonDateDeserializer$10;
import cn.mercury.utils.JsonDateSerializer$10;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
* 物业费用登记
**/
public class PropertyCost  extends BaseEntity<String> { 
    /** auto generate start ,don't modify */
    private static final long serialVersionUID = 636462745423076947L;
    
    /**
    *大区
    **/
    private String zoneNo;
    public String getZoneNo(){
        return  zoneNo;
    }    
    public void setZoneNo(String val ){
        zoneNo = val;
    }
    
    /**
    *卖场编码
    **/
    private String shopNo;
    public String getShopNo(){
        return  shopNo;
    }    
    public void setShopNo(String val ){
        shopNo = val;
    }
    
    /**
    *商场编码
    **/
    private String mallNo;
    public String getMallNo(){
        return  mallNo;
    }    
    public void setMallNo(String val ){
        mallNo = val;
    }
    
    /**
    *公司编码
    **/
    private String companyNo;
    public String getCompanyNo(){
        return  companyNo;
    }    
    public void setCompanyNo(String val ){
        companyNo = val;
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
    @JsonDeserialize(using = JsonDateDeserializer$10.class)
 	@JsonSerialize(using = JsonDateSerializer$10.class)
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
    @JsonDeserialize(using = JsonDateDeserializer$10.class)
 	@JsonSerialize(using = JsonDateSerializer$10.class)
    private Date settleEndDate;
    public Date getSettleEndDate(){
        return  settleEndDate;
    }    
    public void setSettleEndDate(Date val ){
        settleEndDate = val;
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
    *期初数
    **/
    private BigDecimal startNum;
    public BigDecimal getStartNum(){
        return  startNum;
    }    
    public void setStartNum(BigDecimal val ){
        startNum = val;
    }
    
    /**
    *期末数
    **/
    private BigDecimal endNum;
    public BigDecimal getEndNum(){
        return  endNum;
    }    
    public void setEndNum(BigDecimal val ){
        endNum = val;
    }
    
    /**
    *数量
    **/
    private BigDecimal number;
    public BigDecimal getNumber(){
        return  number;
    }    
    public void setNumber(BigDecimal val ){
        number = val;
    }
    
    private BigDecimal firstRatio;
	public BigDecimal getFirstRatio() {
		return firstRatio;
	}
	public void setFirstRatio(BigDecimal firstRatio) {
		this.firstRatio = firstRatio;
	}

	private BigDecimal secondRatio;
	public BigDecimal getSecondRatio() {
		return secondRatio;
	}
	public void setSecondRatio(BigDecimal secondRatio) {
		this.secondRatio = secondRatio;
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
    private Date auditTime;
    public Date getAuditTime(){
        return  auditTime;
    }    
    public void setAuditTime(Date val ){
        auditTime = val;
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
    
    
    @Override
	public String toString() {
		return "PropertyCost ["
            + "id="+getId()+",zoneNo="+zoneNo+",shopNo="+shopNo+",mallNo="+mallNo+",companyNo="+companyNo+",settleMonth="+settleMonth+",settleStartDate="+settleStartDate+",settleEndDate="+settleEndDate+",costNo="+costNo+",startNum="+startNum+",endNum="+endNum+",number="+number+",status="+status+",remark="+remark+",auditor="+auditor+",auditTime="+auditTime 
            + "]";
	}
    
     /** auto generate end,don't modify */
}