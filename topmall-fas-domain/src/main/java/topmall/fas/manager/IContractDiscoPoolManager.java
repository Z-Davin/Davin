package topmall.fas.manager;

import java.util.List;

import topmall.fas.model.ContractDiscoPool;
import topmall.fas.model.MallBalanceDateDtl;
import topmall.fas.model.ShopBalanceDateDtl;
import cn.mercury.basic.query.Query;
import cn.mercury.manager.IManager;

public interface IContractDiscoPoolManager extends IManager<ContractDiscoPool,String>{
	
	/**
	 * 查询有效的合同扣率条款 
	 * @param query 查询条件
	 * @return 有效的合同扣率条款 
	 */
	List<ContractDiscoPool> selectValidDisco(Query query);
	
	/**
	 * 生成专柜销售费用
	 * @param shopBalanceDateDtl 专柜结算期明细(老结算期明细,如果不是历史月份重新,则是当前月的结算期明细)
	 * @param isHisCost 是否是历史费用重算
	 * @param newDateDtl 新结算期明细
	 */
    void createContractDiscoCost(ShopBalanceDateDtl shopBalanceDateDtl,boolean isHisCost,ShopBalanceDateDtl newDateDtl);
    
    
    /** 生成物业销售费用
     * @param mallBalanceDateDtl
     * @param isRecalculation 是否重算 true 是重算, false不是重算
     */
    void createMallDiscoCost(MallBalanceDateDtl mallBalanceDateDtl,boolean isRecalculation);
    
}







