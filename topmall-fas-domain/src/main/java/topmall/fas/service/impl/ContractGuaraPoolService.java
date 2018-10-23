package topmall.fas.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import topmall.fas.model.ContractGuaraPool;
import topmall.fas.repository.ContractGuaraPoolRepository;
import topmall.fas.service.IContractGuaraPoolService;
import topmall.framework.repository.IRepository;
import topmall.framework.service.BaseService;
import cn.mercury.basic.query.Query;

@Service
public class ContractGuaraPoolService extends BaseService<ContractGuaraPool,String> implements  IContractGuaraPoolService{
    @Autowired
    private ContractGuaraPoolRepository repository;
    
    protected IRepository<ContractGuaraPool,String> getRepository(){
        return repository;
    }

	/**
	 * @see topmall.fas.service.IContractGuaraPoolService#selectValidGuara(cn.mercury.basic.query.Query)
	 */
	@Override
	public List<ContractGuaraPool> selectValidGuara(Query query) {
		return repository.selectValidGuara(query.asMap());
	}
    
}







