package topmall.fas.repository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import topmall.fas.model.MallSaleCost;
import topmall.framework.repository.IRepository;
@Mapper
public interface MallSaleCostRepository extends IRepository<MallSaleCost,String> {
	
	/** 解除物业费用和结算单的关系
	 * @param id
	 */
	public void updateToUnsettled(String id);
	
	/**
	 * 查询物业结算期内 物业的基础抽成金额
	 * @param params 查询条件
	 * @return 基础抽成金额
	 */
	BigDecimal queryMallSaleProfit(@Param("params") Map<String, Object> params); 
	
	
	/** 合计
	 * @param params
	 * @return
	 */
	public List<MallSaleCost> queryConditionSum(@Param("params") Map<String, Object> params);
}

             
             
             
             
             
             
             
             
             
             
             
             
             