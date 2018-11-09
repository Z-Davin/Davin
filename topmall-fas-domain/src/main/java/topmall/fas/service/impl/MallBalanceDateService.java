package topmall.fas.service.impl;

import org.springframework.stereotype.Service;
import topmall.fas.model.MallBalanceDate;
import topmall.fas.repository.MallBalanceDateRepository;

import topmall.fas.service.IMallBalanceDateService;
import topmall.framework.repository.IRepository;
import topmall.framework.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

import cn.mercury.basic.query.Query;

@Service
public class MallBalanceDateService extends BaseService<MallBalanceDate,String> implements  IMallBalanceDateService{
    @Autowired
    private MallBalanceDateRepository repository;
    
    protected IRepository<MallBalanceDate,String> getRepository(){
        return repository;
    }

	@Override
	public MallBalanceDate findByUnique(Query query) {
		return repository.findByUnique(query.asMap());
	}
    
}







