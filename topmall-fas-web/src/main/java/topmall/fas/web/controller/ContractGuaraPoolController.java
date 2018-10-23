/** build at 2017-09-27 17:56:57 by Administrator **/
package topmall.fas.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import topmall.fas.manager.IContractGuaraPoolManager;
import topmall.fas.model.ContractGuaraPool;
import topmall.framework.web.controller.BaseController;
import cn.mercury.manager.IManager;

@Controller
@RequestMapping("/contract/guara/pool")
public class ContractGuaraPoolController extends BaseController<ContractGuaraPool,String> {
    @Autowired
    private IContractGuaraPoolManager manager;
    
    protected IManager<ContractGuaraPool,String> getManager(){
        return manager;
    }
    
    	@Override
	protected String getTemplateFolder() {
		 return "/fas/contract/guara/pool";
	}
    
}







