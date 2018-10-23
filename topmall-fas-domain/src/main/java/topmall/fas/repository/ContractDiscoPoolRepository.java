package topmall.fas.repository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import topmall.fas.model.ContractDiscoPool;
import topmall.fas.vo.CounterDaySale;
import topmall.framework.repository.IRepository;
@Mapper
public interface ContractDiscoPoolRepository extends IRepository<ContractDiscoPool,String> {
	
	/**
	 * 查询有效的合同扣率条款
	 * @param params 查询条件
	 * @return 有效的合同扣率条款
	 */
	List<ContractDiscoPool> selectValidDisco(@Param("params") Map<String, Object> params);
	
	/** 根据 部类编码,税率,票扣标识,账扣标识,时间维度分组查询
	 * @return
	 */
	List<ContractDiscoPool> selectGroupContractDiscoData(@Param("params") Map<String, Object> params);
	
	/**  从日结表 根据 部类编码,销售时间,活动,商品折扣,扣率 维度分组
	 * @param params
	 * @return
	 */
	List<CounterDaySale> selectGroupShopDaySaleData(@Param("params") Map<String, Object> params);
	/**
	 * 从日结表 根据销售码类型,专柜,销售时间,活动,商品折扣维度分组
	 * @param params
	 * @return
	 */
	List<CounterDaySale> selectGroupShopDaySaleDataForMall(@Param("params") Map<String, Object> params);
	
	/** 券分摊金额
	 * @param query
	 * @return
	 */
	BigDecimal ticketAbsorptionAmonut(@Param("params") Map<String, Object> params);
	
	/** VIP分摊金额
	 * @param query
	 * @return
	 */
	BigDecimal vipAbsorptionAmonut(@Param("params") Map<String, Object> params);
	
	 /**查询净收入和毛收入只差
	 * @param params
	 * @return
	 */
	BigDecimal queryReduceDiffAmount(@Param("params") Map<String, Object> params);
	
	/**
	 * 汇总物料费用
	 * @param params
	 * @return
	 */
	BigDecimal materialAmount(@Param("params") Map<String, Object> params);
	
	/** 积分底现费用
	 * @param params
	 * @return
	 */
	BigDecimal pointsAmount(@Param("params") Map<String, Object> params);
	
	List<CounterDaySale> selectMinimumMall(@Param("params") Map<String, Object> params);
	
	/**
	 * 查询纸袋的数量
	 * @param params
	 * @return
	 */
	Integer selectbagSaleQty(@Param("params") Map<String, Object> params);
	
}

             
             
             
             
             
             
             
             
             
             
             
             
             