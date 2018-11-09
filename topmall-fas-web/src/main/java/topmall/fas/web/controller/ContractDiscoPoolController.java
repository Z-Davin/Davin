/** build at 2017-09-27 17:56:54 by Administrator **/
package topmall.fas.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import topmall.fas.manager.IContractDiscoPoolManager;
import topmall.fas.model.ContractDiscoPool;
import topmall.framework.web.controller.BaseController;
import cn.mercury.manager.IManager;

@Controller
@RequestMapping("/contract/disco/pool")
public class ContractDiscoPoolController extends BaseController<ContractDiscoPool,String> {
    @Autowired
    private IContractDiscoPoolManager manager;
    
    protected IManager<ContractDiscoPool,String> getManager(){
        return manager;
    }
    
    	@Override
	protected String getTemplateFolder() {
		 return "/fas/contract/disco/pool";
	}
    
}







