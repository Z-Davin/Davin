package topmall.fas.service;


import java.util.List;

import cn.mercury.basic.query.Query;
import topmall.fas.model.MallPrepayDtl;
import topmall.framework.service.IService;

public interface IMallPrepayDtlService extends IService<MallPrepayDtl,String>{ 
	
	/** 根据物业结算单查询物业预付费
	 * @param query
	 * @return
	 */
	public List<MallPrepayDtl> queryByMallBalance(Query query);
    
}







