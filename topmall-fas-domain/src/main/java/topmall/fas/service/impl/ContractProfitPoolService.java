package topmall.fas.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import topmall.fas.dto.QuotaStepDTO;
import topmall.fas.model.ContractProfitPool;
import topmall.fas.repository.ContractProfitPoolRepository;
import topmall.fas.service.IContractProfitPoolService;
import topmall.framework.repository.IRepository;
import topmall.framework.service.BaseService;
import cn.mercury.basic.query.Query;

@Service
public class ContractProfitPoolService extends BaseService<ContractProfitPool,String> implements  IContractProfitPoolService{
    @Autowired
    private ContractProfitPoolRepository repository;
    
    protected IRepository<ContractProfitPool,String> getRepository(){
        return repository;
    }

	/**
	 * @see topmall.fas.service.IContractProfitPoolService#selectValidProfit(cn.mercury.basic.query.Query)
	 */
	@Override
	public List<ContractProfitPool> selectValidProfit(Query query) {
		return repository.selectValidProfit(query.asMap());
	}

	/**
	 * @see topmall.fas.service.IContractProfitPoolService#selectGroupProfitData(cn.mercury.basic.query.Query)
	 */
	@Override
	public List<ContractProfitPool> selectGroupProfitData(Query query) {
		return repository.selectGroupProfitData(query.asMap());
	}

	/**
	 * @see topmall.fas.service.IContractProfitPoolService#selectQuotaStep(cn.mercury.basic.query.Query)
	 */
	@Override
	public List<QuotaStepDTO> selectQuotaStep(Query query) {
		return repository.selectQuotaStep(query.asMap());
	}
}







