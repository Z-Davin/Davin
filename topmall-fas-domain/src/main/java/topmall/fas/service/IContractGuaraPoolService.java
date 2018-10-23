package topmall.fas.service;


import java.util.List;

import topmall.fas.model.ContractGuaraPool;
import topmall.framework.service.IService;
import cn.mercury.basic.query.Query;

public interface IContractGuaraPoolService extends IService<ContractGuaraPool,String>{
	
	/**
	 * 查询有效的合同保底条款
	 * @param query 查询条件
	 * @return 有效的合同保底条款列表
	 */
	List<ContractGuaraPool> selectValidGuara(Query query);
}







