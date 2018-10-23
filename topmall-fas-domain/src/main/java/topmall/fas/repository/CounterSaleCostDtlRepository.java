package topmall.fas.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import topmall.fas.model.CounterSaleCost;
import topmall.fas.model.CounterSaleCostDtl;
import topmall.framework.repository.IRepository;
@Mapper
public interface CounterSaleCostDtlRepository extends IRepository<CounterSaleCostDtl,String> {
	
	
	/** 根据部类码,时间查询出明细历史费用
	 * @param params
	 * @return
	 */
	List<CounterSaleCostDtl> selectRecalculateDtlHisList(@Param("params") Map<String, Object> params);
	
	
	
	/** 根据部类码,时间查询出历史费用
	 * @param params
	 * @return
	 */
	List<CounterSaleCost> selectRecalculateHisList(@Param("params") Map<String, Object> params);
	
	/**
	 * 查询店铺专柜(某个部类码)的销售提成
	 * @param params 查询条件
	 * @return 销售提成总数
	 */
	BigDecimal querySaleProfit(@Param("params") Map<String, Object> params);
}

             
             
             
             
             
             
             
             
             
             
             
             
             