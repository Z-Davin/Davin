package topmall.fas.service;


import java.util.List;

import cn.mercury.basic.query.Query;
import topmall.fas.model.CounterCost; 
import topmall.framework.service.IService;

public interface ICounterCostService extends IService<CounterCost,String>{  
	
	/** 解除专柜费用和结算单的关系
	 * @param id
	 */
	public void updateToUnsettled(String id);
	
	/**
	 * footer
	 * @param query
	 * @return
	 */
	List<CounterCost> queryConditionSum(Query query);
	
	
	/**
	 * 更新状态
	 * @param query
	 */
	public Integer updateStatus(Query query);
    
}







