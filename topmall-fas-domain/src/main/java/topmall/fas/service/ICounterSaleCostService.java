package topmall.fas.service;

import java.math.BigDecimal;
import java.util.List;
import topmall.fas.model.CounterSaleCost;
import topmall.framework.service.IService;
import cn.mercury.basic.query.Query;

public interface ICounterSaleCostService extends IService<CounterSaleCost,String>{    
	
	/** 根据专柜及结算期查询销售费用
	 * @param query
	 * @return
	 */
	public List<CounterSaleCost> selectCounterSaleDisco(Query query);
	
	/** 解除专柜销售费用和结算单的关系
	 * @param id
	 */
	public void updateToUnsettled(String id);
	
	/**
	 * 用于统计合计
	 * @param query
	 * @return
	 */
	List<CounterSaleCost>  queryConditionSum(Query query);
	
	/**
	 * 查询店铺专柜(某个部类码)的销售提成
	 * @param query 查询条件
	 * @return 销售提成总数
	 */
	BigDecimal querySaleProfit(Query query);
	
	
	/**
	 * 根据部类码分组,数量,金额都取SUM
	 * @param query
	 * @return
	 */
	 List<CounterSaleCost> queryListGroupDivisionNo(Query query);
	 
	 /**查询净收入和毛收入只差
	 * @param query
	 * @return
	 */
	BigDecimal queryReduceDiffAmount(Query query);
}