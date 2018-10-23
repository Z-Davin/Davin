
package topmall.fas.model;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import cn.mercury.domain.BaseEntity;
import cn.mercury.utils.BigDecimalSerializer$2;
import cn.mercury.utils.JsonDateDeserializer$10;
import cn.mercury.utils.JsonDateSerializer$10;

/**
* 物业销售费用
**/
public class MallSaleCost  extends BaseEntity<String> { 
    /** auto generate start ,don't modify */
    private static final long serialVersionUID = 636440195813181363L;
    
    /**
    *批次号
    **/
    private String seqId;
    public String getSeqId(){
        return  seqId;
    }    
    public void setSeqId(String val ){
        seqId = val;
    }
    
    /**
    *结算单编码
    **/
    private String balanceBillNo;
    public String getBalanceBillNo(){
        return  balanceBillNo;
    }    
    public void setBalanceBillNo(String val ){
        balanceBillNo = val;
    }
    
    /**
    *物业编码
    **/
    private String mallNo;
    public String getMallNo(){
        return  mallNo;
    }    
    public void setMallNo(String val ){
        mallNo = val;
    }
    
    /**
    *销售门店编号
    **/
    private String shopNo;
    public String getShopNo(){
        return  shopNo;
    }    
    public void setShopNo(String val ){
        shopNo = val;
    }
    
    /**
    *状态
    **/
    private Integer status;
    public Integer getStatus(){
        return  status;
    }    
    public void setStatus(Integer val ){
        status = val;
    }
    
    /**
    *销售码类型（正价/特价/促销价）0:正价，1：特价
    **/
    private String priceType;
    public String getPriceType(){
        return  priceType;
    }    
    public void setPriceType(String val ){
        priceType = val;
    }
    
    /**
    *扣率(营促销)
    **/
    private BigDecimal rateValue;
    public BigDecimal getRateValue(){
        return  rateValue;
    }    
    public void setRateValue(BigDecimal val ){
        rateValue = val;
    }
    
    /**
    *账扣标识
    **/
    private Integer accountDebit;
    public Integer getAccountDebit(){
        return  accountDebit;
    }    
    public void setAccountDebit(Integer val ){
        accountDebit = val;
    }
    
    /**
    *票扣标识
    **/
    private Integer billDebit;
    public Integer getBillDebit(){
        return  billDebit;
    }    
    public void setBillDebit(Integer val ){
        billDebit = val;
    }
    
    /**
    *生成类型(0:正常生成,1:重算生成)
    **/
    private Short type;
    public Short getType(){
        return  type;
    }    
    public void setType(Short val ){
        type = val;
    }
    
    /**
    *税率
    **/
    private BigDecimal raxRate;
    public BigDecimal getRaxRate(){
        return  raxRate;
    }    
    public void setRaxRate(BigDecimal val ){
        raxRate = val;
    }
    
    /**
    *销售总额
    **/
    @JsonSerialize(using = BigDecimalSerializer$2.class)
    private BigDecimal settleSum;
    public BigDecimal getSettleSum(){
        return  settleSum;
    }    
    public void setSettleSum(BigDecimal val ){
        settleSum = val;
    }
    
    
    /**
    *销售提成
    **/
    @JsonSerialize(using = BigDecimalSerializer$2.class)
    private BigDecimal profitAmount;
    public BigDecimal getProfitAmount(){
        return  profitAmount;
    }    
    public void setProfitAmount(BigDecimal val ){
        profitAmount = val;
    }
    
    /**
     * 物业扣费
     */
    @JsonSerialize(using = BigDecimalSerializer$2.class)
    private BigDecimal mallProfitAmount;
    

	public BigDecimal getMallProfitAmount() {
		return mallProfitAmount;
	}
	public void setMallProfitAmount(BigDecimal mallProfitAmount) {
		this.mallProfitAmount = mallProfitAmount;
	}

	/**
     * 扣费差异
     */
    @JsonSerialize(using = BigDecimalSerializer$2.class)
    private BigDecimal diffProfitAmount;
    
    
    
