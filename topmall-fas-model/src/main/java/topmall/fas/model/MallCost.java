package topmall.fas.model;

import java.math.BigDecimal;
import java.util.Date;

import cn.mercury.utils.BigDecimalSerializer$2;
import cn.mercury.utils.JsonDateDeserializer$10;
import cn.mercury.utils.JsonDateDeserializer$19;
import cn.mercury.utils.JsonDateSerializer$10;
import cn.mercury.utils.JsonDateSerializer$19;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 卖场费用表
 **/
public class MallCost extends cn.mercury.domain.BaseEntity<String> {
	/** auto generate start ,don't modify */
	private static final long serialVersionUID = 636437667271145650L;

	/**
	 * 系统生成费用的合同条款id
	 **/
	private String refId;

	public String getRefId() {
		return refId;
	}

	public void setRefId(String val) {
		refId = val;
	}
	
	 /**
     * 来源类型：0:手动添加，1：租金条款，2：抽成条款，3：保底条款，4：其他条款
     */
    private Short refType;
	 public Short getRefType() {
		 return refType;
	 }
	 public void setRefType(Short refType) {
		 this.refType = refType;
	 }

	/**
	 * 结算单编码
	 **/
	private String balanceBillNo;

	public String getBalanceBillNo() {
		return balanceBillNo;
	}

	public void setBalanceBillNo(String val) {
		balanceBillNo = val;
	}

	/**
	 * 序列号
	 **/
	private String seqId;

	public String getSeqId() {
		return seqId;
	}

	public void setSeqId(String val) {
		seqId = val;
	}

	/**
	 * 结算公司编码
	 **/
	private String companyNo;

	public String getCompanyNo() {
		return companyNo;
	}

	public void setCompanyNo(String val) {
		companyNo = val;
	}

	/**
	 * 销售门店编号
	 **/
	private String shopNo;

	public String getShopNo() {
		return shopNo;
	}

	public void setShopNo(String val) {
		shopNo = val;
	}

	/**
	 * 物业编码
	 **/
	private String mallNo;

	public String getMallNo() {
		return mallNo;
	}

	public void setMallNo(String val) {
		mallNo = val;
	}

	/**
	 * 扣费项目编码
	 **/
	private String costNo;

	public String getCostNo() {
		return costNo;
	}

	public void setCostNo(String val) {
		costNo = val;
	}

	/**
	 * 结算月
	 **/
	private String settleMonth;

	public String getSettleMonth() {
		return settleMonth;
	}

	public void setSettleMonth(String val) {
		settleMonth = val;
	}

	/**
	 * 结算开始时间
	 **/
	@JsonSerialize(using = JsonDateSerializer$10.class)
	@JsonDeserialize(using = JsonDateDeserializer$10.class)
	private Date settleStartDate;

	public Date getSettleStartDate() {
		return settleStartDate;
	}

	public void setSettleStartDate(Date val) {
		settleStartDate = val;
	}

	/**
	 * 结算结束时间
	 **/
	@JsonSerialize(using = JsonDateSerializer$10.class)
	@JsonDeserialize(using = JsonDateDeserializer$10.class)
	private Date settleEndDate;

	public Date getSettleEndDate() {
		return settleEndDate;
	}

	public void setSettleEndDate(Date val) {
		settleEndDate = val;
	}

	/**
	 * 状态 0制单
	 **/
	private Integer status;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer val) {
		status = val;
	}

	/**
	 * 应结价款
	 **/
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal ableAmount;

	public BigDecimal getAbleAmount() {
		return ableAmount;
	}

	public void setAbleAmount(BigDecimal val) {
		ableAmount = val;
	}

	/**
	 * 应结总额
	 **/
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal ableSum;

	public BigDecimal getAbleSum() {
		return ableSum;
	}

	public void setAbleSum(BigDecimal val) {
		ableSum = val;
	}

	/**
	 * 税额
	 **/
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal taxAmount;

	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(BigDecimal val) {
		taxAmount = val;
	}

	/**
	 * 是否含税(0:不含税;1:含税)
	 **/
	private Short taxFlag;

	public Short getTaxFlag() {
		return taxFlag;
	}

	public void setTaxFlag(Short val) {
		taxFlag = val;
	}

	/**
	 * 税率
	 **/
	private BigDecimal taxRate;

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal val) {
		taxRate = val;
	}

	/**
	 * 票扣标识
	 **/
	private Integer billDebit;

	public Integer getBillDebit() {
		return billDebit;
	}

	public void setBillDebit(Integer val) {
		billDebit = val;
	}

	/**
	 * 账扣标识
	 **/
	private Integer accountDebit;

	public Integer getAccountDebit() {
		return accountDebit;
	}

	public void setAccountDebit(Integer val) {
		accountDebit = val;
	}

	/**
	 * 确认人姓名
	 **/
	private String auditor;

	public String getAuditor() {
		return auditor;
	}

	public void setAuditor(String val) {
		auditor = val;
	}

	/**
	 * 确认时间
	 **/
	@JsonSerialize(using = JsonDateSerializer$19.class)
	@JsonDeserialize(using = JsonDateDeserializer$19.class)
	private Date auditTime;

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date val) {
		auditTime = val;
	}

	/**
	 * 备注
	 **/
	private String remark;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String val) {
		remark = val;
	}

	/**
	 * 生成方式(1系统生产, 2店铺录入,3手工提交)
	 **/
	private Short source;

	public Short getSource() {
		return source;
	}

	public void setSource(Short val) {
		source = val;
	}

	/**
	 * 物业报数
	 */
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
		return diffAmount  ==null?new BigDecimal(0):diffAmount;
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
		return adjustAmount ==null?new BigDecimal(0):adjustAmount;
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
		return preDiffAmount;
	}

	public void setPreDiffAmount(BigDecimal preDiffAmount) {
		this.preDiffAmount = preDiffAmount;
	}


	/**
	 * 本期差异金额
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
		return backAmount  ==null?new BigDecimal(0):backAmount;
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

	@Override
	public String toString() {
		return "MallCost [" + "id=" + getId() + ",refId=" + refId  + ",refType=" + refType + ",balanceBillNo=" + balanceBillNo + ",seqId=" + seqId + ",companyNo=" + companyNo + ",shopNo=" + shopNo + ",mallNo=" + mallNo + ",costNo=" + costNo + ",settleMonth=" + settleMonth
				+ ",settleStartDate=" + settleStartDate + ",settleEndDate=" + settleEndDate + ",status=" + status + ",ableAmount=" + ableAmount + ",ableSum=" + ableSum + ",taxAmount=" + taxAmount + ",taxFlag=" + taxFlag + ",taxRate=" + taxRate
				+ ",billDebit=" + billDebit + ",accountDebit=" + accountDebit + ",auditor=" + auditor + ",auditTime=" + auditTime + ",remark=" + remark + ",source=" + source + "]";
	}
	/** auto generate end,don't modify */
}