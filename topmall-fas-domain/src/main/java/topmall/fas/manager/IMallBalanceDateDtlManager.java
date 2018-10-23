package topmall.fas.manager;

import java.util.Date;

import topmall.fas.dto.ContractMainDto;
import topmall.fas.model.MallBalanceDateDtl;
import cn.mercury.basic.query.Query;
import cn.mercury.manager.IManager;
import cn.mercury.manager.ManagerException;

public interface IMallBalanceDateDtlManager extends IManager<MallBalanceDateDtl, String> {

	/**
	 * 调度任务自动生成物业结算期明细
	 * @throws ManagerException
	 */
	public void generateMallBalanceDateDtl() throws ManagerException;

	/**
	 * 根据物业结算期明细去获取物业有效合同
	 * @param mallBalanceDateDtl 物业结算期明细
	 * @return 物业合同的主合同
	 * @throws Exception
	 */
	public ContractMainDto genarateCreateValidContract(MallBalanceDateDtl mallBalanceDateDtl);
	
	/**
	 * 重新获取有效的合同条款
	 * @param mallBalanceDateDtl 物业结算期明细
	 * @return 物业合同的主合同
	 * @throws Exception
	 */
	public ContractMainDto rebuildValidContract(MallBalanceDateDtl mallBalanceDateDtl);
	
	/**
	 * 查询没有生成费用最小的结算结束时间	
	 * @param query 卖场编码和物业编码
	 * @return 最小的结束时间
	 */
	Date getUnGeneralCostMinDate(Query query);
}
