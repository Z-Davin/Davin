package topmall.fas.manager;

import java.util.List;

import topmall.fas.model.ContractGuaraPool;
import topmall.fas.model.CounterCost;
import topmall.fas.model.ShopBalanceDateDtl;
import cn.mercury.basic.query.Query;
import cn.mercury.manager.IManager;

public interface IContractGuaraPoolManager extends IManager<ContractGuaraPool,String>{
    
	
	/**
	 * 查询有效的合同保底条款
	 * @param query 查询条件
	 * @return 有效的合同保底条款列表
	 */
	List<ContractGuaraPool> selectValidGuara(Query query);
	
	/**
     * 生成专柜合同保底费用
     * @param shopBalanceDate 结算期对象
     * @return 专柜费用list
     */
	List<CounterCost> createGuaraCost(ShopBalanceDateDtl shopBalanceDateDtl);
}







