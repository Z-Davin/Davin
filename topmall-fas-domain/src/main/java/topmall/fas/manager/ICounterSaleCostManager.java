package topmall.fas.manager;

import java.math.BigDecimal;
import java.util.List;

import topmall.fas.model.CounterSaleCost;
import cn.mercury.basic.query.Query;
import cn.mercury.manager.IManager;

public interface ICounterSaleCostManager extends IManager<CounterSaleCost,String>{
	
//	/**
//	 * 根据专柜编码,结算期生成销售费用单
//	 */
//	public void createSaleCost(ShopBalanceDate shopBalanceDate);
	/**
	 * 用于统计合计
	 * @param query
	 * @return
	 */
	List<CounterSaleCost> queryConditionSum(Query query);
    
	/**
	 * 查询店铺专柜(某个部类码)的销售提成
	 * @param query 查询条件
	 * @return 销售提成总数
	 */
	BigDecimal querySaleProfit(Query query);
}







