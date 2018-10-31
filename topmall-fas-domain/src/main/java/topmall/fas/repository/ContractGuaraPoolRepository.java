package topmall.fas.repository;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import topmall.fas.model.ContractGuaraPool;
import topmall.framework.repository.IRepository;
@Mapper
public interface ContractGuaraPoolRepository extends IRepository<ContractGuaraPool,String> {
	
	/**
	 * 查询有效的合同保底条款
	 * @param params 查询条件
	 * @return 有效的合同保底条款
	 */
	List<ContractGuaraPool> selectValidGuara(@Param("params") Map<String, Object> params);
}

             
             
             
             
             
             
             
             
             
             
             
             
             