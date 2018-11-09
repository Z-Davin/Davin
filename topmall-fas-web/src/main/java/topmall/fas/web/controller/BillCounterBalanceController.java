package topmall.fas.web.controller;

/** build at 2017-08-11 11:05:04 by Administrator **/

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.mercury.annotation.JsonVariable;
import cn.mercury.basic.query.Query;
import cn.mercury.manager.IManager;
import topmall.fas.dto.BatchCounterBalanceDto;
import topmall.fas.dto.CounterBalancePrint;
import topmall.fas.manager.IBillCounterBalanceManager;
import topmall.fas.model.BillCounterBalance;
import topmall.fas.util.CommonResult;
import topmall.fas.util.PublicConstans;

@Controller
@RequestMapping("/bill/counter/balance")
public class BillCounterBalanceController extends BaseFasController<BillCounterBalance, String> {
	@Autowired
	private IBillCounterBalanceManager manager;

	protected IManager<BillCounterBalance, String> getManager() {
		return manager;
	}

	@Override
	protected String getTemplateFolder() {
		return "/fas/counter_balance";
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/save")
	public CommonResult save(@JsonVariable BillCounterBalance billCounterBalance) {
		billCounterBalance = manager.save(billCounterBalance,1);
		if (null == billCounterBalance) {
			return CommonResult.error(PublicConstans.SYSTEM_ERROR, "该专柜已生成结算单或者还未生成费用");
		} else {
			return CommonResult.sucess(billCounterBalance);
		}
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
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/printCount")
	public void printCount(String billNo) {
		 manager.printCount(billNo);
	}
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/unVerify")
	public CommonResult unVerify(String billNo) {
		return manager.unVerify(billNo);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/batchverify")
	public CommonResult batchVerify(@JsonVariable String[] billNos) {
		return manager.batchVerify(billNos);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/batchCreate")
	public CommonResult batchCreate(@JsonVariable BatchCounterBalanceDto batchCounterBalanceDto) {

		return manager.batchCreate(batchCounterBalanceDto);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/print")
	public CounterBalancePrint print(String billNo, Integer templateType) {
		return manager.print(billNo, templateType);
	}
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/batchprint/{templateType}")
	public List<CounterBalancePrint> bacthPrint(@PathVariable("templateType") Integer templateType, Query query) {
		return manager.batchPrint(query, templateType);
	}

}
