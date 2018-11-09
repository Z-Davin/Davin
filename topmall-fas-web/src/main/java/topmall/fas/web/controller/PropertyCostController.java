/** build at 2017-11-14 16:42:23 by Administrator **/
package topmall.fas.web.controller;

import java.util.List;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import topmall.fas.manager.IPropertyCostManager;
import topmall.fas.model.PropertyCost;
import topmall.fas.util.CommonResult;
import cn.mercury.manager.IManager;
import cn.mercury.utils.JsonUtils;


/**
 * 
 * 物业水电登记Controller
 * 
 * @author dai.j
 * @date 2017-11-15 上午11:09:30
 * @version 0.1.0 
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
@Controller
@RequestMapping("/property/cost")
public class PropertyCostController extends BaseFasController<PropertyCost,String> {
    @Autowired
    private IPropertyCostManager manager;
    
    protected IManager<PropertyCost,String> getManager(){
        return manager;
    }
    
    @Override
	protected String getTemplateFolder() {
		 return "/fas/property_cost";
	}
    
    
    @ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/confirm")
	public PropertyCost confirm(String id) {
		return manager.confirm(id);
	}
    
    @ResponseBody
	@RequestMapping(value = "/import")
	public CommonResult importsDetails(@RequestParam String details, HttpServletRequest req)  {
		List<PropertyCost> dtlList = JsonUtils.fromListJson(details, PropertyCost.class);
		manager.batchSave(dtlList, null, null);
		return CommonResult.getSucessResult();
	}
}
