
package topmall.fas.model;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import cn.mercury.basic.query.Q;
import cn.mercury.basic.query.Query;
import cn.mercury.utils.JsonDateDeserializer$10;
import cn.mercury.utils.JsonDateSerializer$10;

/**
* 物业结算期设置表
**/
public class MallBalanceDate  extends cn.mercury.domain.BaseEntity<String> { 
    /** auto generate start ,don't modify */
    private static final long serialVersionUID = 636434192773816332L;
    
    
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
    *结束时间(32:自然月)
    **/
    private String endDate;
    public String getEndDate(){
        return  endDate;
    }    
    public void setEndDate(String val ){
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
    *区总关账日期
    **/
    @JsonDeserialize(using = JsonDateDeserializer$10.class)
	@JsonSerialize(using = JsonDateSerializer$10.class)
    private Date closeDate;
    public Date getCloseDate(){
        return  closeDate;
    }    
    public void setCloseDate(Date val ){
        closeDate = val;
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
     * 铺位组
     */
    private String bunkGroupNo;
    
    
    public String getBunkGroupNo() {
		return bunkGroupNo;
	}
	public void setBunkGroupNo(String val) {
		this.bunkGroupNo = val;
	}
	
	/**
	 * 0:同意抵扣,1:不同意抵扣
	 */
	private Integer balanceType;
	
	public Integer getBalanceType() {
		return balanceType;
	}
	public void setBalanceType(Integer val) {
		this.balanceType = val;
	}
	
	/**
	 * 积分低现是否参与计算(0:不参与计算;1:参与计算)
	 */
	private Integer pointsCalculateFlag;
	
	
	public Integer getPointsCalculateFlag() {
		return pointsCalculateFlag;
	}
	public void setPointsCalculateFlag(Integer val) {
		this.pointsCalculateFlag = val;
	}
	public Query baseQuery(){
		Query q=Q.where("bunkGroupNo", this.bunkGroupNo).and("shopNo",this.shopNo).and("mallNo", this.mallNo);
		return q;
	}
	@Override
	public String toString() {
		return "MallBalanceDate ["
            + "id="+getId()+",zoneNo="+zoneNo+",companyNo="+companyNo+",mallNo="+mallNo+",shopNo="+shopNo+",endDate="+endDate+"status="+status+",closeDate="+closeDate+",remark="+remark 
            + "]";
	}
    
}