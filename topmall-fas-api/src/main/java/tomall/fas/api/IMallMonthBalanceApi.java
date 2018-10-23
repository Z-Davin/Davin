/**
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
package tomall.fas.api;

import java.util.Date;

/**
 * 物业月结接口
 * 
 * @author dai.j
 * @date 2018-3-19 下午4:34:00
 * @version 0.1.0 
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
public interface IMallMonthBalanceApi {
	
	/**
	 * 查询物业的月结日期
	 * @param shopNo 卖场编码
	 * @param mallNo 物业编码
	 * @param bunkGroupNo 铺位组编码
	 * @return 月结日期
	 */
	Date getLastMallBalanceDate(String shopNo, String mallNo, String bunkGroupNo);
}
