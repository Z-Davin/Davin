/**
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
package topmall.fas.api;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import tomall.fas.api.IShopBalanceDateDtlApiService;
import topmall.fas.enums.StatusEnums;
import topmall.fas.manager.IShopBalanceDateDtlManager;
import topmall.fas.model.ShopBalanceDateDtl;
import cn.mercury.basic.query.Q;
import cn.mercury.basic.query.Query;
import cn.mercury.exception.ApiServiceException;
import cn.mercury.manager.IManager;
import cn.mercury.service.api.AbstractApiService;
import cn.mercury.utils.DateUtil;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * 财务结算期明细接口实现
 * 
 * @author dai.j
 * @date 2018-1-16 下午5:09:25
 * @version 0.1.0 
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
@Service
public class ShopBalanceDateDtlApiService extends AbstractApiService<ShopBalanceDateDtl, String> implements
		IShopBalanceDateDtlApiService {

	@Autowired
	private IShopBalanceDateDtlManager manager;

	/**
	 * @see cn.mercury.service.api.AbstractApiService#getManager()
	 */
	@Override
	protected IManager<ShopBalanceDateDtl, String> getManager() {
		return manager;
	}

	/**
	 * @see tomall.fas.api.IShopBalanceDateDtlApiService#splitBalanceDate(int, java.util.Date, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean splitBalanceDate(int splitType, Date splitDate, String counterNo, String contractBillNo) {
		Query query = Q.And(Q.LessThenEquals("settleStartDate", splitDate),
				Q.GreatThenEquals("settleEndDate", splitDate)).asQuery();
		query.where("counterNo", counterNo);
		//.and("status", StatusEnums.EFFECTIVE.getStatus());
		ShopBalanceDateDtl shopBalanceDateDtl = manager.findByParam(query);
		if (null == shopBalanceDateDtl) {
			
			// 如果没获取到结算期 就不拆分
			logger.error("专柜编码:" + counterNo + "日期:" + DateUtil.getDate(splitDate) + "未获取到生效状态的结算期明细");
			
		} else {
			
			// 如果需要拆分的结算期 不是 生效状态 那么就不拆分结算期
			if(StatusEnums.EFFECTIVE.getStatus() == shopBalanceDateDtl.getStatus()){
				if (1 == splitType) {
					shopBalanceDateDtl.setRemark("合同:" + contractBillNo + "截断结算期");
					shopBalanceDateDtl.setSettleStartDate(splitDate);
					manager.update(shopBalanceDateDtl);
				} else if (2 == splitType) {
					
					if (splitDate.equals(shopBalanceDateDtl.getSettleEndDate())) {
						return true;
					}
					
					shopBalanceDateDtl.setRemark("合同:" + contractBillNo + "截断结算期");

					ShopBalanceDateDtl newShopBalanceDateDtl = new ShopBalanceDateDtl();
					BeanUtils.copyProperties(shopBalanceDateDtl, newShopBalanceDateDtl, new String[] { "id" });

					shopBalanceDateDtl.setSettleEndDate(splitDate);
					manager.update(shopBalanceDateDtl);

					newShopBalanceDateDtl.setSettleStartDate(DateUtil.addDate(splitDate, 1));
					manager.insert(newShopBalanceDateDtl);
				} else if (3 == splitType) {
					shopBalanceDateDtl.setSettleEndDate(splitDate);
					shopBalanceDateDtl.setRemark("合同:" + contractBillNo + "截断结算期");
					manager.update(shopBalanceDateDtl);
				} else {
					logger.error("无效的拆分类型：" + splitType);
					throw new ApiServiceException("无效的拆分类型：" + splitType);
				}
			}
		}
		return true;
	}
}
