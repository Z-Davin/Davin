
package topmall.fas.model;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import cn.mercury.domain.BaseEntity;
import cn.mercury.utils.JsonDateDeserializer$10;
import cn.mercury.utils.JsonDateSerializer$10;

/**
 * 物业销售费用
 * @author Administrator
 *
 */
public class MallPay  extends BaseEntity<String> { 
    /** auto generate start ,don't modify */
    private static final long serialVersionUID = 636464505583417829L;
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
    private String balanceBillNo;
    public String getBalanceBillNo(){
        return  balanceBillNo;
    }    
    public void setBalanceBillNo(String val ){
        balanceBillNo = val;
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
    private String payNo;
    public String getPayNo(){
        return  payNo;
    }    
    public void setPayNo(String val ){
        payNo = val;
    }
    
    /**
    *
    **/
    private String payName;
    public String getPayName(){
        return  payName;
    }    
    public void setPayName(String val ){
        payName = val;
    }
    
    /**
    *
    **/
    private BigDecimal payAmount;
    public BigDecimal getPayAmount(){
        return  payAmount;
    }    
    public void setPayAmount(BigDecimal val ){
        payAmount = val;
    }
    
    /**
    *
    **/
    private Integer paidWay;
    public Integer getPaidWay(){
        return  paidWay;
    }    
    public void setPaidWay(Integer val ){
        paidWay = val;
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
	 * 差异金额
	 */
	private BigDecimal diffPayAmount;
	
   public BigDecimal getDiffPayAmount() {
		return diffPayAmount;
	}
	public void setDiffPayAmount(BigDecimal diffPayAmount) {
		this.diffPayAmount = diffPayAmount;
	}
	
	/**
	 * 物业方报数
	 */
	private BigDecimal mallPayAmount;
	
	public BigDecimal getMallPayAmount() {
		return mallPayAmount;
	}
	public void setMallPayAmount(BigDecimal mallPayAmount) {
		this.mallPayAmount = mallPayAmount;
	}
	@Override
	public String toString() {
		return "FasMallPay ["
            + "id="+getId()+",seqId="+seqId+",balanceBillNo="+balanceBillNo+",mallNo="+mallNo+",shopNo="+shopNo+",status="+status+",settleMonth="+settleMonth+",settleStartDate="+settleStartDate+",settleEndDate="+settleEndDate+",payNo="+payNo+",payName="+payName+",payAmount="+payAmount+",paidWay="+paidWay 
            + "]";
	}
    
     /** auto generate end,don't modify */
}