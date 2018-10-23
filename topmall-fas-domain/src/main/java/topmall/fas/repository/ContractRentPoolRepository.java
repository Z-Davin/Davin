package topmall.fas.repository;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import topmall.fas.model.ContractRentPool;
import topmall.framework.repository.IRepository;
@Mapper
public interface ContractRentPoolRepository extends IRepository<ContractRentPool,String> {
	
	/**
	 * 查询有效的合同租金条款
	 * @param params 查询条件
	 * @return 有效的合同租金条款
	 */
	List<ContractRentPool> selectValidRent(@Param("params") Map<String, Object> params);
}

             
             
             
             
             
             
             
             
             
             
             
             
             