package topmall.fas.manager.report.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.mercury.basic.query.Pagenation;
import cn.mercury.basic.query.Query;
import cn.mercury.manager.ManagerException;
import topmall.fas.dto.BalanceSummary;
import topmall.fas.dto.CostTypeDto;
import topmall.fas.dto.PriceTypeDto;
import topmall.fas.manager.report.IBalanceSummaryManager;
import topmall.fas.service.report.IBalanceSummaryService;
import topmall.framework.manager.BaseManager;
import topmall.framework.service.IService;
@Service
public class BalanceSummaryManager extends BaseManager<BalanceSummary,String> implements IBalanceSummaryManager {

	@Autowired
	private IBalanceSummaryService service;
	
	@Override
	protected IService<BalanceSummary, String> getService() {
		return service;
	}

	@Override
	public List<PriceTypeDto> queryPriceTypeList(Query query) {
		return service.queryPriceTypeList(query);
	}
	
	@Override
	public List<CostTypeDto> queryCostTypeList(Query query) {
		return service.queryCostTypeList(query);
	}

	@Override
	public List<BalanceSummary> selectByPage(Query query, Pagenation page) throws ManagerException {
		try {
			List<PriceTypeDto> list = service.queryPriceTypeList(query);
			query.and("priceTypeList", list);
			return service.selectByPage(query, page);
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<Map<String, Object>> selectReportByPage(Query query,Pagenation page) {
		try {
			List<PriceTypeDto> list = service.queryPriceTypeList(query);
			query.and("priceTypeList", list);
			List<Map<String,Object>> listMap =service.selectReportByPage(query, page);
			
			ArrayList<String> counterList = new ArrayList<>();
			
			for(Map<String,Object> map:listMap){
				counterList.add(map.get("counterNo").toString());
			}
			query.and("counterList",counterList);
			
			//销售扣费
			List<Map<String,Object>> guaraSaleCostList = service.selectSaleCostInfo(query);
			for(Map<String,Object> map:listMap){
				for(Map<String,Object> saleCost:guaraSaleCostList){
					if(map.get("counterNo").equals(saleCost.get("counterNo"))&&map.get("settleMonth").equals(saleCost.get("settleMonth"))){
						map.put(saleCost.get("priceType").toString(), saleCost.get("profitAmount"));
					}
				}
			}
			
			//销售总额
			List<Map<String,Object>> guaraSaleAmount = service.selectSaleAmount(query);
			for(Map<String,Object> map:listMap){
				for(Map<String,Object> saleAmount:guaraSaleAmount){
					if(map.get("counterNo").equals(saleAmount.get("counterNo"))&&map.get("settleMonth").equals(saleAmount.get("settleMonth"))){
						map.put("settleSum", saleAmount.get("settleSum"));
					}
				}
			}
			// 保底利润
			List<Map<String,Object>> guaraInfoList = service.selectGuaraInfo(query);
			for(Map<String,Object> map:listMap){
				for(Map<String,Object> guaraInfo:guaraInfoList){
					if(map.get("counterNo").equals(guaraInfo.get("counterNo"))&&map.get("settleMonth").equals(guaraInfo.get("settleMonth"))){
						map.put("guaraProfitSum", guaraInfo.get("cost"));
					}
				}
			}
			
			//扣费
			List<Map<String,Object>> costList = service.selectCostInfo(query);
			for(Map<String,Object> map:listMap){
				for(Map<String,Object> costMap:costList){
					if(map.get("counterNo").equals(costMap.get("counterNo"))&&map.get("settleMonth").equals(costMap.get("settleMonth"))){
						if(null!=costMap.get("costNo")){
							map.put((String) costMap.get("costNo"), costMap.get("cost"));
						}
					}
				}
			}
			// 保底利润
			return listMap;
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}
}
