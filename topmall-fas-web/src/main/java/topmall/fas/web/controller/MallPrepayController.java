/** build at 2018-04-12 18:07:48 by user **/
package topmall.fas.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.mercury.annotation.JsonVariable;
import cn.mercury.manager.IManager;
import topmall.fas.manager.IMallPrepayManager;
import topmall.fas.model.MallPrepay;
import topmall.fas.util.CommonResult;

/** 物业预交费登记
 * @author zengxa
 *
 */
@Controller
@RequestMapping("/mall/prepay")
public class MallPrepayController extends BaseFasController<MallPrepay,String> {
    @Autowired
    private IMallPrepayManager manager;
    
    protected IManager<MallPrepay,String> getManager(){
        return manager;
    }
    
    @Override
	protected String getTemplateFolder() {
		 return "/fas/mall_prepay";
	}
    
    @ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/save")
	public CommonResult save(@JsonVariable MallPrepay mallPrepay) {
    	return manager.save(mallPrepay);
	}
    
    @ResponseBody
   	@RequestMapping(method = RequestMethod.POST, value = "/deleteBill")
   	public CommonResult deleteBill(String billNo) {
   	 return manager.deleteBill(billNo);
   	}
    
    @ResponseBody
   	@RequestMapping(method = RequestMethod.POST, value = "/verify")
   	public CommonResult verify(String billNo) {
    	return manager.verify(billNo);
   	}
    
}
