package topmall.fas.manager.report;

import java.util.List;
import java.util.Map;

import topmall.fas.dto.BalanceSummary;
import topmall.fas.dto.CostTypeDto;
import topmall.fas.dto.PriceTypeDto;
import cn.mercury.basic.query.Pagenation;
import cn.mercury.basic.query.Query;
import cn.mercury.manager.IManager;

public interface IBalanceSummaryManager extends IManager<BalanceSummary,String>{
	
	/** 根据查询条件查询出价码类型动态表头
	 * @param query
	 * @return
	 */
	List<PriceTypeDto> queryPriceTypeList(Query query);
	
	
	List<Map<String,Object>> selectReportByPage(Query query,Pagenation page);
	
	
	/** 根据查询条件查询扣费项目动态表头
	 * @param query
	 * @return
	 */
	List<CostTypeDto> queryCostTypeList(Query query);
	
    
}
