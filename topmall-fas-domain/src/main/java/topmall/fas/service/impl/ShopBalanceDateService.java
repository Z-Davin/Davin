package topmall.fas.service.impl;

import org.springframework.stereotype.Service;
import topmall.fas.model.ShopBalanceDate;
import topmall.fas.repository.ShopBalanceDateRepository;

import topmall.fas.service.IShopBalanceDateService;
import topmall.framework.repository.IRepository;
import topmall.framework.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

import cn.mercury.basic.query.Query;

@Service
public class ShopBalanceDateService extends BaseService<ShopBalanceDate,String> implements  IShopBalanceDateService{
    @Autowired
    private ShopBalanceDateRepository repository;
    
    protected IRepository<ShopBalanceDate,String> getRepository(){
        return repository;
    }
    /**
	 * @see topmall.fas.service.IShopBalanceDateService#findByUnique(cn.mercury.basic.query.Query)
	 */
	@Override
	public ShopBalanceDate findByUnique(Query query) {
		return repository.findByUnique(query.asMap());
	}
    
}







