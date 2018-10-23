package topmall.fas.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import topmall.fas.dto.QuotaStepDTO;
import topmall.fas.model.ContractOtherPool;
import topmall.fas.repository.ContractOtherPoolRepository;
import topmall.fas.service.IContractOtherPoolService;
import topmall.framework.repository.IRepository;
import topmall.framework.service.BaseService;
import cn.mercury.basic.query.Query;

@Service
public class ContractOtherPoolService extends BaseService<ContractOtherPool,String> implements  IContractOtherPoolService{
    @Autowired
    private ContractOtherPoolRepository repository;
    
    protected IRepository<ContractOtherPool,String> getRepository(){
        return repository;
    }

	/**
	 * @see topmall.fas.service.IContractOtherPoolService#selectValidOther(cn.mercury.basic.query.Query)
	 */
	@Override
	public List<ContractOtherPool> selectValidOther(Query query) {
		return repository.selectValidOther(query.asMap());
	}

	/**
	 * @see topmall.fas.service.IContractOtherPoolService#selectGroupOtherData(java.util.Map)
	 */
	@Override
	public List<ContractOtherPool> selectGroupOtherData(Query query) {
		return repository.selectGroupOtherData(query.asMap());
	}

	/**
	 * @see topmall.fas.service.IContractOtherPoolService#selectQuotaStep(java.util.Map)
	 */
	@Override
	public List<QuotaStepDTO> selectQuotaStep(Query query) {
		return repository.selectQuotaStep(query.asMap());
	}
    
}







