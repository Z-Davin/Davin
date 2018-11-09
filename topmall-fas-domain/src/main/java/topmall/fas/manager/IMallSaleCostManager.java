package topmall.fas.manager;

import java.math.BigDecimal;
import java.util.List;

import cn.mercury.basic.query.Query;
import cn.mercury.manager.IManager;
import topmall.fas.model.MallSaleCost;

public interface IMallSaleCostManager extends IManager<MallSaleCost,String>{
    
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







