/** build at 2018-05-28 17:19:00 by user **/
package topmall.fas.web.controller;

import java.util.List;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.mercury.annotation.JsonVariable;
import cn.mercury.manager.IManager;
import cn.mercury.utils.JsonUtils;
import topmall.fas.manager.ISupplierAccountManager;
import topmall.fas.model.SupplierAccount;
import topmall.fas.util.CommonResult;

@Controller
@RequestMapping("/supplier/account")
public class SupplierAccountController extends BaseFasController<SupplierAccount,String> {
    @Autowired
    private ISupplierAccountManager manager;
    
    protected IManager<SupplierAccount,String> getManager(){
        return manager;
    }
    
    @Override
	protected String getTemplateFolder() {
		 return "/fas/supplier_account";
	}
    
	/**
	 * @param ids
	 * @param operation 8 启用操作,9是禁用操作
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/operation")
	public CommonResult operation(@JsonVariable String[] ids,int operation) {
		return manager.operation(ids,operation);
	}
	
	@ResponseBody
	@RequestMapping(value = "/import.json")
	public CommonResult importsDetails(@RequestParam String details, HttpServletRequest req)  {
		List<SupplierAccount> dtlList = JsonUtils.fromListJson(details, SupplierAccount.class);
		manager.importBatchSave(dtlList);
		return CommonResult.getSucessResult();
	}
    
}
