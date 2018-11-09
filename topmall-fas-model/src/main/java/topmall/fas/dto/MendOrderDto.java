package topmall.fas.dto;

import java.io.Serializable;
import java.util.Date;

import cn.mercury.utils.JsonDateDeserializer$10;
import cn.mercury.utils.JsonDateSerializer$10;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class MendOrderDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6967291491524657211L;
	/**
	 * 门店
	 */
	private String shopNo;
	/**
	 * 专柜
	 */
	private String counterNo;
	/**
	 * 开始时间,如果只有一天则只传开始时间
	 */
	@JsonSerialize(using = JsonDateSerializer$10.class)
	@JsonDeserialize(using = JsonDateDeserializer$10.class)
	private Date startDate;
	/**
	 * 结束时间
	 */
	 @JsonSerialize(using = JsonDateSerializer$10.class)
	 @JsonDeserialize(using = JsonDateDeserializer$10.class)
	private Date endDate;
	public String getShopNo() {
		return shopNo;
	}
	public void setShopNo(String shopNo) {
		this.shopNo = shopNo;
	}
	public String getCounterNo() {
		return counterNo;
	}
	public void setCounterNo(String counterNo) {
		this.counterNo = counterNo;
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
	 
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MendOrderDto [shopNo=").append(shopNo).append(", counterNo=").append(counterNo).append(", startDate=")
				.append(startDate).append(", endDate=").append(endDate).append("]");
		return builder.toString();
	}

}
