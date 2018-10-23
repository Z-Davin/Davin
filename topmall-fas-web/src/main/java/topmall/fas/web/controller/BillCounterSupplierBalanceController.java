package topmall.fas.web.controller;

/** build at 2017-08-11 11:05:04 by Administrator **/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.mercury.annotation.JsonVariable;
import cn.mercury.manager.IManager;
import topmall.fas.manager.IBillCounterBalanceManager;
import topmall.fas.model.BillCounterBalance;
import topmall.fas.util.CommonResult;
import topmall.fas.util.PublicConstans;

@Controller
@RequestMapping("/bill/counter/supplier/balance")
public class BillCounterSupplierBalanceController extends BillCounterBalanceController {
	@Autowired
	private IBillCounterBalanceManager manager;
	@Override
	protected IManager<BillCounterBalance, String> getManager() {
		return manager;
	}
	@Override
	protected String getTemplateFolder() {
		return "/fas/counter_supplier_balance";
	}

	@Override
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/save")
	public CommonResult save(@JsonVariable BillCounterBalance billCounterBalance) {
		billCounterBalance = manager.save(billCounterBalance,2);
		if (null == billCounterBalance) {
			return CommonResult.error(PublicConstans.SYSTEM_ERROR, "该专柜已生成结算单或者还未生成费用");
		} else {
			return CommonResult.sucess(billCounterBalance);
		}
	}
	
}
