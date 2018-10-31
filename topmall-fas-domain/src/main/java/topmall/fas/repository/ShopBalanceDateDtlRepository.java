package topmall.fas.repository;
import java.util.Date;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import topmall.fas.dto.ContractMainDto;
import topmall.fas.model.ShopBalanceDateDtl;
import topmall.framework.repository.IRepository;
/**
 * dai.xw
 */
@Mapper
public interface ShopBalanceDateDtlRepository extends IRepository<ShopBalanceDateDtl,String> {
	
	/**
	 * 根据店铺编码和专柜编码获取有效合同编号
	 * @param params 店铺编码和专柜编码
	 * @return 专柜合同的主要数据
	 */
	ContractMainDto selectContractBillNo(@Param("params") Map<String, Object> params);
	
	/**
	 * 根据店铺编码、专柜编码、结算月、结算开始日期、结算结束日期 获取唯一的结算期
	 * @param params 查询条件
	 * @return 结算期明细
	 */
	ShopBalanceDateDtl findByUnique(@Param("params") Map<String, Object> params);
	
	/** 查询没有生成费用最小的结算期
	 * @param query
	 * @return
	 */
	String getUnGeneralCostMinMonth(@Param("params") Map<String, Object> params);
	
	/**
	 * 查询没有生成费用的最大时间
	 */
	Date getUnGeneralCostMaxDate(@Param("params") Map<String, Object> params);
}

             
             
             
             
             
             
             
             
             
             
             
             
             