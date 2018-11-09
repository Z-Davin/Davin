package topmall.fas.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.mercury.basic.query.Query;
import topmall.fas.model.MallPrepay;
import topmall.fas.repository.MallPrepayRepository;
import topmall.fas.service.IMallPrepayService;
import topmall.framework.repository.IRepository;
import topmall.framework.service.BaseService;

@Service
public class MallPrepayService extends BaseService<MallPrepay,String> implements  IMallPrepayService{
    @Autowired
    private MallPrepayRepository repository;
    
    protected IRepository<MallPrepay,String> getRepository(){
        return repository;
    }

	@Override
	public MallPrepay findByUnique(Query query) {
		return repository.findByUnique(query.asMap());
	}
    
}







