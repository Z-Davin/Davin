package topmall.fas.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import topmall.fas.domain.handler.RentCalculateHandler;
import topmall.fas.manager.IContractRentPoolManager;
import topmall.fas.model.ContractRentPool;
import topmall.fas.model.CounterCost;
import topmall.fas.model.ShopBalanceDateDtl;
import topmall.fas.service.IContractRentPoolService;
import topmall.framework.manager.BaseManager;
import topmall.framework.service.IService;
import cn.mercury.basic.query.Query;

/**
 * 财务结算期专柜合同租金池表
 * @author Administrator
 *
 */
@Service
public class ContractRentPoolManager extends BaseManager<ContractRentPool, String> implements IContractRentPoolManager {
	@Autowired
	private IContractRentPoolService service;

	protected IService<ContractRentPool, String> getService() {
		return service;
	}

	/**
	 * @see topmall.fas.manager.IContractRentPoolManager#selectValidRent(cn.mercury.basic.query.Query)
	 */
	@Override
	public List<ContractRentPool> selectValidRent(Query query) {
		return service.selectValidRent(query);
	}

	/**
	 * @see topmall.fas.manager.IContractRentPoolManager#createRentCost(topmall.fas.model.ShopBalanceDate)
	 */
	@Override
	public List<CounterCost> createRentCost(ShopBalanceDateDtl shopBalanceDateDtl) {
		RentCalculateHandler calculateHandler = new RentCalculateHandler(shopBalanceDateDtl, this);
		return calculateHandler.calculateCost(false);
	}
}
