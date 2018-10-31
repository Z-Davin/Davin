package topmall.fas.manager;

import java.util.List;

import cn.mercury.manager.IManager;
import cn.mercury.manager.ManagerException;
import topmall.fas.dto.ContractMainDto;
import topmall.fas.model.ShopBalanceDate;
import topmall.fas.model.ShopBalanceDateDtl;

/**
 * dai.xw
 */
public interface IShopBalanceDateDtlManager extends IManager<ShopBalanceDateDtl, String> {
	/** 
	 * 根据门店结算期设置表生成门店结算期设置表明细
	 * @param shopBalanceDate
	 * @return
	 * @throws ManagerException
	 */
	public ShopBalanceDate generateShopBalanceDateDtl(ShopBalanceDate shopBalanceDate);

	/** 
	 * 根据门店结算期设置表生成门店结算期设置表明细 自动全量生成（调度任务使用）
	 * @return
	 * @throws ManagerException
	 */
	public void generateShopBalanceDateDtlAuto();

	/**
	 * 根据结算期明细自动获取保存有效合同条款
	 * @param shopBalanceDateDtl 结算期明细
	 * @return 返回是否有取到有效合同
	 */
	public ContractMainDto genarateCreateValidContract(ShopBalanceDateDtl shopBalanceDateDtl);

	/**
	 * 重新获取有效的合同条款 (重算费用使用)
	 * @param shopBalanceDateDtl 结算期明细
	 * @return 返回是否有取到有效合同
	 */
	public ContractMainDto rebuildValidContract(ShopBalanceDateDtl shopBalanceDateDtl);
	
	/**
	 * 根据导入的结算期明细修改结算期明细
	 * @param dtlList 导入的结算期明细列表
	 */
	void modifyBalanceDateDtl(List<ShopBalanceDateDtl> dtlList);
}
