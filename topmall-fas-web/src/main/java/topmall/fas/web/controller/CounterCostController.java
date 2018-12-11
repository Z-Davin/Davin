/** build at 2017-08-04 11:15:24 by Administrator **/
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
import cn.mercury.basic.query.PageResult;
import cn.mercury.basic.query.Pagenation;
import cn.mercury.basic.query.Query;
import cn.mercury.manager.IManager;
import cn.mercury.utils.JsonUtils;
import topmall.common.enums.BillTypeEnums;
import topmall.fas.enums.TaxFlagEnums;
import topmall.fas.manager.ICounterCostManager;
import topmall.fas.manager.impl.CommonStaticManager;
import topmall.fas.model.CounterCost;
import topmall.fas.model.ShopBalanceDateDtl;
import topmall.fas.util.CommonResult;
import topmall.fas.util.CommonUtil;

@Controller
@RequestMapping("/counter/cost")
public class CounterCostController extends BaseFasController<CounterCost, String> {
	@Autowired
	private ICounterCostManager manager;

	protected IManager<CounterCost, String> getManager() {
		return manager;
	}

	@Override
	protected String getTemplateFolder() {
		return "/fas/counter_cost";
	}

	/**
	 * 确认
	 * 
	 * @param
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/confirm")
	public CommonResult confirm(@JsonVariable String[] ids) {
		return manager.batchConfirm(ids);
	}

	/**
	 * 反确认
	 * 
	 * @param
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/unConfirm")
	public CommonResult unConfirm(@JsonVariable String[] ids) {
		return manager.batchUnConfirm(ids);
	}

	@ResponseBody
	@RequestMapping(value = "/import")
	public CommonResult importsDetails(@RequestParam String details, HttpServletRequest req)  {
		List<CounterCost> dtlList = JsonUtils.fromListJson(details, CounterCost.class);
		manager.importBatchSave(dtlList, null, null);
		return CommonResult.getSucessResult();
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/settle")
	public List<ShopBalanceDateDtl> findBySettle(Query query) {
		return manager.findBySettle(query);
	}

	/**
	 * 生成专柜销售费用
	 * @param query
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/recalculation")
	public CommonResult recalculationCost(ShopBalanceDateDtl shopBalanceDateDtl) {
		return manager.recalculationCost(shopBalanceDateDtl);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/create")
	@Override
	public CounterCost create(CounterCost entry) {
		if (entry.getTaxFlag().equals(TaxFlagEnums.NOT_INCLUDE.getFlag())) {
			//不含税 设置able_amount（应结价款）
			// 应结总额 (含税金额)= 应结价款(不含税金额)*(100+税率)/100 
			entry.setAbleSum(CommonUtil.getTaxCost(entry.getCostAmount(), entry.getTaxRate()));
			entry.setAbleAmount(entry.getCostAmount());
		} else {
			// 公式： 应结价款(不含税金额) = 应结总额(含税金额)/(100+税率)*100
			entry.setAbleAmount(CommonUtil.getTaxFreeCost(entry.getCostAmount(), entry.getTaxRate()));
			entry.setAbleSum(entry.getCostAmount());
		}
		manager.insert(entry);
		return entry;
	}
	
	
	/**
	 * 生成专柜销售费用
	 * @param query
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/recalculateHis")
	public CommonResult recalculateHisCost(@JsonVariable ShopBalanceDateDtl shopBalanceDateDtl, String newSettleMonth) {
		String billNo = shopBalanceDateDtl.getCounterNo() + "-" + newSettleMonth;
		CommonStaticManager.insertUnAccountBill(billNo,shopBalanceDateDtl.getZoneNo() ,BillTypeEnums.RECALULATE_DAY_SALE,Integer.parseInt(shopBalanceDateDtl.getSettleMonth()),0,1);
		return CommonResult.getSucessResult();
	}
	
	
	@RequestMapping("/list")
	@Override
	public PageResult<CounterCost> selectByPage(Query query, Pagenation page) {
		long total = page.getTotal();
		if (total < 0) {
			total = getManager().selectCount(query);
		}
		if(!CommonUtil.hasValue(query.getSort())){
			query.orderby("update_time",true);
		}
		List<CounterCost> rows = getManager().selectByPage(query, page);
		List<CounterCost> saleCost = manager.queryConditionSum(query);
		return new PageResult<>(rows, total,saleCost);
	}
}