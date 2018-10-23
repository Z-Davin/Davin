package topmall.fas.manager;

import java.util.List;

import topmall.fas.model.MallBalanceDateDtl;
import topmall.fas.model.PropertyCost;
import cn.mercury.manager.IManager;

public interface IPropertyCostManager extends IManager<PropertyCost,String>{
	
	/**
	 * 物业水电费登记按照结算月汇总
	 * @param mallBalanceDateDtl 物业结算期明细
	 * @return 水电费登记 按照扣费项目汇总
	 */
	List<PropertyCost> selectGroupNum(MallBalanceDateDtl mallBalanceDateDtl);
	
	/**
	 * 物业水电费登记 审核
	 * @param id 
	 * @return
	 */
	PropertyCost confirm(String id);
    
}







