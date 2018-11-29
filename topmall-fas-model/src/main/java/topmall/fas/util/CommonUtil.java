/**
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
package topmall.fas.util;

import java.math.BigDecimal;
import java.util.List;
import topmall.fas.dto.QuotaStepDTO;

/**
 * 公用的工具类
 * 
 * @author dai.j
 * @date 2017-8-15 下午3:55:22
 * @version 0.1.0 
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
public class CommonUtil {

	public static BigDecimal ONE_HUNDRED = new BigDecimal(100);

	/**
	 * 判断list是否有值
	 * @param list
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean hasValue(List list) {
		return ((null != list) && (list.size() > 0));
	}

	/**
	 * 判断String是否为空
	 * @param s String 对象
	 * @return 是否为空
	 */
	public static boolean hasValue(String s) {
		return (s != null) && (s.trim().length() > 0);
	}

	/**
	 * 根据应结价款和税率获取应结总额 公式：应结总额 (含税金额)= 应结价款(不含税金额)*(100+税率)/100 
	 * @param taxFreeCost 不含税金额
	 * @param taxRate 税率 （不含百分号的数）
	 * @return 含税金额 (保留两位小数)
	 */
	public static BigDecimal getTaxCost(BigDecimal taxFreeCost, BigDecimal taxRate) {
		return taxFreeCost.multiply(ONE_HUNDRED.add(taxRate)).divide(ONE_HUNDRED, 2, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 根据应结总额和税率获应结价款   公式： 应结价款(不含税金额) = 应结总额(含税金额)/(100+税率)*100
	 * @param taxCost 含税金额
	 * @param taxRate 税率 （不含百分号的数）
	 * @return 不含税金额  (保留两位小数)
	 */
	public static BigDecimal getTaxFreeCost(BigDecimal taxCost, BigDecimal taxRate) {
		return taxCost.multiply(ONE_HUNDRED).divide(ONE_HUNDRED.add(taxRate), 2, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 根据额度和额度阶梯扣来获取扣率结果值
	 * @param quotaStepList 额度阶梯 （阶梯必须是有序 从低到高 而且起始值都是从0开始而且连续）
	 * @param initCost 初始额度
	 * @param uniformDiscountFlag 是否统一扣率
	 * @return 扣率结果
	 */
	public static BigDecimal getQuatoStepCost(List<QuotaStepDTO> quotaStepList, BigDecimal initCost,
			boolean uniformDiscountFlag) {
		BigDecimal mathResult = new BigDecimal(0);

		// 如果不是统一的扣率
		if (uniformDiscountFlag) {
			BigDecimal startValue = new BigDecimal(0);
			// 循环额度阶梯对象，对每一级的阶梯进行计算
			for (QuotaStepDTO quotaStepDTO : quotaStepList) {
				//如果阶梯的开始值是负数,这取0
				if(quotaStepDTO.getStartValue().compareTo(new BigDecimal(0))>0){
					startValue = quotaStepDTO.getStartValue();
				}
				// 如果额度比阶梯对象的结束值大，那么计算值就直接使用这个阶梯的最大值计算
				if (initCost.compareTo(quotaStepDTO.getEndValue()) > 0) {
					// 1：费率（%） 2：固定额
					if(null == quotaStepDTO.getValueType() || 1 == quotaStepDTO.getValueType()) {
						mathResult = mathResult.add((quotaStepDTO.getEndValue().subtract(startValue)).multiply(quotaStepDTO.getDiscountRate())
								.divide(ONE_HUNDRED, 2, BigDecimal.ROUND_HALF_UP)); 
					} else {
						mathResult = mathResult.add(quotaStepDTO.getDiscountRate());
					}
					continue;
				} else { // 如果额度比阶梯对象的结束值小， 那么就使用临时的结果值做计算
					
					// 1：费率（%） 2：固定额
					if(null == quotaStepDTO.getValueType() || 1 == quotaStepDTO.getValueType()) {
						mathResult = mathResult.add((initCost.subtract(startValue)).multiply(quotaStepDTO.getDiscountRate()).divide(ONE_HUNDRED,
								2, BigDecimal.ROUND_HALF_UP));
					} else {
						mathResult = mathResult.add(quotaStepDTO.getDiscountRate());
					}
					break;
				}
			}
		} else { //如果是统一的扣率,那就只需要找到适合扣率的阶梯运算一次 就可以跳出循环了
			for (QuotaStepDTO quotaStepDTO : quotaStepList) {
				if (initCost.compareTo(quotaStepDTO.getStartValue()) > 0
						&& initCost.compareTo(quotaStepDTO.getEndValue()) <= 0) {
					
					// 1：费率（%） 2：固定额
					if(1 == quotaStepDTO.getValueType()) {
						mathResult = initCost.multiply(quotaStepDTO.getDiscountRate()).divide(ONE_HUNDRED, 2,
								BigDecimal.ROUND_HALF_UP);
					} else {
						mathResult = mathResult.add(quotaStepDTO.getDiscountRate());
					} 
					break;
				} else {
					continue;
				}
			}
		}
		return mathResult;
	}
	
	/**
	 * 根据折扣阶梯扣 获取扣率值
	 * @param quotaStepList 折扣阶梯扣
	 * @param initCost 用来计算扣率的金额
	 * @param initDicount 用来比较阶梯扣的折扣
	 * @return 扣率金额
	 */
	public static BigDecimal getDicountStepCost(List<QuotaStepDTO> quotaStepList, BigDecimal initCost,
			BigDecimal initDicount) {
		BigDecimal mathResult = new BigDecimal(0);
		for (QuotaStepDTO quotaStepDTO : quotaStepList) {
			if (initDicount.compareTo(quotaStepDTO.getStartValue()) > 0
					&& initDicount.compareTo(quotaStepDTO.getEndValue()) <= 0) {
				mathResult = initCost.multiply(quotaStepDTO.getDiscountRate()).divide(ONE_HUNDRED, 2,
						BigDecimal.ROUND_HALF_UP);
				break;
			} else {
				continue;
			}
		}
		return mathResult;
	}
}
