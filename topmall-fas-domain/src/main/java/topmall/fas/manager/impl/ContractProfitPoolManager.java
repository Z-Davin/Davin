package topmall.fas.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import topmall.fas.domain.handler.ProfitCalculateHandler;
import topmall.fas.dto.QuotaStepDTO;
import topmall.fas.manager.IContractProfitPoolManager;
import topmall.fas.model.ContractProfitPool;
import topmall.fas.model.CounterCost;
import topmall.fas.model.ShopBalanceDateDtl;
import topmall.fas.service.IContractProfitPoolService;
import topmall.fas.util.CommonUtil;
import topmall.fas.util.GroupByUtils;
import topmall.fas.util.PublicConstans;
import topmall.framework.exception.TopmallException;
import topmall.framework.manager.BaseManager;
import topmall.framework.service.IService;
import cn.mercury.basic.query.Query;
import cn.mercury.utils.DateUtil;

import com.google.common.base.Function;

/**
 * 财务结算期专柜合同抽成池表
 * @author Administrator
 *
 */
@Service
public class ContractProfitPoolManager extends BaseManager<ContractProfitPool, String> implements
		IContractProfitPoolManager {
	@Autowired
	private IContractProfitPoolService service;

	protected IService<ContractProfitPool, String> getService() {
		return service;
	}

	/**
	 * @see topmall.fas.manager.IContractProfitPoolManager#selectValidProfit(cn.mercury.basic.query.Query)
	 */
	@Override
	public List<ContractProfitPool> selectValidProfit(Query query) {
		
		List<ContractProfitPool> reusltList = new ArrayList<>();
		
		// 获取结算期内有效的抽成条款
		List<ContractProfitPool> profitPoolList = service.selectValidProfit(query);
		if(CommonUtil.hasValue(profitPoolList)){
			
			// 如果有效的合同条款 有多条则按照扣项和部类编码
			Map<String, List<ContractProfitPool>> groupByMap = GroupByUtils.groupByKey(profitPoolList,
					new Function<ContractProfitPool, String>() {
						@Override
						public String apply(ContractProfitPool input) {
							return input.getCostNo() + input.getDivisionNo();
						}
					});
			
			
			
			for (Entry<String, List<ContractProfitPool>>  entry : groupByMap.entrySet()) {
				
				List<ContractProfitPool> tempResult = new ArrayList<>();
				
				// 有效合同分组后 需要第二次分组 按照合同的开始时间
				Map<Date, List<ContractProfitPool>> groupByMapTemp = GroupByUtils.groupByKey(profitPoolList,
						new Function<ContractProfitPool, Date>() {
							@Override
							public Date apply(ContractProfitPool input) {
								return input.getStartDate();
							}
						});
				for (Entry<Date, List<ContractProfitPool>>  entryTemp : groupByMapTemp.entrySet()) {
					//此日期没有实际意义 只是用来与合同的生效日期做比对
					Date tempDate;
					try {
						tempDate = DateUtil.getdate("1989-09-11");
					} catch (Exception e) {
						throw new TopmallException(PublicConstans.SYSTEM_ERROR, "日期转换出错");
					}
					Date contractDate = entryTemp.getKey();
					
					// 取一条生效日期最大的条款作为 抽成费用计算的有效条款
					if(contractDate.after(tempDate)){
						tempDate = contractDate;
						tempResult = entry.getValue();
					}
				}
				reusltList.addAll(tempResult);
			}
		}
		return reusltList;
	}

	/**
	 * @see topmall.fas.manager.IContractProfitPoolManager#selectGroupProfitData(cn.mercury.basic.query.Query)
	 */
	@Override
	public List<ContractProfitPool> selectGroupProfitData(Query query) {
		return service.selectGroupProfitData(query);
	}

	/**
	 * @see topmall.fas.manager.IContractProfitPoolManager#selectQuotaStep(cn.mercury.basic.query.Query)
	 */
	@Override
	public List<QuotaStepDTO> selectQuotaStep(Query query) {
		return service.selectQuotaStep(query);
	}

	/**
	 * @see topmall.fas.manager.IContractProfitPoolManager#createProfitCost(topmall.fas.model.ShopBalanceDate)
	 */
	@Override
	public List<CounterCost> createProfitCost(ShopBalanceDateDtl shopBalanceDateDtl) {
		ProfitCalculateHandler profitCalculateHandler = new ProfitCalculateHandler(shopBalanceDateDtl, this);
		return profitCalculateHandler.calculateCost(false);

	}

}
