package topmall.fas.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import topmall.fas.domain.handler.GuaraCalculateHandler;
import topmall.fas.manager.IContractGuaraPoolManager;
import topmall.fas.model.ContractGuaraPool;
import topmall.fas.model.CounterCost;
import topmall.fas.model.ShopBalanceDateDtl;
import topmall.fas.service.IContractGuaraPoolService;
import topmall.framework.manager.BaseManager;
import topmall.framework.service.IService;
import cn.mercury.basic.query.Query;

@Service
public class ContractGuaraPoolManager extends BaseManager<ContractGuaraPool, String> implements
		IContractGuaraPoolManager {
	@Autowired
	private IContractGuaraPoolService service;

	protected IService<ContractGuaraPool, String> getService() {
		return service;
	}

	/**
	 * @see topmall.fas.manager.IContractGuaraPoolManager#selectValidGuara(cn.mercury.basic.query.Query)
	 */
	@Override
	public List<ContractGuaraPool> selectValidGuara(Query query) {
		return service.selectValidGuara(query);
	}

	/**
	 * @see topmall.fas.manager.IContractGuaraPoolManager#createGuaraCost(topmall.fas.model.ShopBalanceDate)
	 */
	@Override
	public List<CounterCost> createGuaraCost(ShopBalanceDateDtl shopBalanceDateDtl) {
		GuaraCalculateHandler guaraCalculateHandler =  new  GuaraCalculateHandler(shopBalanceDateDtl, this);
		return guaraCalculateHandler.calculateCost(false);
	}
	
}
