package topmall.fas.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import topmall.fas.model.CounterCost;
import topmall.fas.repository.CounterCostRepository;

import topmall.fas.service.ICounterCostService;
import topmall.framework.repository.IRepository;
import topmall.framework.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

import cn.mercury.basic.query.Query;

@Service
public class CounterCostService extends BaseService<CounterCost,String> implements  ICounterCostService{
    @Autowired
    private CounterCostRepository repository;
    
    protected IRepository<CounterCost,String> getRepository(){
        return repository;
    }

	@Override
	public void updateToUnsettled(String id) {
		repository.updateToUnsettled(id);
	}

	@Override
	public List<CounterCost> queryConditionSum(Query query) {
		return repository.queryConditionSum(query.asMap());
	}

	@Override
	public Integer updateStatus(Query query) {
		return repository.updateStatus(query.asMap());
	}
    
}







