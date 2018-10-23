package topmall.fas.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import topmall.fas.model.MallSaleCost;
import topmall.fas.repository.MallSaleCostRepository;
import topmall.fas.service.IMallSaleCostService;
import topmall.framework.repository.IRepository;
import topmall.framework.service.BaseService;
import cn.mercury.basic.query.Query;

@Service
public class MallSaleCostService extends BaseService<MallSaleCost,String> implements  IMallSaleCostService{
    @Autowired
    private MallSaleCostRepository repository;
    
    protected IRepository<MallSaleCost,String> getRepository(){
        return repository;
    }
    
	@Override
	public void updateToUnsettled(String id) {
		repository.updateToUnsettled(id);
	}

	/**
	 * @see topmall.fas.service.IMallSaleCostService#queryMallSaleProfit(cn.mercury.basic.query.Query)
	 */
	@Override
	public BigDecimal queryMallSaleProfit(Query query) {
		return repository.queryMallSaleProfit(query.asMap());
	}

	@Override
	public List<MallSaleCost> queryConditionSum(Query q) {
		return repository.queryConditionSum(q.asMap());
	}
    
}







