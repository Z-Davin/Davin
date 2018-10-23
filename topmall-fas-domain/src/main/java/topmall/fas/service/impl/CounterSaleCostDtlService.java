package topmall.fas.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import topmall.fas.model.CounterSaleCost;
import topmall.fas.model.CounterSaleCostDtl;
import topmall.fas.repository.CounterSaleCostDtlRepository;
import topmall.fas.service.ICounterSaleCostDtlService;
import topmall.framework.repository.IRepository;
import topmall.framework.service.BaseService;
import cn.mercury.basic.query.Query;

@Service
public class CounterSaleCostDtlService extends BaseService<CounterSaleCostDtl,String> implements  ICounterSaleCostDtlService{
    @Autowired
    private CounterSaleCostDtlRepository repository;
    
    protected IRepository<CounterSaleCostDtl,String> getRepository(){
        return repository;
    }

	@Override
	public List<CounterSaleCostDtl> selectRecalculateDtlHisList(Query query) {
		return repository.selectRecalculateDtlHisList(query.asMap());
	}

	@Override
	public List<CounterSaleCost> selectRecalculateHisList(Query query) {
		return repository.selectRecalculateHisList(query.asMap());
	}

	/**
	 * @see topmall.fas.service.ICounterSaleCostDtlService#querySaleProfit(cn.mercury.basic.query.Query)
	 */
	@Override
	public BigDecimal querySaleProfit(Query query) {
		return repository.querySaleProfit(query.asMap());
	}
}







