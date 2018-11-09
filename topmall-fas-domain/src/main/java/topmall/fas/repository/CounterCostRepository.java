package topmall.fas.repository;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import topmall.fas.model.CounterCost;
import topmall.framework.repository.IRepository;
@Mapper
public interface CounterCostRepository extends IRepository<CounterCost,String> {
	
	/** 解除专柜费用和结算单的关系
	 * @param id
	 */
	public void updateToUnsettled(String id);
	
	/**
	 * footer
	 * @param query
	 * @return
	 */
	List<CounterCost> queryConditionSum(@Param("params") Map<String, Object> params);
	
	
	/**
	 * 更新单据状态
	 * @param params
	 */
	public Integer updateStatus (@Param("params") Map<String, Object> params);
}

             
             
             
             
             
             
             
             
             
             
             
             
             