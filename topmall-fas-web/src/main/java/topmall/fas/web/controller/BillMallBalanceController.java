/** build at 2017-10-25 10:22:04 by Administrator **/
package topmall.fas.web.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import topmall.fas.dto.MallBalanceSummary;
import topmall.fas.manager.IBillMallBalanceManager;
import topmall.fas.model.BillMallBalance;
import topmall.fas.util.CommonResult;
import topmall.fas.util.PublicConstans;
import cn.mercury.annotation.JsonVariable;
import cn.mercury.manager.IManager;

@Controller
@RequestMapping("/bill/mall/balance")
public class BillMallBalanceController extends BaseFasController<BillMallBalance,String> {
    @Autowired
    private IBillMallBalanceManager manager;
    
    protected IManager<BillMallBalance,String> getManager(){
        return manager;
    }
    
    @Override
	protected String getTemplateFolder() {
		 return "/fas/mall_balance";
	}
    
    @ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/save")
	public CommonResult save(@JsonVariable BillMallBalance billMallBalance) {
    	billMallBalance = manager.save(billMallBalance);
    	if (null == billMallBalance) {
			return CommonResult.error(PublicConstans.SYSTEM_ERROR, "该结算期已生成结算单或者还未生成费用");
		} else {
			return CommonResult.sucess(billMallBalance);
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
	@RequestMapping(method = RequestMethod.POST, value = "/unVerify")
	public CommonResult unVerify(String billNo) {
		return manager.unVerify(billNo);
	}
    
    @ResponseBody
   	@RequestMapping(value = "/summary")
   	public List<MallBalanceSummary> get(String balanceBillNo) {
   	 return manager.getMallBalanceSummary(balanceBillNo);
   	}
    
}

