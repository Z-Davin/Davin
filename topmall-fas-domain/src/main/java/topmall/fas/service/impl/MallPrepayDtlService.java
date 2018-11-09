package topmall.fas.service.impl;

import org.springframework.stereotype.Service;

import cn.mercury.basic.query.Query;
import topmall.fas.model.MallPrepayDtl;
import topmall.fas.repository.MallPrepayDtlRepository;

import topmall.fas.service.IMallPrepayDtlService;
import topmall.framework.repository.IRepository;
import topmall.framework.service.BaseService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class MallPrepayDtlService extends BaseService<MallPrepayDtl,String> implements  IMallPrepayDtlService{
    @Autowired
    private MallPrepayDtlRepository repository;
    
    protected IRepository<MallPrepayDtl,String> getRepository(){
        return repository;
    }

	@Override
	public List<MallPrepayDtl> queryByMallBalance(Query query) {
		return repository.queryByMallBalance(query.asMap());
	}
    
}







