package tomall.fas.api;

import java.util.Date;



/** 店铺月结api
 * @author zengxa
 *
 */
public interface IShopMonthBalanceApi {
	
	/** 
	 * @param shopNo 
	 * @return 店铺月结的最后时间(如果没有月结则为空)
	 */
	Date getLastShopBalanceDate(String shopNo);

}
