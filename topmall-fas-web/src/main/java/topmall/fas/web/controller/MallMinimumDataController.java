/** build at 2018-09-11 14:37:15 by dj370 **/
package topmall.fas.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.mercury.manager.IManager;
import topmall.fas.manager.IMallMinimumDataManager;
import topmall.fas.model.MallMinimumData;
import topmall.framework.web.controller.BaseController;

@Controller
@RequestMapping("/mall/minimum/data")
public class MallMinimumDataController extends BaseController<MallMinimumData,String> {
    @Autowired
    private IMallMinimumDataManager manager;
    
    protected IManager<MallMinimumData,String> getManager(){
        return manager;
    }
    
    @Override
	protected String getTemplateFolder() {
		 return "/mall/minimum/data";
	}
    
}







