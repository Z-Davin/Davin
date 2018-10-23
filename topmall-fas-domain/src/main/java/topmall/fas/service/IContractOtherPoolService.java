package topmall.fas.service;


import java.util.List;

import topmall.fas.dto.QuotaStepDTO;
import topmall.fas.model.ContractOtherPool;
import topmall.framework.service.IService;
import cn.mercury.basic.query.Query;

public interface IContractOtherPoolService extends IService<ContractOtherPool,String>{
	
	/**
	 * 查询有效的合同其他条款
	 * @param query 查询条件
	 * @return 有效的合同其他条款列表
	 */
	List<ContractOtherPool> selectValidOther(Query query);
	
	
	/**
	 * 汇总其它条款的记录 （按照扣费项，计费模式，计费基数，支付方式，生效日期段）
	 * @param query 查询条件
	 * @return 汇总记录List
	 */
	List<ContractOtherPool> selectGroupOtherData(Query query);
	
	/**
	 * 根据其他的条款唯一条件 查询出此条款的额度阶梯扣列表
	 * @param query 其他条款的唯一性条件
	 * @return 额度阶梯扣DTO列表
	 */
	List<QuotaStepDTO> selectQuotaStep(Query query);
}







