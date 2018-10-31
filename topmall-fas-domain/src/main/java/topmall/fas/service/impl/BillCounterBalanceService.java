package topmall.fas.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import topmall.fas.model.BillCounterBalance;
import topmall.fas.repository.BillCounterBalanceRepository;
import topmall.fas.service.IBillCounterBalanceService;
import topmall.framework.repository.IRepository;
import topmall.framework.service.BaseService;
import cn.mercury.basic.query.Pagenation;
import cn.mercury.basic.query.Query;

@Service
public class BillCounterBalanceService extends BaseService<BillCounterBalance,String> implements  IBillCounterBalanceService{
    @Autowired
    private BillCounterBalanceRepository repository;
    
    protected IRepository<BillCounterBalance,String> getRepository(){
        return repository;
    }

	@Override
	public BillCounterBalance findByUnique(Query query) {
		return repository.findByUnique(query.asMap());
	}

	@Override
	public BillCounterBalance getLastCounterBalanceDate(Query query) {
		return repository.getLastCounterBalanceDate(query.asMap());
	}


	@Override
	public Integer updateStatus(Query query) {
		return repository.updateStatus(query.asMap());
	}

	/**
	 * @see topmall.fas.service.IBillCounterBalanceService#selectByPageTotal(cn.mercury.basic.query.Query, cn.mercury.basic.query.Pagenation)
	 */
	@Override
	public List<BillCounterBalance> selectByPageTotal(Query query, Pagenation page) {
		return repository.selectByPageTotal(query.asMap(), page, query.getSort());
	}
    
}







