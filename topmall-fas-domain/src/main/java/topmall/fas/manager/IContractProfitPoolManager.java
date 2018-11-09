package topmall.fas.manager;

import java.util.List;

import topmall.fas.dto.QuotaStepDTO;
import topmall.fas.model.ContractProfitPool;
import topmall.fas.model.CounterCost;
import topmall.fas.model.ShopBalanceDateDtl;
import cn.mercury.basic.query.Query;
import cn.mercury.manager.IManager;

public interface IContractProfitPoolManager extends IManager<ContractProfitPool,String>{
    
	/**
	 * 查询有效的合同抽成条款
	 * @param query 查询条件
	 * @return 有效的合同抽成条款
	 */
	List<ContractProfitPool> selectValidProfit(Query query);
	
	/**
	 * 汇总抽成条款的记录 （按照扣费项，扣率类型，销售码，生效日期段）
	 * @param query 查询条件
	 * @return 汇总记录List
	 */
	List<ContractProfitPool> selectGroupProfitData(Query query);
	
	/**
	 * 根据抽成的条款唯一条件 查询出此条款的额度阶梯扣列表
	 * @param query 抽成条款的唯一性条件
	 * @return 额度阶梯扣DTO列表
	 */
	List<QuotaStepDTO> selectQuotaStep(Query query);
	
	/**
     * 生成专柜合同抽成费用
     * @param shopBalanceDate 结算期对象
     * @return 专柜费用list
     */
	List<CounterCost> createProfitCost(ShopBalanceDateDtl shopBalanceDateDtl);
}







