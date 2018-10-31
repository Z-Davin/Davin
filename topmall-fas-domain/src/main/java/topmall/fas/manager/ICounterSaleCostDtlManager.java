package topmall.fas.manager;

import java.math.BigDecimal;

import topmall.fas.model.CounterSaleCostDtl;
import cn.mercury.basic.query.Query;
import cn.mercury.manager.IManager;

public interface ICounterSaleCostDtlManager extends IManager<CounterSaleCostDtl,String>{
	
	/**
	 * 查询店铺专柜(某个部类码)的销售提成
	 * @param query 查询条件
	 * @return 销售提成总数
	 */
	BigDecimal querySaleProfit(Query query);
}







