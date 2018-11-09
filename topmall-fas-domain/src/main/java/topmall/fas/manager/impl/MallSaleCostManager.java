package topmall.fas.manager.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import topmall.fas.manager.IMallSaleCostManager;
import topmall.fas.model.MallSaleCost;
import topmall.fas.service.IMallSaleCostService;
import topmall.framework.manager.BaseManager;
import topmall.framework.service.IService;
import cn.mercury.basic.query.Query;


@Service
public class MallSaleCostManager extends BaseManager<MallSaleCost,String> implements IMallSaleCostManager{
    @Autowired
    private IMallSaleCostService service;
    
    protected IService<MallSaleCost,String> getService(){
        return service;
    }

	/**
	 * @see topmall.fas.manager.IMallSaleCostManager#queryMallSaleProfit(cn.mercury.basic.query.Query)
	 */
	@Override
	public BigDecimal queryMallSaleProfit(Query query) {
		return service.queryMallSaleProfit(query);
	}

	/** 
	 * @see topmall.fas.manager.IMallSaleCostManager#queryConditionSum(cn.mercury.basic.query.Query)
	 */
	@Override
	public List<MallSaleCost> queryConditionSum(Query q) {
		return service.queryConditionSum(q);
	}
    
}