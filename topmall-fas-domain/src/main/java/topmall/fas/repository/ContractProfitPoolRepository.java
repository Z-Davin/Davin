package topmall.fas.repository;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import topmall.fas.dto.QuotaStepDTO;
import topmall.fas.model.ContractProfitPool;
import topmall.framework.repository.IRepository;
@Mapper
public interface ContractProfitPoolRepository extends IRepository<ContractProfitPool,String> {
	
	/**
	 * 查询有效的合同抽成条款
	 * @param params 查询条件
	 * @return 有效的合同抽成条款
	 */
	List<ContractProfitPool> selectValidProfit(@Param("params") Map<String, Object> params);
	
	/**
	 * 汇总抽成条款的记录 （按照扣费项，扣率类型，销售码，生效日期段）
	 * @param params 查询条件
	 * @return 汇总记录List
	 */
	List<ContractProfitPool> selectGroupProfitData(@Param("params") Map<String, Object> params);
	
	/**
	 * 根据抽成的条款唯一条件 查询出此条款的额度阶梯扣列表
	 * @param params 抽成条款的唯一性条件
	 * @return 额度阶梯扣DTO列表
	 */
	List<QuotaStepDTO> selectQuotaStep(@Param("params") Map<String, Object> params);
}

             
             
             
             
             
             
             
             
             
             
             
             
             