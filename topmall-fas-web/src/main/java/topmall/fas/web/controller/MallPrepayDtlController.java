/** build at 2018-06-12 11:27:49 by user **/
package topmall.fas.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import cn.mercury.manager.IManager;
import topmall.fas.manager.IMallPrepayDtlManager;
import topmall.fas.model.MallPrepayDtl;
 
@Controller
@RequestMapping("/mall/prepay/dtl")
public class MallPrepayDtlController extends BaseFasController<MallPrepayDtl,String> {
    @Autowired
    private IMallPrepayDtlManager manager;
    
    protected IManager<MallPrepayDtl,String> getManager(){
        return manager;
    }
    
    	@Override
	protected String getTemplateFolder() {
		 return "/fas/mall/prepay/dtl";
	}
    
}
