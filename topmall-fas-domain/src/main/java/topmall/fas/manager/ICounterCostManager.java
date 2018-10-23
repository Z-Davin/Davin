package topmall.fas.manager;

import java.util.List;

import topmall.fas.model.CounterCost;
import topmall.fas.model.ShopBalanceDateDtl;
import topmall.fas.util.CommonResult;
import cn.mercury.basic.query.Query;
import cn.mercury.manager.IManager;
import cn.mercury.manager.ManagerException;

public interface ICounterCostManager extends IManager<CounterCost,String>{
	
	/** 专柜费用确认
	 * @param id
	 * @return
	 */
	public CounterCost confirm(String id);
	
	
	/**
	 * 专柜费用批量确认
	 * @param ids
	 * @return
	 */
	public CommonResult batchConfirm(String[] ids) ;
	
	/**
	 * 专柜费用批量反确认
	 * @param ids
	 * @return
	 */
	public CommonResult batchUnConfirm(String[] ids);
	

    public List<ShopBalanceDateDtl>  findBySettle(Query query);
    
    /**
     * 重算费用
     * @param query 重算费用的条件
     * @return 重算结果
     */
    CommonResult recalculationCost(ShopBalanceDateDtl shopBalanceDateDtl);
    
    
	/**生成费用
	 * @param shopBalanceDateDtl
	 */
	public void generateCounterCost(ShopBalanceDateDtl shopBalanceDateDtl) ;
	
	/**
	 * 重算历史费用
	 * @param oldDateDtl 重算的结算期
	 * @param newDateDtl 重算后费用计入的新结算期
	 */
	public void generateHisCounterCost(ShopBalanceDateDtl oldDateDtl, ShopBalanceDateDtl newDateDtl);
	
	/**
	 * footer
	 * @param query
	 * @return
	 */
	List<CounterCost> queryConditionSum(Query query);
	
	/**
	 * 导入调用的BatchSave
	 * @param inserted
	 * @param updated
	 * @param deleted
	 * @return
	 * @throws ManagerException
	 */
	public Integer importBatchSave(List<CounterCost> inserted, List<CounterCost> updated, List<CounterCost> deleted);
}







