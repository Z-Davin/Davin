package topmall.fas.manager;

import topmall.fas.model.ShopBalanceDate;
import topmall.fas.util.CommonResult;
import cn.mercury.basic.query.Query;
import cn.mercury.manager.IManager;
import cn.mercury.manager.ManagerException;

public interface IShopBalanceDateManager extends IManager<ShopBalanceDate,String>{
	/**
	 * 插入数据之前的验证方法
	 * @param shopBalanceDate 验证对象
	 * @return 返回结果集
	 */
	CommonResult validateCreate(ShopBalanceDate shopBalanceDate);

	ShopBalanceDate confirm(String id);

	Integer insert(ShopBalanceDate entry) throws ManagerException;
	
	/** 店铺月结
	 * @param idList
	 */
	public CommonResult monthBalance(String idList,int targetStatus);
	
	/**
	 * 根据唯一条件查询出唯一值
	 * @param Query 查询出唯一结果的条件
	 * @return  唯一结果
	 */
	ShopBalanceDate findByUnique(Query query);
	
}







