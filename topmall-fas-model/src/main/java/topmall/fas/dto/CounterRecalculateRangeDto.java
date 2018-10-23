package topmall.fas.dto;

import java.io.Serializable;
import java.sql.Date;

public class CounterRecalculateRangeDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4105482508392901115L;
	/**
	 *  专柜编码
	 */
	private String counterNo;
	/*
	 * 部内编码
	 */
	private String divisionNo;
	/*
	 * 开始时间
	 */
	private Date startDate;
	/*
	 * 结束时间
	 */
	private Date endDate;
	
	public String getCounterNo() {
		return counterNo;
	}
	public void setCounterNo(String counterNo) {
		this.counterNo = counterNo;
	}
	public String getDivisionNo() {
		return divisionNo;
	}
	public void setDivisionNo(String divisionNo) {
		this.divisionNo = divisionNo;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	
}
