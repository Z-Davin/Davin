/**
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
package tomall.fas.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

import topmall.fas.dto.MendOrderDto;
import topmall.fas.model.BillCounterBalance;
import cn.mercury.service.api.IApiService;

/**
 * 结算单对外接口
 * 
 * @author dai.j
 * @date 2017-9-9 上午10:29:30
 * @version 0.1.0 
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
public interface IBillCounterBalanceApiSerivce extends IApiService<BillCounterBalance, String> {
	
	/**
	 * 查询是否专柜已结算
	 * @param shopNo 店铺编码
	 * @param counterNo  专柜编码
	 * @param queryDateList  查询日期的列表
	 * @return 日期是否已结算列表 放入Map对象
	 */
	Map<Date, Boolean> isBalance(String shopNo, String counterNo, List<Date> queryDateList);
	
	
	/** 获取专柜的最后结算期
	 * @param mendOrderDto(门店,专柜,日期必传)
	 * @return
	 */
	@Deprecated
	Date getLastCounterBalanceDate(MendOrderDto mendOrderDto);
	
	

}
