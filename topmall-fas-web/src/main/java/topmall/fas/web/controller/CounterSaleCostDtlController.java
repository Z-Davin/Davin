/** build at 2017-09-27 17:57:18 by Administrator **/
package topmall.fas.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import topmall.fas.manager.ICounterSaleCostDtlManager;
import topmall.fas.model.CounterSaleCostDtl;
import cn.mercury.manager.IManager;

@Controller
@RequestMapping("/counter/sale/cost/dtl")
public class CounterSaleCostDtlController extends BaseFasController<CounterSaleCostDtl,String> {
    @Autowired
    private ICounterSaleCostDtlManager manager;
    
    protected IManager<CounterSaleCostDtl,String> getManager(){
        return manager;
    }
    
    	@Override
	protected String getTemplateFolder() {
		 return "/fas/counter/sale/cost/dtl";
	}
    
}







