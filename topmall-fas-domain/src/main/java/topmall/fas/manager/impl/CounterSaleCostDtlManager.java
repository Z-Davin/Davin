package topmall.fas.manager.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import topmall.fas.manager.ICounterSaleCostDtlManager;
import topmall.fas.model.CounterSaleCostDtl;
import topmall.fas.service.ICounterSaleCostDtlService;
import topmall.framework.manager.BaseManager;
import topmall.framework.service.IService;
import cn.mercury.basic.query.Query;

@Service
public class CounterSaleCostDtlManager extends BaseManager<CounterSaleCostDtl, String> implements ICounterSaleCostDtlManager {
	@Autowired
	private ICounterSaleCostDtlService service;

	@Autowired
	protected IService<CounterSaleCostDtl, String> getService() {
		return service;
	}

	/**
	 * @see topmall.fas.manager.ICounterSaleCostDtlManager#querySaleProfit(cn.mercury.basic.query.Query)
	 */
	@Override
	public BigDecimal querySaleProfit(Query query) {
		return service.querySaleProfit(query);
	}
}
