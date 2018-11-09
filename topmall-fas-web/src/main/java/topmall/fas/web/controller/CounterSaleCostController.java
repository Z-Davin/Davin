/** build at 2017-08-17 11:50:08 by Administrator **/
package topmall.fas.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import topmall.fas.manager.ICounterSaleCostManager;
import topmall.fas.model.CounterSaleCost;
import cn.mercury.basic.query.PageResult;
import cn.mercury.basic.query.Pagenation;
import cn.mercury.basic.query.Query;
import cn.mercury.manager.IManager;

@Controller
@RequestMapping("/counter/sale/cost")
public class CounterSaleCostController extends BaseFasController<CounterSaleCost, String> {
	@Autowired
	private ICounterSaleCostManager manager;

	protected IManager<CounterSaleCost, String> getManager() {
		return manager;
	}

	@Override
	protected String getTemplateFolder() {
		return "/counter/sale/";
	}
	
	@ResponseBody
	@RequestMapping("/list")
	@Override
	public PageResult<CounterSaleCost> selectByPage(Query query, Pagenation page) {
		long total = page.getTotal();
		if (total < 0) {
			total = getManager().selectCount(query);
		}
		query.orderby("update_time",true);
		List<CounterSaleCost> rows = getManager().selectByPage(query, page);
		List<CounterSaleCost> saleCost = manager.queryConditionSum(query);
		return new PageResult<>(rows, total,saleCost);
	}

}
