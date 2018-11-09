package topmall.fas.service;


import cn.mercury.basic.query.Query;
import topmall.fas.model.ShopBalanceDate; 
import topmall.framework.service.IService;

public interface IShopBalanceDateService extends IService<ShopBalanceDate,String>{    
	/**
	 * 根据唯一条件查询出唯一值
	 * @param Query 查询出唯一结果的条件
	 * @return  唯一结果
	 */
	ShopBalanceDate findByUnique(Query query);
}







