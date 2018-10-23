/** build at 2017-10-19 14:19:45 by Administrator **/
package topmall.fas.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.mercury.basic.query.PageResult;
import cn.mercury.basic.query.Pagenation;
import cn.mercury.basic.query.Query;
import cn.mercury.manager.IManager;
import topmall.fas.model.MallSaleCost;
import topmall.fas.util.CommonUtil;
import topmall.fas.manager.IMallSaleCostManager;
 

@Controller
@RequestMapping("/mall/sale/cost")
public class MallSaleCostController extends BaseFasController<MallSaleCost,String> {
    @Autowired
    private IMallSaleCostManager manager;
    
    protected IManager<MallSaleCost,String> getManager(){
        return manager;
    }
    
    @Override
	protected String getTemplateFolder() {
		 return "/fas/mall/sale/cost";
	}
    
    
    @ResponseBody
	@RequestMapping("/list")
	@Override
	public PageResult<MallSaleCost> selectByPage(Query query, Pagenation page) {

		long total = page.getTotal();
		if (total < 0) {
			total = manager.selectCount(query);
		}
		
		if(!CommonUtil.hasValue(query.getSort())){
			query.orderby("update_time,id",true);
		}
		List<MallSaleCost> rows = manager.selectByPage(query, page);
		List<MallSaleCost> footer = manager.queryConditionSum(query);
		return new PageResult<>(rows, total,footer);
	}
    
}
