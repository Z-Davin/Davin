package topmall.fas.service;


import java.util.List;

import topmall.fas.model.ShopCost;
import topmall.framework.service.IService;
import cn.mercury.basic.query.Query;

public interface IShopCostService extends IService<ShopCost,String>{    
	
	/**
	 * 专柜费用登记需要按照扣项汇总
	 * @param query 查询条件
	 * @return 扣项汇总后的数据结果
	 */
	List<ShopCost> selectGroupNum(Query query);
    
}







