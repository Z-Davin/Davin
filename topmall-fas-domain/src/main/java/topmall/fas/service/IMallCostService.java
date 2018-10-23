package topmall.fas.service;


import java.util.List;

import cn.mercury.basic.query.Query;
import topmall.fas.model.MallCost; 
import topmall.framework.service.IService;

public interface IMallCostService extends IService<MallCost,String>{    
	
	/** 解除物业费用和结算单的关系
	 * @param id
	 */
	public void updateToUnsettled(String id);
	
	/**
	 * 根据账扣标志分组得到汇总信息
	 * @param query
	 * @return
	 */
	public List<MallCost> getCostGroupAccountDebit(Query query);
	
	/**
	 * 更新状态
	 * @param query
	 */
	public Integer updateStatus(Query query);
	
	/** 合计
	 * @param query
	 * @return
	 */
	public List<MallCost> queryConditionSum(Query query);
    
}







