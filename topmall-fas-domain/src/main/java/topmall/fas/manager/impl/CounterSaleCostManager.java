package topmall.fas.manager.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import topmall.fas.manager.ICounterSaleCostManager;
import topmall.fas.model.CounterSaleCost;
import topmall.fas.service.ICounterSaleCostService;
import topmall.framework.manager.BaseManager;
import topmall.framework.service.IService;
import cn.mercury.basic.query.Query;

@Service
public class CounterSaleCostManager extends BaseManager<CounterSaleCost, String> implements ICounterSaleCostManager {
	@Autowired
	private ICounterSaleCostService service;

	@Autowired
	protected IService<CounterSaleCost, String> getService() {
		return service;
	}

	@Override
	public List<CounterSaleCost>  queryConditionSum(Query query) {
		return service.queryConditionSum(query);
	}

	/**
	 * @see topmall.fas.manager.ICounterSaleCostManager#querySaleProfit(cn.mercury.basic.query.Query)
	 */
	@Override
	public BigDecimal querySaleProfit(Query query) {
		return service.querySaleProfit(query);
	}
}
