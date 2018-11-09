/**
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
package topmall.fas.api;

import java.util.Date;

import javax.annotation.Resource;

import tomall.fas.api.IMallMonthBalanceApi;
import topmall.fas.manager.IMallBalanceDateManager;
import topmall.fas.model.MallBalanceDate;
import cn.mercury.basic.query.Q;
import cn.mercury.basic.query.Query;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * 物业月结
 * 
 * @author dai.j
 * @date 2018-3-19 下午4:37:07
 * @version 0.1.0 
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
@Service
public class MallMonthBalanceApi implements IMallMonthBalanceApi {

	@Resource
	private IMallBalanceDateManager mallBalanceDateManager;

	/**
	 * @see tomall.fas.api.IMallMonthBalanceApi#getLastMallBalanceDate(java.lang.String, java.lang.String)
	 */
	@Override
	public Date getLastMallBalanceDate(String shopNo, String mallNo, String bunkGroupNo) {
		Query query = Q.where("shopNo", shopNo).and("mallNo", mallNo).and("bunkGroupNo", bunkGroupNo);

		MallBalanceDate mallBalanceDate = mallBalanceDateManager.findByUnique(query);
		if (null != mallBalanceDate) {
			return mallBalanceDate.getCloseDate();
		} else {
			return null;
		}
	}
}
