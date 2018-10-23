package topmall.fas.manager;

import java.util.List;

import topmall.fas.model.ShopBalanceDateDtl;
import topmall.fas.model.ShopCost;
import cn.mercury.manager.IManager;

public interface IShopCostManager extends IManager<ShopCost,String>{
    
	/** 专柜费用确认
	 * @param id
	 * @return
	 */
	public ShopCost confirm(String id);
	
	/**
	 * 根据结算期明细获取结算期内的已确认费用登记列表
	 * @param shopBalanceDateDtl 结算期明细
	 * @return 专柜费用登记列表
	 */
	public List<ShopCost> queryByBalanceDate(ShopBalanceDateDtl shopBalanceDateDtl);
}







