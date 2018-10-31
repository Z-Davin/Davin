/**
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
package topmall.fas.api;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import tomall.fas.api.IMallBalanceDateDtlApiService;
import topmall.fas.enums.StatusEnums;
import topmall.fas.manager.IMallBalanceDateDtlManager;
import topmall.fas.model.MallBalanceDateDtl;
import topmall.fas.model.ShopBalanceDateDtl;
import cn.mercury.basic.query.Q;
import cn.mercury.basic.query.Query;
import cn.mercury.exception.ApiServiceException;
import cn.mercury.manager.IManager;
import cn.mercury.service.api.AbstractApiService;
import cn.mercury.utils.DateUtil;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * 物业结算期明细接口实现
 * 
 * @author dai.j
 * @date 2018-3-20 上午11:10:27
 * @version 0.1.0 
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
@Service
public class MallBalanceDateDtlApiService extends AbstractApiService<MallBalanceDateDtl, String>
		implements IMallBalanceDateDtlApiService {

	@Autowired
	private IMallBalanceDateDtlManager manager;

	@Override
	public boolean splitBalanceDate(int splitType, Date splitDate, String shopNo, String mallNo, String bunkGroupNo,
			String contractBillNo) {
		Query query = Q
				.And(Q.LessThenEquals("settleStartDate", splitDate), Q.GreatThenEquals("settleEndDate", splitDate))
				.asQuery();
		query.where("shopNo", shopNo).and("mallNo", mallNo).and("bunkGroupNo", bunkGroupNo);
		MallBalanceDateDtl mallBalanceDateDtl = manager.findByParam(query);
		if (null == mallBalanceDateDtl) {

			// 如果没获取到结算期 就不拆分
			logger.error("卖场:" + shopNo + "-物业：" + mallNo + "-铺位组:" + bunkGroupNo + "日期:" + DateUtil.getDate(splitDate) + "未获取到有效的结算期明细");

		} else {
			// 如果需要拆分的结算期 不是 生效状态 那么就不拆分结算期
			if (StatusEnums.EFFECTIVE.getStatus() == mallBalanceDateDtl.getStatus()) {
				if (1 == splitType) {
					mallBalanceDateDtl.setRemark("合同:" + contractBillNo + "截断结算期");
					mallBalanceDateDtl.setSettleStartDate(splitDate);
					manager.update(mallBalanceDateDtl);
				} else if (2 == splitType) {

					if (splitDate.equals(mallBalanceDateDtl.getSettleEndDate())) {
						return true;
					}

					mallBalanceDateDtl.setRemark("合同:" + contractBillNo + "截断结算期");

					ShopBalanceDateDtl newShopBalanceDateDtl = new ShopBalanceDateDtl();
					BeanUtils.copyProperties(mallBalanceDateDtl, newShopBalanceDateDtl, new String[] { "id" });

					mallBalanceDateDtl.setSettleEndDate(splitDate);
					manager.update(mallBalanceDateDtl);

					newShopBalanceDateDtl.setSettleStartDate(DateUtil.addDate(splitDate, 1));
					manager.insert(mallBalanceDateDtl);
				} else if (3 == splitType) {
					mallBalanceDateDtl.setSettleEndDate(splitDate);
					mallBalanceDateDtl.setRemark("合同:" + contractBillNo + "截断结算期");
					manager.update(mallBalanceDateDtl);
				} else {
					logger.error("无效的拆分类型：" + splitType);
					throw new ApiServiceException("无效的拆分类型：" + splitType);
				}
			}
		}
		return true;
	}

	/**
	 * @see cn.mercury.service.api.AbstractApiService#getManager()
	 */
	@Override
	protected IManager<MallBalanceDateDtl, String> getManager() {
		return manager;
	}

}
