package topmall.fas.repository;
import topmall.fas.model.MallCost;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import topmall.framework.repository.IRepository;
@Mapper
public interface MallCostRepository extends IRepository<MallCost,String> {
	
	/** 解除物业费用和结算单的关系
	 * @param id
	 */
	public void updateToUnsettled(String id);
	
	/**
	 * 根据账扣标志分组得到汇总信息,用于组装结算单第一个tab页数据
	 * @param query
	 * @return
	 */
	public List<MallCost> getCostGroupAccountDebit(@Param("params") Map<String, Object> params);
	
	/**
	 * 更新单据状态
	 * @param params
	 */
	public Integer updateStatus (@Param("params") Map<String, Object> params);
	
	/** 合计
	 * @param query
	 * @return
	 */
	public List<MallCost> queryConditionSum(@Param("params") Map<String, Object> params);
	
}

