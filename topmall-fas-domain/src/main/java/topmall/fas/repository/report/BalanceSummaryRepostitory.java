package topmall.fas.repository.report;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import cn.mercury.basic.query.Pagenation;
import topmall.fas.dto.BalanceSummary;
import topmall.fas.dto.CostTypeDto;
import topmall.fas.dto.PriceTypeDto;
import topmall.framework.repository.IRepository;

public interface BalanceSummaryRepostitory  extends IRepository<BalanceSummary,String> {
	
	/** 根据查询条件查询出价码类型动态表头
	 * @param params
	 * @return
	 */
	List<PriceTypeDto> queryPriceTypeList(@Param("params") Map<String,Object> params);
	
	/** 查询出扣项费用等信息
	 * @param query
	 * @return
	 */
	List<Map<String,Object>> selectCostInfo(@Param("params") Map<String,Object> params);
	
	
	List<Map<String,Object>> selectReportByPage(@Param("params") Map<String,Object> params,@Param("page") Pagenation page,
			@Param("orderby") String orderby);
	
	/** 根据查询条件查询扣费项目动态表头
	 * @param query
	 * @return
	 */
	List<CostTypeDto> queryCostTypeList(@Param("params") Map<String,Object> params);
	
	
	/** 查询保底利润
	 * @param query
	 * @return
	 */
	List<Map<String,Object>> selectGuaraInfo(@Param("params") Map<String,Object> params);
	
	
	/** 销售扣费
	 * @param query
	 * @return
	 */
	List<Map<String,Object>> selectSaleCostInfo(@Param("params") Map<String,Object> params);
	
	/**
	 * 销售总额
	 * @param query
	 * @return
	 */
	List<Map<String,Object>> selectSaleAmount(@Param("params") Map<String,Object> params);

}
