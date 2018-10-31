package topmall.fas.manager;

import java.util.List;

import cn.mercury.manager.IManager;
import topmall.fas.model.SupplierAccount;
import topmall.fas.util.CommonResult;

public interface ISupplierAccountManager extends IManager<SupplierAccount,String>{
	
	/**
	 * @param ids
	 * @param operation operation 8 启用操作,9是禁用操作
	 * @return
	 */
	public CommonResult operation(String[] ids,int operation);
	
	/** 导入
	 * @param list
	 */
	public void importBatchSave(List<SupplierAccount> list);
}







