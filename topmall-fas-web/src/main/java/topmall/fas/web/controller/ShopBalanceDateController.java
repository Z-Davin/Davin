/** build at 2017-08-22 10:50:53 by Administrator **/
package topmall.fas.web.controller;

import java.util.List;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.mercury.annotation.JsonVariable;
import cn.mercury.manager.IManager;
import cn.mercury.utils.JsonUtils;
import topmall.fas.manager.IShopBalanceDateManager;
import topmall.fas.model.ShopBalanceDate;
import topmall.fas.util.CommonResult;

@Controller
@RequestMapping("/shop/balance/date")
public class ShopBalanceDateController extends BaseFasController<ShopBalanceDate, String> {
	@Autowired
	private IShopBalanceDateManager manager;

	protected IManager<ShopBalanceDate, String> getManager() {
		return manager;
	}

	protected String getTemplateFolder() {
		return "/fas/shop_balance_date";
	}

	@ResponseBody
	@RequestMapping("/validateCreate")
	public CommonResult validateCreate(ShopBalanceDate shopBalanceDate) {
		return manager.validateCreate(shopBalanceDate);
	}

	/**
	 * 确认
	 * 
	 * @param
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/confirm")
	public ShopBalanceDate confirm(String id) {
		return manager.confirm(id);
	}

	@ResponseBody
	@RequestMapping(value = "/import")
	public CommonResult importsDetails(@RequestParam String details,
			HttpServletRequest req) {
		List<ShopBalanceDate> dtlList = JsonUtils.fromListJson(details,ShopBalanceDate.class);
		manager.batchSave(dtlList, null, null);
		return CommonResult.getSucessResult();
	}
	
	@ResponseBody
	@RequestMapping(value="/monthBalance")
	public CommonResult monthBalance(@JsonVariable String ids,int targetStatus){
		manager.monthBalance(ids,targetStatus);
		return CommonResult.getSucessResult();
	}
}
