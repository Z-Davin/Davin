package topmall.fas.manager;

import java.util.List;

import topmall.fas.model.ContractRentPool;
import topmall.fas.model.CounterCost;
import topmall.fas.model.ShopBalanceDateDtl;
import cn.mercury.basic.query.Query;
import cn.mercury.manager.IManager;

public interface IContractRentPoolManager extends IManager<ContractRentPool,String>{
    
	/**
	 * 查询有效的合同租金条款
	 * @param query 查询条件
	 * @return 有效的合同租金条款
	 */
	List<ContractRentPool> selectValidRent(Query query);
	
	/**
	 * 生成专柜合同租金费用
	 * @param shopBalanceDate 结算期对象
	 * @return  专柜费用List
	 */
	List<CounterCost> createRentCost(ShopBalanceDateDtl shopBalanceDateDtl);
}







