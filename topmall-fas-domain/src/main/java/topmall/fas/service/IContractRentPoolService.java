package topmall.fas.service;


import java.util.List;

import topmall.fas.model.ContractRentPool;
import topmall.framework.service.IService;
import cn.mercury.basic.query.Query;

public interface IContractRentPoolService extends IService<ContractRentPool,String>{    
	
	
	/**
	 * 查询有效的合同租金条款
	 * @param query 查询条件
	 * @return 有效的合同租金条款
	 */
	List<ContractRentPool> selectValidRent(Query query);
    
}







