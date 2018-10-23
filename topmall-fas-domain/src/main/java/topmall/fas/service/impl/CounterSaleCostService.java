package topmall.fas.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import topmall.fas.model.CounterSaleCost;
import topmall.fas.repository.CounterSaleCostRepository;
import topmall.fas.service.ICounterSaleCostService;
import topmall.framework.repository.IRepository;
import topmall.framework.service.BaseService;
import cn.mercury.basic.query.Query;

@Service
public class CounterSaleCostService extends BaseService<CounterSaleCost,String> implements  ICounterSaleCostService{
    @Autowired
    private CounterSaleCostRepository repository;
    
    protected IRepository<CounterSaleCost,String> getRepository(){
        return repository;
    }

	@Override
	public List<CounterSaleCost> selectCounterSaleDisco(Query query) {
		return repository.selectCounterSaleDisco(query.asMap());
	}

	@Override
	public void updateToUnsettled(String id) {
		repository.updateToUnsettled(id);
		
	}

	@Override
	public List<CounterSaleCost>  queryConditionSum(Query query) {
		return repository.queryConditionSum(query.asMap());
	}

	/**
	 * @see topmall.fas.service.ICounterSaleCostService#querySaleProfit(cn.mercury.basic.query.Query)
	 */
	@Override
	public BigDecimal querySaleProfit(Query query) {
		return repository.querySaleProfit(query.asMap());
	}

	@Override
	public List<CounterSaleCost> queryListGroupDivisionNo(Query query) {
		return repository.queryListGroupDivisionNo(query.asMap());
	}

	@Override
	public BigDecimal queryReduceDiffAmount(Query query) {
		return repository.queryReduceDiffAmount(query.asMap());
	}
	
	
    
}







