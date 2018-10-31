package topmall.fas.service;


import java.util.List;

import topmall.fas.model.PropertyCost;
import topmall.framework.service.IService;
import cn.mercury.basic.query.Query;

public interface IPropertyCostService extends IService<PropertyCost,String>{
	
	/**
	 * 物业水电费登记按照结算月汇总
	 * @param Query 查询条件
	 * @return 水电费登记 按照扣费项目汇总
	 */
	List<PropertyCost> selectGroupNum(Query query);
    
}