    public BigDecimal getDiffProfitAmount() {
		return diffProfitAmount;
	}
	public void setDiffProfitAmount(BigDecimal diffProfitAmount) {
		this.diffProfitAmount = diffProfitAmount;
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
    *结算结束时间
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
     * 物业报数
     */
    @JsonSerialize(using = BigDecimalSerializer$2.class)
    private BigDecimal mallAmount;
    
    public BigDecimal getMallAmount() {
		return mallAmount==null?new BigDecimal(0):mallAmount;
	}
	public void setMallAmount(BigDecimal mallAmount) {
		this.mallAmount = mallAmount;
	}
    
    /**
     * 差异金额
     */
    @JsonSerialize(using = BigDecimalSerializer$2.class)
    private BigDecimal diffAmount;
    
    public BigDecimal getDiffAmount() {
		return diffAmount==null?new BigDecimal(0):diffAmount;
	}
	public void setDiffAmount(BigDecimal diffAmount) {
		this.diffAmount = diffAmount;
	}
    
    
    /**
     * 差异原因
     */
    private String diffReason;
    
   
	
    public String getDiffReason() {
		return diffReason;
	}
	public void setDiffReason(String diffReason) {
		this.diffReason = diffReason;
	}
	
	/**
     * 调整金额
     */
	  @JsonSerialize(using = BigDecimalSerializer$2.class)
    private BigDecimal adjustAmount;
    
    public BigDecimal getAdjustAmount() {
		return adjustAmount==null?new BigDecimal(0):adjustAmount;
	}
	public void setAdjustAmount(BigDecimal adjustAmount) {
		this.adjustAmount = adjustAmount;
	}
	
	/**
	 * 父ID
	 */
	private String parentId;

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * 上期差异金额
	 */
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal preDiffAmount;
	
	public BigDecimal getPreDiffAmount() {
		return  preDiffAmount==null?new BigDecimal(0):preDiffAmount;
	}

	public void setPreDiffAmount(BigDecimal preDiffAmount) {
		this.preDiffAmount = preDiffAmount;
	}


	/**
	 * 本地差异金额
	 */
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal curDiffAmount;
	
	public BigDecimal getCurDiffAmount() {
		return curDiffAmount;
	}

	public void setCurDiffAmount(BigDecimal curDiffAmount) {
		this.curDiffAmount = curDiffAmount;
	}

	/**
	 * 汇款金额
	 */
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal backAmount;

	
	public BigDecimal getBackAmount() {
		return backAmount==null?new BigDecimal(0):backAmount;
	}

	public void setBackAmount(BigDecimal backAmount) {
		this.backAmount = backAmount;
	}
	
	/**
	 * 结算状态
	 */
	private Integer settleStatus;

	public Integer getSettleStatus() {
		return settleStatus;
	}

	public void setSettleStatus(Integer settleStatus) {
		this.settleStatus = settleStatus;
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
	 * 积分低现金额
	 */
	  @JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal pointsAmount;
	
	public BigDecimal getPointsAmount() {
		return pointsAmount;
	}

	public void setPointsAmount(BigDecimal pointsAmount) {
		this.pointsAmount = pointsAmount;
	}
	
	/**
	 *扣除积分的金额
	 */
	  @JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal deductPointsAmount;
    
    
    public BigDecimal getDeductPointsAmount() {
		return deductPointsAmount;
	}
	public void setDeductPointsAmount(BigDecimal val) {
		this.deductPointsAmount = val;
	}
	@Override
	public String toString() {
		return "MallSaleCost ["
            + "id="+getId()+",seqId="+seqId+",balanceBillNo="+balanceBillNo+",mallNo="+mallNo+",shopNo="+shopNo+",status="+status+",priceType="+priceType+",rateValue="+rateValue+",accountDebit="+accountDebit+",billDebit="+billDebit+",type="+type+",raxRate="+raxRate+",settleSum="+settleSum+",profitAmount="+profitAmount+",settleMonth="+settleMonth+",settleStartDate="+settleStartDate+",settleEndDate="+settleEndDate 
            + "]";
	}
    
     /** auto generate end,don't modify */
}