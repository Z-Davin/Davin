package topmall.fas.service;

import java.math.BigDecimal;

import cn.mercury.basic.query.Query;
import topmall.fas.model.BillMallBalance; 
import topmall.framework.service.IService;

public interface IBillMallBalanceService extends IService<BillMallBalance,String>{    
	
	/** 根据唯一索引查询
	 * @param query
	 * @return
	 */
	public BillMallBalance findByUnique(Query query);
	
	/** 查询物业促销计入保底的金额
	 * @param query
	 * @return
	 */
	public BigDecimal proMinimumSum(Query query);
	
	/**
	 * 年保底金额
	 */
	public BigDecimal yearGuaraSum(Query query);
	
	/**
	 * 物业结算的开票金额
	 */
	public BigDecimal billingSum(Query query);
	
    
}







