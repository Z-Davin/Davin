package topmall.fas.service.impl;

import org.springframework.stereotype.Service;

import cn.mercury.basic.query.Query;
import topmall.fas.model.MallCost;
import topmall.fas.repository.MallCostRepository;

import topmall.fas.service.IMallCostService;
import topmall.framework.repository.IRepository;
import topmall.framework.service.BaseService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class MallCostService extends BaseService<MallCost,String> implements  IMallCostService{
    @Autowired
    private MallCostRepository repository;
    
    protected IRepository<MallCost,String> getRepository(){
        return repository;
    }

	@Override
	public void updateToUnsettled(String id) {
		repository.updateToUnsettled(id);
	}

	@Override
	public List<MallCost> getCostGroupAccountDebit(Query query) {
		return repository.getCostGroupAccountDebit(query.asMap());
	}
	
	@Override
	public Integer updateStatus(Query query) {
		return repository.updateStatus(query.asMap());
	}

	@Override
	public List<MallCost> queryConditionSum(Query query) {
		return repository.queryConditionSum(query.asMap());
	}
    
}







