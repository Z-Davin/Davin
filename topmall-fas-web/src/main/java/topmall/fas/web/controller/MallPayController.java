/** build at 2017-11-16 17:35:59 by user **/
package topmall.fas.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import topmall.fas.manager.IMallPayManager;
import topmall.fas.model.MallPay;
import topmall.fas.util.CommonUtil;
import cn.mercury.basic.query.PageResult;
import cn.mercury.basic.query.Pagenation;
import cn.mercury.basic.query.Query;
import cn.mercury.manager.IManager;

@Controller
@RequestMapping("/mall/pay")
public class MallPayController extends BaseFasController<MallPay,String> {
    @Autowired
    private IMallPayManager manager;
    
    protected IManager<MallPay,String> getManager(){
        return manager;
    }
    
    @Override
	protected String getTemplateFolder() {
		 return "/fas/mall/pay";
	}
    
    @ResponseBody
   	@RequestMapping("/list")
   	@Override
   	public PageResult<MallPay> selectByPage(Query query, Pagenation page) {

   		long total = page.getTotal();
   		if (total < 0) {
   			total = manager.selectCount(query);
   		}
   		
   		if(!CommonUtil.hasValue(query.getSort())){
   			query.orderby("update_time,id",true);
   		}
   		List<MallPay> rows = manager.selectByPage(query, page);
   		List<MallPay> footer = manager.queryConditionSum(query);
   		return new PageResult<>(rows, total,footer);
   	}
    
}
