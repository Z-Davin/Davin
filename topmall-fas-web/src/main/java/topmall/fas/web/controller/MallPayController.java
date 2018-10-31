/** build at 2017-11-16 17:35:59 by user **/
package topmall.fas.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import topmall.fas.manager.IMallPayManager;
import topmall.fas.model.MallPay;
import cn.mercury.manager.IManager;

@Controller
@RequestMapping("/mall/pay")
public class MallPayController extends BaseFasController<MallPay,String> {
    @Autowired
    private IMallPayManager manager;
    
    protected IManager<MallPay,String> getManager(){
        return manager;
    }
    
    @Override
	protected String getTemplateFolder() {
		 return "/fas/mall/pay";
	}
    
}
