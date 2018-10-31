/**
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
package tomall.fas.api;

import java.util.Date;

import topmall.fas.model.ShopBalanceDateDtl;
import cn.mercury.service.api.IApiService;

/**
 * 财务结算期明细接口
 * @author dai.j
 * @date 2018-1-16 下午4:57:40
 * @version 0.1.0 
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
public interface IShopBalanceDateDtlApiService extends IApiService<ShopBalanceDateDtl, String> {
	
	/**
	 * 拆分结算期明细
	 * @param splitType 拆分类型 1：从头开始截断; 2：从结算期中间拆开两段; 3: 从尾开始截断
	 * @param splitDate 拆分的日期
	 * @param counterNo 专柜编码
	 * @param contractBillNo 合同号
	 * @return 拆分是否成功
	 */
	boolean splitBalanceDate(int splitType, Date splitDate, String counterNo, String contractBillNo);
}
