package topmall.fas.service;


import cn.mercury.basic.query.Query;
import topmall.fas.model.SupplierAccount;
import topmall.framework.service.IService;

public interface ISupplierAccountService extends IService<SupplierAccount,String>{
	
	/**
	 * 更新状态
	 * @param query
	 */
	public Integer updateStatus(Query query);
	
    
}







