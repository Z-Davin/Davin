package topmall.fas.manager.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import topmall.fas.enums.StatusEnums;
import topmall.fas.manager.IShopCostManager;
import topmall.fas.model.ShopBalanceDateDtl;
import topmall.fas.model.ShopCost;
import topmall.fas.service.IShopCostService;
import topmall.fas.util.PublicConstans;
import topmall.framework.core.CodingRuleHelper;
import topmall.framework.manager.BaseManager;
import topmall.framework.security.Authorization;
import topmall.framework.service.IService;
import cn.mercury.basic.query.Q;
import cn.mercury.basic.query.Query;
import cn.mercury.manager.ManagerException;

@Service
public class ShopCostManager extends BaseManager<ShopCost, String> implements IShopCostManager {
	@Autowired
	private IShopCostService service;

	protected IService<ShopCost, String> getService() {
		return service;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public Integer insert(ShopCost entry) throws ManagerException {
		if (entry.getStatus() == null) {
			entry.setStatus(StatusEnums.INEFFECTIVE.getStatus());
		}
		if (null == entry.getSeqId()) {
			String orderNo = entry.getShopNo()
					+ CodingRuleHelper.getBasicCoding(PublicConstans.COUNTER_COST_NO, entry.getShopNo(), null);
			entry.setSeqId(orderNo);
		}
		return super.insert(entry);

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public ShopCost confirm(String id) {
		ShopCost shopCost = service.findByPrimaryKey(id);
		shopCost.setAuditor(Authorization.getUser().getName());
		shopCost.setAuditTime(new Date());
		shopCost.setStatus(StatusEnums.EFFECTIVE.getStatus());
		service.update(shopCost);
		return shopCost;
	}

	/**
	 * @see topmall.fas.manager.IShopCostManager#queryByBalanceDate(topmall.fas.model.ShopBalanceDateDtl)
	 */
	@Override
	public List<ShopCost> queryByBalanceDate(ShopBalanceDateDtl shopBalanceDateDtl) {
		Query query = Q.where("shopNo", shopBalanceDateDtl.getShopNo())
				.and("counterNo", shopBalanceDateDtl.getCounterNo())
				.and("settleMonth", shopBalanceDateDtl.getSettleMonth())
				.and("settleStartDate", shopBalanceDateDtl.getSettleStartDate())
				.and("settleEndDate", shopBalanceDateDtl.getSettleEndDate())
				.and("status", StatusEnums.EFFECTIVE.getStatus());
		return service.selectGroupNum(query);
	}
}
