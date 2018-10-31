/** build at 2017-09-27 17:57:09 by Administrator **/
package topmall.fas.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import topmall.fas.manager.IContractRentPoolManager;
import topmall.fas.model.ContractRentPool;
import topmall.framework.web.controller.BaseController;
import cn.mercury.manager.IManager;

@Controller
@RequestMapping("/contract/rent/pool")
public class ContractRentPoolController extends BaseController<ContractRentPool,String> {
    @Autowired
    private IContractRentPoolManager manager;
    
    protected IManager<ContractRentPool,String> getManager(){
        return manager;
    }
    
    	@Override
	protected String getTemplateFolder() {
		 return "/fas/contract/rent/pool";
	}
    
}







