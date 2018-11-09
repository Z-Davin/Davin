/** build at 2017-09-21 14:28:45 by Administrator **/
package topmall.fas.web.controller;

import java.util.List;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import topmall.fas.manager.IShopCostManager;
import topmall.fas.model.ShopCost;
import topmall.fas.util.CommonResult;
import cn.mercury.manager.IManager;
import cn.mercury.utils.JsonUtils;

@Controller
@RequestMapping("/shop/cost")
public class ShopCostController extends BaseFasController<ShopCost,String> {
    @Autowired
    private IShopCostManager manager;
    
    protected IManager<ShopCost,String> getManager(){
        return manager;
    }
    
    @Override
	protected String getTemplateFolder() {
		 return "/fas/shop_cost";
	}
    
    /**
	 * 确认
	 * 
	 * @param
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/confirm")
	public ShopCost confirm(String id) {
		return manager.confirm(id);
	}
	
	@ResponseBody
	@RequestMapping(value = "/import")
	public CommonResult importsDetails(@RequestParam String details, HttpServletRequest req)  {
		List<ShopCost> dtlList = JsonUtils.fromListJson(details, ShopCost.class);
		manager.batchSave(dtlList, null, null);
		return CommonResult.getSucessResult();
	}

}
