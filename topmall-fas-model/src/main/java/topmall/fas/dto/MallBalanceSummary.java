package topmall.fas.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class MallBalanceSummary implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6630072468020018342L;
	
	/**
	 * 项目
	 */
	private String projectName;
	
	/**
	 * 明细
	 */
	private String detail;
	
	/**
	 * 本期金额
	 */
	private BigDecimal curAmount;
	
	/**
	 * 物业报数
	 */
	private BigDecimal mallAmount;
	
	/**
	 * 差异金额
	 */
	private BigDecimal diffAmount;
	
	/**
	 * 差异原因
	 */
	private String diffReason;

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public BigDecimal getCurAmount() {
		return curAmount;
	}

	public void setCurAmount(BigDecimal curAmount) {
		this.curAmount = curAmount;
	}

	public BigDecimal getMallAmount() {
		return mallAmount;
	}

	public void setMallAmount(BigDecimal mallAmount) {
		this.mallAmount = mallAmount;
	}

	public BigDecimal getDiffAmount() {
		return diffAmount;
	}

	public void setDiffAmount(BigDecimal diffAmount) {
		this.diffAmount = diffAmount;
	}

	public String getDiffReason() {
		return diffReason;
	}

	public void setDiffReason(String diffReason) {
		this.diffReason = diffReason;
	}

}
