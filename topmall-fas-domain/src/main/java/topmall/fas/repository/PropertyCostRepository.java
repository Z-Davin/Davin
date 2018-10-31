package topmall.fas.repository;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import topmall.fas.model.PropertyCost;
import topmall.framework.repository.IRepository;
@Mapper
public interface PropertyCostRepository extends IRepository<PropertyCost,String> {
	
	/**
	 * 物业水电费登记按照结算月汇总
	 * @param params 查询条件
	 * @return 水电费登记 按照扣费项目汇总
	 */
	List<PropertyCost> selectGroupNum(@Param("params") Map<String, Object> params);
}

             
             
             
             
             
             
             
             
             
             
             
             
             