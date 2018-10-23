/** build at 2017-12-20 11:34:50 by Administrator **/
package topmall.fas.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import cn.mercury.manager.IManager;
import topmall.fas.model.SystemConfig;
import topmall.fas.manager.ISystemConfigManager;

@Controller
@RequestMapping("/system/config")
public class SystemConfigController extends BaseFasController<SystemConfig,String> {
    @Autowired
    private ISystemConfigManager manager;
    
    protected IManager<SystemConfig,String> getManager(){
        return manager;
    }
    
    @Override
	protected String getTemplateFolder() {
		 return "/system/config";
	}
    
}







