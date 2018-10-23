/** build at 2017-09-27 17:57:05 by Administrator **/
package topmall.fas.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import topmall.fas.manager.IContractProfitPoolManager;
import topmall.fas.model.ContractProfitPool;
import topmall.framework.web.controller.BaseController;
import cn.mercury.manager.IManager;

@Controller
@RequestMapping("/contract/profit/pool")
public class ContractProfitPoolController extends BaseController<ContractProfitPool,String> {
    @Autowired
    private IContractProfitPoolManager manager;
    
    protected IManager<ContractProfitPool,String> getManager(){
        return manager;
    }
    
    	@Override
	protected String getTemplateFolder() {
		 return "/fas/contract/profit/pool";
	}
    
}







