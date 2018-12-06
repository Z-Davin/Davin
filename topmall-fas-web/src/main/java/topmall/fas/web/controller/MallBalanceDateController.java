/** build at 2017-10-12 15:34:38 by Administrator **/
package topmall.fas.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import topmall.fas.manager.IMallBalanceDateManager;
import topmall.fas.model.MallBalanceDate;
import topmall.fas.util.CommonResult;
import cn.mercury.annotation.JsonVariable;
import cn.mercury.manager.IManager;
 
@Controller
@RequestMapping("/mall/balance/date")
public class MallBalanceDateController extends BaseFasController<MallBalanceDate,String> {
    @Autowired
    private IMallBalanceDateManager manager;
    
    protected IManager<MallBalanceDate,String> getManager(){
        return manager;
    }
    
    @Override
	protected String getTemplateFolder() {
		 return "/fas/mall_balance_date";
	}
    
	@ResponseBody
	@RequestMapping("/validateCreate")
	public CommonResult validateCreate(MallBalanceDate mallBalanceDate) {
		return manager.validateCreate(mallBalanceDate);
	}
	
	/**
	 * 确认
	 * 
	 * @param
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/confirm")
	public MallBalanceDate confirm(String id) {
		return manager.confirm(id);
	}
    
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value="/monthBalance")
	public CommonResult monthBalance(@JsonVariable String idList) throws Exception {
		manager.monthBalance(idList);
		return CommonResult.getSucessResult();
	}
	
	/**
	 *  cancel
	 * @param idList
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value="/cancel")
	public CommonResult cancel(@JsonVariable String idList) {
		manager.cancel(idList);
		return CommonResult.getSucessResult();
	}
}
