package topmall.fas.service.impl;

import org.springframework.stereotype.Service;
import topmall.fas.model.BillMallBalance;
import topmall.fas.repository.BillMallBalanceRepository;

import topmall.fas.service.IBillMallBalanceService;
import topmall.framework.repository.IRepository;
import topmall.framework.service.BaseService;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;

import cn.mercury.basic.query.Query;

@Service
public class BillMallBalanceService extends BaseService<BillMallBalance,String> implements  IBillMallBalanceService{
    @Autowired
    private BillMallBalanceRepository repository;
    
    protected IRepository<BillMallBalance,String> getRepository(){
        return repository;
    }

	@Override
	public BillMallBalance findByUnique(Query query) {
		return repository.findByUnique(query.asMap());
	}

	@Override
	public BigDecimal proMinimumSum(Query query) {
		return repository.proMinimumSum(query.asMap());
	}

	@Override
	public BigDecimal yearGuaraSum(Query query) {
		return repository.yearGuaraSum(query.asMap());
	}

	@Override
	public BigDecimal billingSum(Query query) {
		return repository.billingSum(query.asMap());
	}
    
}
