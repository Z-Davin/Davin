/** build at 2017-10-16 16:05:31 by Administrator **/
package topmall.fas.web.controller;

import java.util.List;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import topmall.fas.manager.IMallCostManager;
import topmall.fas.model.MallBalanceDateDtl;
import topmall.fas.model.MallCost;
import topmall.fas.util.CommonResult;
import topmall.fas.util.CommonUtil;
import cn.mercury.annotation.JsonVariable;
import cn.mercury.basic.query.PageResult;
import cn.mercury.basic.query.Pagenation;
import cn.mercury.basic.query.Query;
import cn.mercury.manager.IManager;
import cn.mercury.manager.ManagerException;
import cn.mercury.utils.JsonUtils;
 

@Controller
@RequestMapping("/mall/cost")
public class MallCostController extends BaseFasController<MallCost,String> {
    @Autowired
    private IMallCostManager manager;
    
    protected IManager<MallCost,String> getManager(){
        return manager;
    }
    
    protected String getTemplateFolder() {
		 return "/fas/mall_cost";
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
	
	
	/**
	 * 生成物业费用
	 * @param query
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/recalculation")
	public CommonResult recalculationCost(MallBalanceDateDtl mallBalanceDateDtl) {
		return manager.recalculationCost(mallBalanceDateDtl);
	}
    
	/**
	 * 查询物业结算期
	 * @param query 查询条件
	 * @return 返回物业结算期列表
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/settle")
	public List<MallBalanceDateDtl> findBySettle(Query query) {
		return manager.findBySettle(query);
	}
	
	/**
	 * 导入物业费用
	 * @param details 到入对象的Json串
	 * @param req http请求
	 * @return 导入结果
	 * @throws ManagerException
	 */
	@ResponseBody
	@RequestMapping(value = "/import")
	public CommonResult importsDetails(@RequestParam String details, HttpServletRequest req) {
		List<MallCost> dtlList = JsonUtils.fromListJson(details, MallCost.class);
		manager.importBatchSave(dtlList);
		return CommonResult.getSucessResult();
	}
	
	@RequestMapping("/list")
	@Override
	public PageResult<MallCost> selectByPage(Query query, Pagenation page) {
		long total = page.getTotal();
		if (total < 0) {
			total = getManager().selectCount(query);
		}
		if(!CommonUtil.hasValue(query.getSort())){
			query.orderby("update_time",true);
		}
		List<MallCost> rows = getManager().selectByPage(query, page);
		List<MallCost> saleCost = manager.queryConditionSum(query);
		return new PageResult<>(rows, total,saleCost);
	}
	
}
