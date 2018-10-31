package topmall.fas.manager;

import java.util.List;

import topmall.fas.model.MallBalanceDateDtl;
import topmall.fas.model.MallCost;
import topmall.fas.util.CommonResult;
import cn.mercury.basic.query.Query;
import cn.mercury.manager.IManager;

public interface IMallCostManager extends IManager<MallCost,String>{
	
	/** 物业费用确认
	 * @param id
	 * @return
	 */
	public CommonResult batchConfirm(String[] id);
	
	
	/**
	 * 异步任务计算物业费用
	 * @param mallBalanceDateDtl 物业结算期明细
	 */
	public void generateMallCost(MallBalanceDateDtl mallBalanceDateDtl);
	
	/**
	 * 重新计算物业费用
	 * @param mallBalanceDateDtl
	 */
	public CommonResult recalculationCost(MallBalanceDateDtl mallBalanceDateDtl);
	
	/**
	 * 物业费用批量反确认
	 * @param ids
	 * @return
	 */
	public CommonResult batchUnConfirm(String[] ids);
	/**
	 * 查询物业结算期
	 * @param query 查询条件
	 * @return 物业结算期列表
	 */
	public List<MallBalanceDateDtl> findBySettle(Query query);
	
	/**
	 * 导入物业费用
	 * @param inserted 导入的物业费用列表
	 * @return 返回导入数目
	 */
	public Integer importBatchSave(List<MallCost> inserted);
	
	
	/** 合计
	 * @param query
	 * @return
	 */
	public List<MallCost> queryConditionSum(Query query);
}







