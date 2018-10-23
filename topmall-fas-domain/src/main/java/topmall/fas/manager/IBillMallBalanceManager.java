package topmall.fas.manager;

import java.util.List;
import cn.mercury.manager.IManager;
import cn.mercury.manager.ManagerException;
import topmall.fas.dto.MallBalanceSummary;
import topmall.fas.model.BillMallBalance;
import topmall.fas.util.CommonResult;

public interface IBillMallBalanceManager extends IManager<BillMallBalance,String>{
	
	/** 保存物业结算单
	 * @param counterBalance
	 * @return
	 */
	public BillMallBalance save(BillMallBalance mallBalance) throws ManagerException;
	
	/** 删除物业结算单
	 * @param billNo
	 * @return
	 */
	public CommonResult deleteBill(String  billNo);
	
	/** 审核物业结算单
	 * @param billNo
	 * @return
	 */
	public CommonResult verify(String  billNo);
	
	/** 物业结算单第一个tab页
	 * @param billNo
	 * @return
	 */
	public List<MallBalanceSummary> getMallBalanceSummary(String billNo);
	
	/** 反审核物业结算单
	 * @param billNo
	 * @return
	 */
	public CommonResult unVerify(String  billNo);
}







