package topmall.fas.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import topmall.fas.model.CounterSaleCost;
import topmall.framework.repository.IRepository;
@Mapper
public interface CounterSaleCostRepository extends IRepository<CounterSaleCost,String> {
	
	/** 根据专柜及结算期查询销售费用
	 * @param params
	 * @return
	 */
	public List<CounterSaleCost> selectCounterSaleDisco(@Param("params") Map<String, Object> params);
	
	/** 解除专柜销售费用和结算单的关系
	 * @param id
	 */
	public void updateToUnsettled(String id);
	
	/**
	 * 用于统计合计
	 * @param query
	 * @return
	 */
	List<CounterSaleCost>  queryConditionSum(@Param("params") Map<String, Object> params);
	
	/**
	 * 查询店铺专柜(某个部类码)的销售提成
	 * @param params 查询条件
	 * @return 销售提成总数
	 */
	BigDecimal querySaleProfit(@Param("params") Map<String, Object> params);
	
	/**
	 * 根据部类码分组,数量,金额都取SUM
	 * @param params
	 * @return
	 */
	List<CounterSaleCost>  queryListGroupDivisionNo(@Param("params") Map<String, Object> params);
	
	 /**查询净收入和毛收入只差
	 * @param params
	 * @return
	 */
	BigDecimal queryReduceDiffAmount(@Param("params") Map<String, Object> params);
	
}

             
             
             
             
             
             
             
             
             
             
             
             
             