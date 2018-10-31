package topmall.fas.manager.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.mercury.basic.query.Q;
import cn.mercury.basic.query.Query;
import cn.mercury.manager.ManagerException;
import topmall.fas.enums.StatusEnums;
import topmall.fas.manager.ISupplierAccountManager;
import topmall.fas.model.SupplierAccount;
import topmall.fas.service.ISupplierAccountService;
import topmall.fas.util.CommonResult;
import topmall.framework.manager.BaseManager;
import topmall.framework.security.Authorization;
import topmall.framework.service.IService;

@Service
public class SupplierAccountManager extends BaseManager<SupplierAccount,String> implements ISupplierAccountManager{
    @Autowired
    private ISupplierAccountService service;
    
    protected IService<SupplierAccount,String> getService(){
        return service;
    }
    
  	@Override
  	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
  	public Integer insert(SupplierAccount entry) {
  		if (entry.getStatus() == null) {
  			entry.setStatus(StatusEnums.DISABLE.getStatus());
  		}
  		return super.insert(entry);
  	}
  	
  	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public CommonResult operation(String[] ids,int operation) {
		Integer result=0;
		if(operation==8){
			for(String id :ids){
				SupplierAccount supplierAccount =service.findByPrimaryKey(id);
				List<SupplierAccount> list = service.selectByParams(Q.q("counterNo", supplierAccount.getCounterNo()));
				for(SupplierAccount account :list){
					if(account.getStatus()==StatusEnums.ENABLE.getStatus()){
						throw new ManagerException("专柜:"+account.getCounterNo()+"已经存在启用的数据");
					}
				}
				supplierAccount.setAuditor(Authorization.getUser().getName());
				supplierAccount.setAuditTime(new Date());
				supplierAccount.setStatus(StatusEnums.ENABLE.getStatus());
				this.service.update(supplierAccount);
			}
		}else{
			Query query = Query.empty().and("keyValue", ids).and("operate", "auditor").and("operateTime", "audit_time")
					.and("user", Authorization.getUser().getName()).and("time", new Date())
					.and("status", StatusEnums.DISABLE.getStatus())
					.and("originStatus", StatusEnums.ENABLE.getStatus()).and("keyName", "id");
			result = this.service.updateStatus(query);
			if (result != ids.length) {
				throw new ManagerException("所选数据中存在状态不正确的数据，不能操作。");
			}
		}
		return CommonResult.sucess("successful");
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public void importBatchSave(List<SupplierAccount> list) {
		for(SupplierAccount supplierAccount:list){
			Query query = Query.Where("counterNo", supplierAccount.getCounterNo());
			SupplierAccount data = service.findByParam(query);
			if(null!=data){
				throw new ManagerException("专柜"+supplierAccount.getCounterNo()+"信息已存在!");
			}
			insert(supplierAccount);
		}
	}
    
}







