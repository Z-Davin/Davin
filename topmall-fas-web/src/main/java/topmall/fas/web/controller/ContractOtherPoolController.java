/** build at 2017-09-27 17:57:01 by Administrator **/
package topmall.fas.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import topmall.fas.manager.IContractOtherPoolManager;
import topmall.fas.model.ContractOtherPool;
import topmall.framework.web.controller.BaseController;
import cn.mercury.manager.IManager;

@Controller
@RequestMapping("/contract/other/pool")
public class ContractOtherPoolController extends BaseController<ContractOtherPool,String> {
    @Autowired
    private IContractOtherPoolManager manager;
    
    protected IManager<ContractOtherPool,String> getManager(){
        return manager;
    }
    
    	@Override
	protected String getTemplateFolder() {
		 return "/fas/contract/other/pool";
	}
    
}







