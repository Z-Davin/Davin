/** build at 2017-08-24 17:37:40 by Administrator **/
package topmall.fas.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.mercury.manager.IManager;
import cn.mercury.utils.JsonUtils;
import topmall.fas.manager.IShopBalanceDateDtlManager;
import topmall.fas.manager.IShopBalanceDateManager;
import topmall.fas.model.ShopBalanceDate;
import topmall.fas.model.ShopBalanceDateDtl;
import topmall.fas.util.CommonResult;

/**
 * 结算日期设置明细 dai.xw
 */
@Controller
@RequestMapping("/shop/balance/date/dtl")
public class ShopBalanceDateDtlController extends BaseFasController<ShopBalanceDateDtl, String> {
	@Autowired
	private IShopBalanceDateDtlManager manager;
	@Autowired
	private IShopBalanceDateManager shopBalanceDateManager;

	protected IManager<ShopBalanceDateDtl, String> getManager() {
		return manager;
	}

	@Override
	protected String getTemplateFolder() {
		return "/fas/shop/balance/date/dtl";
	}

	/**
	 * 根据门店结算期设置表生成门店结算期设置表明细
	 * @param billCounterBalance
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/generate")
	public void generate(ShopBalanceDate shopBalanceDate) {
		shopBalanceDate = shopBalanceDateManager.findByPrimaryKey(shopBalanceDate.getId());
		manager.generateShopBalanceDateDtl(shopBalanceDate);
	}

	@ResponseBody
	@RequestMapping(value = "/import")
	public CommonResult importsDetails(@RequestParam String details) {
		List<ShopBalanceDateDtl> dtlList = JsonUtils.fromListJson(details, ShopBalanceDateDtl.class);
		manager.modifyBalanceDateDtl(dtlList);
		return CommonResult.getSucessResult();
	}
}
