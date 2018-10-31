/** build at 2017-10-12 15:34:39 by Administrator **/
package topmall.fas.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import cn.mercury.manager.IManager;
import topmall.fas.model.MallBalanceDateDtl;
import topmall.fas.manager.IMallBalanceDateDtlManager;
 

@Controller
@RequestMapping("/mall/balance/date/dtl")
public class MallBalanceDateDtlController extends BaseFasController<MallBalanceDateDtl,String> {
    @Autowired
    private IMallBalanceDateDtlManager manager;
    
    protected IManager<MallBalanceDateDtl,String> getManager(){
        return manager;
    }
    
    	@Override
	protected String getTemplateFolder() {
		 return "/fas/mall/balance/date/dtl";
	}
    
}
