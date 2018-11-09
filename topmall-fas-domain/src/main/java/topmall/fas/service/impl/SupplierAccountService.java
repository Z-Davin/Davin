package topmall.fas.service.impl;

import org.springframework.stereotype.Service;

import cn.mercury.basic.query.Query;
import topmall.fas.model.SupplierAccount;
import topmall.fas.repository.SupplierAccountRepository;

import topmall.fas.service.ISupplierAccountService;
import topmall.framework.repository.IRepository;
import topmall.framework.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class SupplierAccountService extends BaseService<SupplierAccount,String> implements  ISupplierAccountService{
    @Autowired
    private SupplierAccountRepository repository;
    
    protected IRepository<SupplierAccount,String> getRepository(){
        return repository;
    }
    
	@Override
	public Integer updateStatus(Query query) {
		return repository.updateStatus(query.asMap());
	}

    
}







