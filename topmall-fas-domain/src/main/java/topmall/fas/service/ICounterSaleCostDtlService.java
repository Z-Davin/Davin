package topmall.fas.service;




import java.math.BigDecimal;
import java.util.List;

import topmall.fas.model.CounterSaleCost;
import topmall.fas.model.CounterSaleCostDtl;
import topmall.framework.service.IService;
import cn.mercury.basic.query.Query;

public interface ICounterSaleCostDtlService extends IService<CounterSaleCostDtl,String>{    
	
	/** 根据部类码,时间查询出历史明细费用
	 * @param query
	 * @return
	 */
	List<CounterSaleCostDtl> selectRecalculateDtlHisList(Query query);
	
	/** 根据部类码,时间查询出历史费用
	 * @param query
	 * @return
	 */
	List<CounterSaleCost> selectRecalculateHisList(Query query);
	
	/**
	 * 查询店铺专柜(某个部类码)的销售提成
	 * @param query 查询条件
	 * @return 销售提成总数
	 */
	BigDecimal querySaleProfit(Query query);
}







