package topmall.fas.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import topmall.fas.model.ShopCost;
import topmall.fas.repository.ShopCostRepository;
import topmall.fas.service.IShopCostService;
import topmall.framework.repository.IRepository;
import topmall.framework.service.BaseService;
import cn.mercury.basic.query.Query;

@Service
public class ShopCostService extends BaseService<ShopCost,String> implements  IShopCostService{
    @Autowired
    private ShopCostRepository repository;
    
    protected IRepository<ShopCost,String> getRepository(){
        return repository;
    }

	/**
	 * @see topmall.fas.service.IShopCostService#selectGroupNum(cn.mercury.basic.query.Query)
	 */
	@Override
	public List<ShopCost> selectGroupNum(Query query) {
		return repository.selectGroupNum(query.asMap());
	}
}







