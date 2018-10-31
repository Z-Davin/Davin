/**
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
package topmall.fas.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *  额度阶梯对象（财务使用阶梯扣率的时候使用）
 * 
 * @author dai.j
 * @date 2017-8-18 下午2:48:09
 * @version 0.1.0 
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
public class QuotaStepDTO implements Serializable{

	/**
	 * 序列化
	 */
	private static final long serialVersionUID = -6075279108060939775L;
	
	/**
	 * id值
	 */
	private String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 起始值
	 */
	private BigDecimal startValue;
	public BigDecimal getStartValue() {
		return startValue;
	}
	public void setStartValue(BigDecimal startValue) {
		this.startValue = startValue;
	}
	
	/**
	 * 结束值
	 */
	private BigDecimal endValue;
	public BigDecimal getEndValue() {
		return endValue;
	}
	public void setEndValue(BigDecimal endValue) {
		this.endValue = endValue;
	}
	
	/**
	 * 1：费率（%） 2：固定额
	 */
	private Integer valueType;
	public Integer getValueType() {
		return valueType;
	}
	public void setValueType(Integer valueType) {
		this.valueType = valueType;
	}

	/**
	 * 扣率/费率
	 */
	private BigDecimal discountRate;
	public BigDecimal getDiscountRate() {
		return discountRate;
	}
	public void setDiscountRate(BigDecimal discountRate) {
		this.discountRate = discountRate;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "QuotaStepDTO [id=" + id + ", startValue=" + startValue + ", endValue=" + endValue + ", valueType="
				+ valueType + ", discountRate=" + discountRate + "]";
	}
}
