package topmall.fas.service.report;

import java.util.List;
import java.util.Map;

import cn.mercury.basic.query.Pagenation;
import cn.mercury.basic.query.Query;
import topmall.fas.dto.BalanceSummary;
import topmall.fas.dto.CostTypeDto;
import topmall.fas.dto.PriceTypeDto;
import topmall.framework.service.IService;

public interface IBalanceSummaryService extends IService<BalanceSummary,String>{    
	
	/** 根据查询条件查询出价码类型动态表头
	 * @param query
	 * @return
	 */
	List<PriceTypeDto> queryPriceTypeList(Query query);
	
	
	/** 根据查询条件查询扣费项目动态表头
	 * @param query
	 * @return
	 */
	List<CostTypeDto> queryCostTypeList(Query query);
	
	/** 根据条件查询出报表的基本信息和价码销售等信息
	 * @param query
	 * @param page
	 * @return
	 */
	List<Map<String,Object>> selectReportByPage(Query query,Pagenation page);
	
	/** 查询出扣项费用等信息
	 * @param query
	 * @return
	 */
	List<Map<String,Object>> selectCostInfo(Query query);
	
	/** 查询保底利润
	 * @param query
	 * @return
	 */
	List<Map<String,Object>> selectGuaraInfo(Query query);
	/**
	 * 销售扣费
	 * @param query
	 * @return
	 */
	List<Map<String,Object>> selectSaleCostInfo(Query query);
	
	/**
	 * 销售总额
	 * @param query
	 * @return
	 */
	List<Map<String,Object>> selectSaleAmount(Query query);
	
}

