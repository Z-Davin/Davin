package topmall.fas.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import topmall.fas.domain.handler.OtherCalculateHandler;
import topmall.fas.dto.QuotaStepDTO;
import topmall.fas.manager.IContractOtherPoolManager;
import topmall.fas.model.ContractOtherPool;
import topmall.fas.model.CounterCost;
import topmall.fas.model.ShopBalanceDateDtl;
import topmall.fas.service.IContractOtherPoolService;
import topmall.framework.manager.BaseManager;
import topmall.framework.service.IService;
import cn.mercury.basic.query.Query;

@Service
public class ContractOtherPoolManager extends BaseManager<ContractOtherPool, String> implements
		IContractOtherPoolManager {
	@Autowired
	private IContractOtherPoolService service;
	
	protected IService<ContractOtherPool, String> getService() {
		return service;
	}

	/**
	 * @see topmall.fas.manager.IContractOtherPoolManager#selectValidOther(cn.mercury.basic.query.Query)
	 */
	@Override
	public List<ContractOtherPool> selectValidOther(Query query) {
		return service.selectValidOther(query);
	}

	/**
	 * @see topmall.fas.manager.IContractOtherPoolManager#createOtherCost(topmall.fas.model.ShopBalanceDate)
	 */
	@Override
	public List<CounterCost> createOtherCost(ShopBalanceDateDtl shopBalanceDateDtl) {
		OtherCalculateHandler calculateHandler = new OtherCalculateHandler(shopBalanceDateDtl, this);
		return calculateHandler.calculateCost(false);
	}

	/**
	 * @see topmall.fas.manager.IContractOtherPoolManager#selectGroupOtherData(cn.mercury.basic.query.Query)
	 */
	@Override
	public List<ContractOtherPool> selectGroupOtherData(Query query) {
		return service.selectGroupOtherData(query);
	}

	/**
	 * @see topmall.fas.manager.IContractOtherPoolManager#selectQuotaStep(cn.mercury.basic.query.Query)
	 */
	@Override
	public List<QuotaStepDTO> selectQuotaStep(Query query) {
		return service.selectQuotaStep(query);
	}

		

}
