package topmall.fas.web.controller.report;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import topmall.fas.dto.BalanceSummary;
import topmall.fas.dto.CostTypeDto;
import topmall.fas.dto.PriceTypeDto;
import topmall.fas.manager.report.IBalanceSummaryManager;
import topmall.fas.web.controller.BaseFasController;
import cn.mercury.basic.query.IStatement;
import cn.mercury.basic.query.PageResult;
import cn.mercury.basic.query.Pagenation;
import cn.mercury.basic.query.Q;
import cn.mercury.basic.query.Query;
import cn.mercury.manager.IManager;

@Controller
@RequestMapping("/balance/summary/report")
public class BalanceSummaryReportController extends BaseFasController<BalanceSummary, String> {

	@Autowired
	private IBalanceSummaryManager manager;

	@Override
	protected IManager<BalanceSummary, String> getManager() {
		return manager;
	}

	@Override
	protected String getTemplateFolder() {
		return "/fas/balance_summary";
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/queryPriceTypeList")
	public List<PriceTypeDto> queryPriceTypeList(Query query) {
		handleQuery(query);
		return manager.queryPriceTypeList(query);
	}
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/queryCostTypeList")
	public List<CostTypeDto> queryCostTypeList(Query query) {
		handleQuery(query);
		return manager.queryCostTypeList(query);
	}
	
	/**
	 * 分页查询数据
	 * @param query 查询条件
	 * @param page 分页条件
	 * @return 返回分页数据
	 */
	@ResponseBody
	@RequestMapping("/reportList")
	public PageResult<Map<String, Object>> selectReportByPage(Query query, Pagenation page) {
		handleQuery(query);
		long total = page.getTotal();
		if (total < 0) {
			total = manager.selectCount(query);
		}
		List<Map<String,Object>> rows = manager.selectReportByPage(query, page);
		return new PageResult<>(rows, total);
	}
	
	/**对店铺多选做处理
	 * @param query
	 */
	private void handleQuery(Query query){
		Object shop= query.asMap().get("shopNo");
		if(null!=shop){
			String[] shopList = shop.toString().split(",");
			IStatement is = Q.In("a.shopNo",shopList);
			query.and(is);
			query.and("shopNo", null);
		}
	}
	

}
