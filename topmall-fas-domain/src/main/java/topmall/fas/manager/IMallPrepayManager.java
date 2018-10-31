package topmall.fas.manager;

import cn.mercury.manager.IManager;
import topmall.fas.model.MallPrepay;
import topmall.fas.util.CommonResult;

public interface IMallPrepayManager extends IManager<MallPrepay,String>{
	
	public CommonResult save(MallPrepay mallPrepay);
	
	/** 删除
	 * @param billNo
	 * @return
	 */
	public CommonResult deleteBill(String  billNo);
	
	/** 审核
	 * @param billNo
	 * @return
	 */
	public CommonResult verify(String  billNo);
    
}







