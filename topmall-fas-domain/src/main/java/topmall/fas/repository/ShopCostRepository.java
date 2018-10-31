package topmall.fas.repository;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import topmall.fas.model.ShopCost;
import topmall.framework.repository.IRepository;
@Mapper
public interface ShopCostRepository extends IRepository<ShopCost,String> {
	
	/**
	 * 专柜费用登记需要按照扣项汇总
	 * @param map 查询条件
	 * @return 扣项汇总后的数据结果
	 */
	List<ShopCost> selectGroupNum(@Param("params") Map<String, Object> params);
}

             
             
             
             
             
             
             
             
             
             
             
             
             