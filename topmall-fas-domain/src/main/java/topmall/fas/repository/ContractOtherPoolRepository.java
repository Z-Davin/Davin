package topmall.fas.repository;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import topmall.fas.dto.QuotaStepDTO;
import topmall.fas.model.ContractOtherPool;
import topmall.framework.repository.IRepository;
@Mapper
public interface ContractOtherPoolRepository extends IRepository<ContractOtherPool,String> {
	
	/**
	 * 查询有效的合同其他条款
	 * @param params 查询条件
	 * @return 有效的合同其他条款
	 */
	List<ContractOtherPool> selectValidOther(@Param("params") Map<String, Object> params);
	
	/**
	 * 汇总其它条款的记录 （按照扣费项，计费模式，计费基数，支付方式，生效日期段）
	 * @param params 查询条件
	 * @return 汇总记录List
	 */
	List<ContractOtherPool> selectGroupOtherData(@Param("params") Map<String, Object> params);
	
	/**
	 * 根据其他的条款唯一条件 查询出此条款的额度阶梯扣列表
	 * @param params 其他条款的唯一性条件
	 * @return 额度阶梯扣DTO列表
	 */
	List<QuotaStepDTO> selectQuotaStep(@Param("params") Map<String, Object> params);
}

             
             
             
             
             
             
             
             
             
             
             
             
             