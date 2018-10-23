package topmall.fas.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import topmall.fas.model.ContractRentPool;
import topmall.fas.repository.ContractRentPoolRepository;
import topmall.fas.service.IContractRentPoolService;
import topmall.framework.repository.IRepository;
import topmall.framework.service.BaseService;
import cn.mercury.basic.query.Query;

@Service
public class ContractRentPoolService extends BaseService<ContractRentPool,String> implements  IContractRentPoolService{
    @Autowired
    private ContractRentPoolRepository repository;
    
    protected IRepository<ContractRentPool,String> getRepository(){
        return repository;
    }

	/**
	 * @see topmall.fas.service.IContractRentPoolService#selectValidRent(cn.mercury.basic.query.Query)
	 */
	@Override
	public List<ContractRentPool> selectValidRent(Query query) {
		return repository.selectValidRent(query.asMap());
	}
    
}







