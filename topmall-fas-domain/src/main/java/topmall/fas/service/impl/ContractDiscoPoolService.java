package topmall.fas.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import topmall.fas.model.ContractDiscoPool;
import topmall.fas.repository.ContractDiscoPoolRepository;
import topmall.fas.service.IContractDiscoPoolService;
import topmall.fas.vo.CounterDaySale;
import topmall.framework.repository.IRepository;
import topmall.framework.service.BaseService;
import cn.mercury.basic.query.Query;

@Service
public class ContractDiscoPoolService extends BaseService<ContractDiscoPool,String> implements  IContractDiscoPoolService{
    @Autowired
    private ContractDiscoPoolRepository repository;
    
    protected IRepository<ContractDiscoPool,String> getRepository(){
        return repository;
    }

	/**
	 * @see topmall.fas.service.IContractDiscoPoolService#selectValidDisco(cn.mercury.basic.query.Query)
	 */
	@Override
	public List<ContractDiscoPool> selectValidDisco(Query query) {
		return repository.selectValidDisco(query.asMap());
	}

	@Override
	public List<ContractDiscoPool> selectGroupContractDiscoData(Query query) {
		return repository.selectGroupContractDiscoData(query.asMap());
	}

	@Override
	public List<CounterDaySale> selectGroupShopDaySaleData(Query query) {
		return repository.selectGroupShopDaySaleData(query.asMap());
	}

	@Override
	public List<CounterDaySale> selectGroupShopDaySaleDataForMall(Query query) {
		return repository.selectGroupShopDaySaleDataForMall(query.asMap());
	}

	@Override
	public BigDecimal ticketAbsorptionAmonut(Query query) {
		return repository.ticketAbsorptionAmonut(query.asMap());
	}

	@Override
	public BigDecimal vipAbsorptionAmonut(Query query) {
		return repository.vipAbsorptionAmonut(query.asMap());
	}
	
	@Override
	public BigDecimal queryReduceDiffAmount(Query query) {
		return repository.queryReduceDiffAmount(query.asMap());
	}

	@Override
	public BigDecimal materialAmount(Query query) {
		return repository.materialAmount(query.asMap());
	}

	@Override
	public BigDecimal pointsAmount(Query query) {
		return repository.pointsAmount(query.asMap());
	}

	@Override
	public List<CounterDaySale> queryMinimumMall(Query query) {
		return repository.selectMinimumMall(query.asMap());
	}

	@Override
	public Integer bagSaleQty(Query query) {
		return repository.selectbagSaleQty(query.asMap());
	}
	

    
}







