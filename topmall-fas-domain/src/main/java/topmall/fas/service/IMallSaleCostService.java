package topmall.fas.service;


import java.math.BigDecimal;
import java.util.List;

import topmall.fas.model.MallSaleCost;
import topmall.framework.service.IService;
import cn.mercury.basic.query.Query;

public interface IMallSaleCostService extends IService<MallSaleCost,String>{   
	
	
	/** 解除物业费用和结算单的关系
	 * @param id
	 */
	public void updateToUnsettled(String id);
    
	
	/**
	 * 查询物业结算期内 物业的基础抽成金额
	 * @param query 查询对象
	 * @return 基础抽成金额
	 */
	BigDecimal queryMallSaleProfit(Query query); 
	
	/** 合计
	 * @param q
	 * @return
	 */
	public List<MallSaleCost> queryConditionSum(Query q);
}







