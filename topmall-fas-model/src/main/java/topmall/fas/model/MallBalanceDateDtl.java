
package topmall.fas.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import cn.mercury.basic.query.Q;
import cn.mercury.basic.query.Query;
import cn.mercury.utils.JsonDateDeserializer$10;
import cn.mercury.utils.JsonDateSerializer$10;

/**
* 物业结算期设置表-明细
**/
public class MallBalanceDateDtl  extends cn.mercury.domain.BaseEntity<String> { 
    /** auto generate start ,don't modify */
    private static final long serialVersionUID = 636434192784866964L;
    
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
	 * 积分低现是否参与计算(0:不参与计算;1:参与计算)
	 */
	private Integer pointsCalculateFlag;
	
	
	public Integer getPointsCalculateFlag() {
		return pointsCalculateFlag;
	}
	public void setPointsCalculateFlag(Integer val) {
		this.pointsCalculateFlag = val;
	}
	
	/**
	 * 计算方式:0:毛收入,1:净收入
	 */
	private Integer calculationMethod;
	
	public Integer getCalculationMethod() {
		return calculationMethod;
	}
	public void setCalculationMethod(Integer calculationMethod) {
		this.calculationMethod = calculationMethod;
	}
    
    /**
    *结算月(201707)
    **/
    private String settleMonth;
    public String getSettleMonth(){
        return  settleMonth;
    }    
    public void setSettleMonth(String val ){
        settleMonth = val;
    }
    
    /**
    *结算开始日期
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
    *结算结束日期
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
    *应对账日(32:自然月)
    **/
    @JsonDeserialize(using = JsonDateDeserializer$10.class)
	@JsonSerialize(using = JsonDateSerializer$10.class)
    private Date checkDate;
    public Date getCheckDate(){
        return  checkDate;
    }    
    public void setCheckDate(Date val ){
        checkDate = val;
    }
    
    /**
    *应交票日(32:自然月)
    **/
    @JsonDeserialize(using = JsonDateDeserializer$10.class)
	@JsonSerialize(using = JsonDateSerializer$10.class)
    private Date payTicketDate;
    public Date getPayTicketDate(){
        return  payTicketDate;
    }    
    public void setPayTicketDate(Date val ){
        payTicketDate = val;
    }
    
    /**
    *应结款日(32:自然月)
    **/
    @JsonDeserialize(using = JsonDateDeserializer$10.class)
	@JsonSerialize(using = JsonDateSerializer$10.class)
    private Date paymentDate;
    public Date getPaymentDate(){
        return  paymentDate;
    }    
    public void setPaymentDate(Date val ){
        paymentDate = val;
    }
    
    /**
    *状态(1:未生效,2:生效)
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
     * 专柜编码列表
     */
    private List<String> counterList;
    
    
    public List<String> getCounterList() {
		return counterList;
	}
	public void setCounterList(List<String> counterList) {
		this.counterList = counterList;
	}
	
	public void copyProperties(MallSaleCost mallSaleCost){
		mallSaleCost.setShopNo(this.shopNo);
		mallSaleCost.setSettleMonth(this.settleMonth);
		mallSaleCost.setSettleEndDate(this.settleEndDate);
		mallSaleCost.setSettleStartDate(this.settleStartDate);
		mallSaleCost.setMallNo(this.mallNo);
		mallSaleCost.setBunkGroupNo(this.bunkGroupNo);
	}
	
	public void copyProperties(MallMinimumData mallMinimum){
		mallMinimum.setShopNo(this.shopNo);
		mallMinimum.setSettleMonth(this.settleMonth);
		mallMinimum.setSettleEndDate(this.settleEndDate);
		mallMinimum.setSettleStartDate(this.settleStartDate);
		mallMinimum.setMallNo(this.mallNo);
		mallMinimum.setBunkGroupNo(this.bunkGroupNo);
	}
	
	public Query baseQuery(){
		return  Q.where("shopNo",this.shopNo).and("mallNo",this.mallNo)
				.and("settleStartDate", this.settleStartDate).and("settleEndDate", this.settleEndDate).and("bunkGroupNo", this.bunkGroupNo).and("settleMonth", this.settleMonth);
	}
	
	@Override
	public String toString() {
		return "MallBalanceDateDtl ["
            + "id="+getId()+",zoneNo="+zoneNo+",companyNo="+companyNo+",shopNo="+shopNo+",mallNo="+mallNo+",settleMonth="+settleMonth+",settleStartDate="+settleStartDate+",settleEndDate="+settleEndDate+",checkDate="+checkDate+",payTicketDate="+payTicketDate+",paymentDate="+paymentDate+",status="+status+",remark="+remark 
            + "]";
	}
    
     /** auto generate end,don't modify */
}