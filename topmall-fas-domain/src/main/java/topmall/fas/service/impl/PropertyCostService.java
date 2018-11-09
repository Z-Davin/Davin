package topmall.fas.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import topmall.fas.model.PropertyCost;
import topmall.fas.repository.PropertyCostRepository;
import topmall.fas.service.IPropertyCostService;
import topmall.framework.repository.IRepository;
import topmall.framework.service.BaseService;
import cn.mercury.basic.query.Query;

@Service
public class PropertyCostService extends BaseService<PropertyCost,String> implements  IPropertyCostService{
    @Autowired
    private PropertyCostRepository repository;
    
    protected IRepository<PropertyCost,String> getRepository(){
        return repository;
    }

	/**
	 * @see topmall.fas.service.IPropertyCostService#selectGroupNum(cn.mercury.basic.query.Query)
	 */
	@Override
	public List<PropertyCost> selectGroupNum(Query query) {
		return repository.selectGroupNum(query.asMap());
	}
    
}







