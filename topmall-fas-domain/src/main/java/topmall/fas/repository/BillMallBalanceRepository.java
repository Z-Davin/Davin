package topmall.fas.repository;
import topmall.fas.model.BillMallBalance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;
import java.util.Map;
import topmall.framework.repository.IRepository;
@Mapper
public interface BillMallBalanceRepository extends IRepository<BillMallBalance,String> {
	
	/** 根据唯一索引查询
	 * @param query
	 * @return
	 */
	public BillMallBalance findByUnique(@Param("params") Map<String, Object> params);
	
	
	/** 查询物业促销计入保底的金额
	 * @param params
	 * @return
	 */
	public BigDecimal proMinimumSum(@Param("params") Map<String, Object> params);
	
	/**
	 * 年保底金额
	 * @param params
	 * @return 
	 */
	public BigDecimal yearGuaraSum(@Param("params") Map<String, Object> params);
	
	
	/** 开票金额
	 * @param params
	 * @return
	 */
	public BigDecimal billingSum(@Param("params") Map<String, Object> params);
}

