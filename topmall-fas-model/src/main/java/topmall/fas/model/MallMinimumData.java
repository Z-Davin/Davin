
package topmall.fas.model;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import cn.mercury.utils.BigDecimalSerializer$2;

import java.math.BigDecimal; 

/**
* 
**/
public class MallMinimumData extends cn.mercury.domain.BaseEntity<String> { 
    /** auto generate start ,don't modify */
    private static final long serialVersionUID = 636722734353432084L;
    
    /**
    *大区编码
    **/
    private String zoneNo;
    public String getZoneNo(){
        return  zoneNo;
    }    
    public void setZoneNo(String val ){
        zoneNo = val;
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
    * 门店编号
    **/
    private String shopNo;
    public String getShopNo(){
        return  shopNo;
    }    
    public void setShopNo(String val ){
        shopNo = val;
    }
    
    /**
    * 铺位组编码
    **/
    private String bunkGroupNo;
    public String getBunkGroupNo(){
        return  bunkGroupNo;
    }    
    public void setBunkGroupNo(String val ){
        bunkGroupNo = val;
    }
    
    /**
    * 商场编码
    **/
    private String mallNo;
    public String getMallNo(){
        return  mallNo;
    }    
    public void setMallNo(String val ){
        mallNo = val;
    }
    
    /**
    * 经营类型：1：联营(客户)，2：租赁（供应商）
    **/
    private Short businessType;
    public Short getBusinessType(){
        return  businessType;
    }    
    public void setBusinessType(Short val ){
        businessType = val;
    }
    
    /**
    * 合同面积
    **/
    private BigDecimal areaCompact;
    public BigDecimal getAreaCompact(){
        return  areaCompact;
    }    
    public void setAreaCompact(BigDecimal val ){
        areaCompact = val;
    }
    
    /**
    * 结算月
    **/
    private String settleMonth;
    public String getSettleMonth(){
        return  settleMonth;
    }    
    public void setSettleMonth(String val ){
        settleMonth = val;
    }
    
    /**
    * 结算开始日期
    **/
    private Date settleStartDate;
    public Date getSettleStartDate(){
        return  settleStartDate;
    }    
    public void setSettleStartDate(Date val ){
        settleStartDate = val;
    }
    
    /**
    * 结算结束日期
    **/
    private Date settleEndDate;
    public Date getSettleEndDate(){
        return  settleEndDate;
    }    
    public void setSettleEndDate(Date val ){
        settleEndDate = val;
    }
    
    /**
    * 实际销售金额
    **/
    @JsonSerialize(using = BigDecimalSerializer$2.class)
    private BigDecimal settleAmount;
    public BigDecimal getSettleAmount(){
        return  settleAmount==null?new BigDecimal(0):settleAmount;
    }    
    public void setSettleAmount(BigDecimal val ){
        settleAmount = val;
    }
    
    /**
    * 物业计入保底销售
    **/
    @JsonSerialize(using = BigDecimalSerializer$2.class)
    private BigDecimal mallMinimumAmount;
    public BigDecimal getMallMinimumAmount(){
        return  mallMinimumAmount==null?new BigDecimal(0):mallMinimumAmount;
    }    
    public void setMallMinimumAmount(BigDecimal val ){
        mallMinimumAmount = val;
    }
    
    /**
    * 我司计入保底销售
    **/
    @JsonSerialize(using = BigDecimalSerializer$2.class)
    private BigDecimal minimumAmount;
    public BigDecimal getMinimumAmount(){
        return  minimumAmount==null?new BigDecimal(0):minimumAmount;
    }    
    public void setMinimumAmount(BigDecimal val ){
        minimumAmount = val;
    }
    
    /**
    * 未计入保底销售
    **/
    @JsonSerialize(using = BigDecimalSerializer$2.class)
    private BigDecimal notMinimumAmount;
    public BigDecimal getNotMinimumAmount(){
        return  notMinimumAmount==null?new BigDecimal(0):notMinimumAmount;
    }    
    public void setNotMinimumAmount(BigDecimal val ){
        notMinimumAmount = val;
    }
    
    /**
    * 结算表实际销售扣费（NC扣费）
    **/
    @JsonSerialize(using = BigDecimalSerializer$2.class)
    private BigDecimal ncDeductionAmount;
    public BigDecimal getNcDeductionAmount(){
        return  ncDeductionAmount==null?new BigDecimal(0):ncDeductionAmount;
    }    
    public void setNcDeductionAmount(BigDecimal val ){
    	ncDeductionAmount = val;
    }
    
    /**
    * 基础表数据
    **/
    @JsonSerialize(using = BigDecimalSerializer$2.class)
    private BigDecimal basicAmount;
    public BigDecimal getBasicAmount(){
        return  basicAmount==null?new BigDecimal(0):basicAmount;
    }    
    public void setBasicAmount(BigDecimal val ){
        basicAmount = val;
    }
    
    /**
    * 备注
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
		return "MallMinimumData ["
            + "id="+getId()+",zoneNo="+zoneNo+",companyNo="+companyNo+",shopNo="+shopNo+",bunkGroupNo="+bunkGroupNo+",mallNo="+mallNo+",businessType="+businessType+",areaCompact="+areaCompact+",settleMonth="+settleMonth+",settleStartDate="+settleStartDate+",settleEndDate="+settleEndDate+",settleAmount="+settleAmount+",mallMinimumAmount="+mallMinimumAmount+",minimumAmount="+minimumAmount+",notMinimumAmount="+notMinimumAmount+",ncDeductionAmount="+ncDeductionAmount+",basicAmount="+basicAmount+",remark="+remark 
            + "]";
	}
    
     /** auto generate end,don't modify */
}