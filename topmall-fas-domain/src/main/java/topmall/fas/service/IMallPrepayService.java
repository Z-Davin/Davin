package topmall.fas.service;


import cn.mercury.basic.query.Query;
import topmall.fas.model.MallPrepay;
import topmall.framework.service.IService;

public interface IMallPrepayService extends IService<MallPrepay,String>{  
	
	/** 根据唯一索引查询
	 * @param query
	 * @return
	 */
	public MallPrepay findByUnique(Query query);
    
}







