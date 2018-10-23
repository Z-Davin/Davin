/**
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
package topmall.fas.api;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import tomall.fas.api.IBillCounterBalanceApiSerivce;
import topmall.fas.dto.MendOrderDto;
import topmall.fas.manager.IBillCounterBalanceManager;
import topmall.fas.model.BillCounterBalance;
import topmall.fas.util.CommonUtil;
import cn.mercury.basic.query.Q;
import cn.mercury.basic.query.Query;
import cn.mercury.exception.ApiServiceException;
import cn.mercury.manager.IManager;
import cn.mercury.service.api.AbstractApiService;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * 结算单对外接口实现
 * 
 * @author dai.j
 * @date 2017-9-9 上午10:38:31
 * @version 0.1.0
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
@Service
public class BillCounterBalanceApiSerivce extends AbstractApiService<BillCounterBalance, String> implements
		IBillCounterBalanceApiSerivce {

	@Autowired
	private IBillCounterBalanceManager manager;

	/**
	 * 
	 * @see tomall.fas.api.IBillCounterBalanceApiSerivce#isBalance(java.lang.String,
	 *      java.lang.String, java.util.List)
	 */
	@Override
	public Map<Date, Boolean> isBalance(String shopNo, String counterNo,
			List<Date> queryDateList) {
		Map<Date, Boolean> resultMap = new HashMap<>();
		Query query = Q.where("shopNo", shopNo).and("counterNo", counterNo);
		for (Date date : queryDateList) {
			query.and("saleDate", date);
			int count = manager.selectCount(query);
			if (count > 0) {
				resultMap.put(date, true);
			} else {
				resultMap.put(date, false);
			}
		}
		return resultMap;
	}

	/**
	 * @see cn.mercury.service.api.AbstractApiService#getManager()
	 */
	@Override
	protected IManager<BillCounterBalance, String> getManager() {
		return manager;
	}

	@Override
	public Date getLastCounterBalanceDate(MendOrderDto mendOrderDto) {
		if(null==mendOrderDto){
			throw new ApiServiceException("传入对象为空,请检查...");
		}
		if (!CommonUtil.hasValue(mendOrderDto.getShopNo())) {
			throw new ApiServiceException("门店参数为空,请检查...");
		}
		if (!CommonUtil.hasValue(mendOrderDto.getCounterNo())) {
			throw new ApiServiceException("专柜参数为空,请检查...");
		}
		if(null==mendOrderDto.getStartDate()){
			throw new ApiServiceException("开始时间参数为空,请检查...");
		}
		if(null==mendOrderDto.getEndDate()){
			mendOrderDto.setEndDate(mendOrderDto.getStartDate());
		}
		Query q=Q.where("shopNo", mendOrderDto.getShopNo()).and("counterNo", mendOrderDto.getCounterNo());
		BillCounterBalance counterBalance = manager.getLastCounterBalanceDate(q);
		if(null!=counterBalance){
			return counterBalance.getSettleEndDate();
		}
		return null;
	}

}
