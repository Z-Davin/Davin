
package topmall.fas.model;

import java.util.Date;

import cn.mercury.domain.BaseEntity;
import cn.mercury.utils.JsonDateDeserializer$10;
import cn.mercury.utils.JsonDateSerializer$10;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
* 门店结算期设置表
**/
public class ShopBalanceDate  extends BaseEntity<String> { 
    /** auto generate start ,don't modify */
    private static final long serialVersionUID = 636389958532760786L;
    
    
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
    *门店编号
    **/
    private String shopNo;
    public String getShopNo(){
        return  shopNo;
    }    
    public void setShopNo(String val ){
        shopNo = val;
    }
    
    
    /**
    *结算结束日
    **/
    private Integer endDate;
    public Integer getEndDate(){
        return  endDate;
    }    
    public void setEndDate(Integer val ){
        endDate = val;
    }
    
    /**
    *状态(0:停用;1:启用)
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
     * 区总关账日期
     */
	@JsonDeserialize(using = JsonDateDeserializer$10.class)
	@JsonSerialize(using = JsonDateSerializer$10.class)
    private Date closeDate;
    
    
    public Date getCloseDate() {
		return closeDate;
	}
	public void setCloseDate(Date val) {
		this.closeDate = val;
	}
	@Override
	public String toString() {
		return "ShopBalanceDate ["
            + "id="+getId()+",zoneNo="+zoneNo+",companyNo="+companyNo+",shopNo="+shopNo+",endDate="+endDate+",status="+status+",remark="+remark 
            + ",closeDate="+closeDate+"]";
	}
    
     /** auto generate end,don't modify */
}