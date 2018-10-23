package topmall.fas.service;


import java.math.BigDecimal;
import java.util.List;

import topmall.fas.model.ContractDiscoPool;
import topmall.fas.vo.CounterDaySale;
import topmall.framework.service.IService;
import cn.mercury.basic.query.Query;

public interface IContractDiscoPoolService extends IService<ContractDiscoPool,String>{    
	
	/**
	 * 查询有效的合同扣率条款 
	 * @param query 查询条件
	 * @return 有效的合同扣率条款 
	 */
	List<ContractDiscoPool> selectValidDisco(Query query);
	
	/** 根据 部类编码,税率,票扣标识,账扣标识,时间维度分组查询
	 * @return
	 */
	List<ContractDiscoPool> selectGroupContractDiscoData(Query query);
	
	/** 从日结表 根据 部类编码,销售时间,活动,商品折扣,扣率 维度分组
	 * @param query
	 * @return
	 */
	List<CounterDaySale> selectGroupShopDaySaleData(Query query);
	
	/**
	 * 从日结表 根据销售码类型,专柜,销售时间,活动,商品折扣维度分组
	 * @param query
	 * @return
	 */
	List<CounterDaySale> selectGroupShopDaySaleDataForMall(Query query);
	
	/** 券分摊金额
	 * @param query
	 * @return
	 */
	BigDecimal ticketAbsorptionAmonut(Query query);
	
	/** 积分底现费用
	 * @param query
	 * @return
	 */
	BigDecimal pointsAmount(Query query);
	
	/** VIP分摊金额
	 * @param query
	 * @return
	 */
	BigDecimal vipAbsorptionAmonut(Query query);
	
	 /**查询净收入和毛收入只差
	 * @param query
	 * @return
	 */
	BigDecimal queryReduceDiffAmount(Query query);
	
	/**汇总物料费用
	 * @param query
	 * @return
	 */
	BigDecimal materialAmount(Query query);
	
	/** 物业保底数据
	 * @param query
	 * @return
	 */
	public List<CounterDaySale> queryMinimumMall(Query query);
	
	/**
	 * 查询纸袋的数量
	 * @param query
	 * @return
	 */
	Integer bagSaleQty(Query query);
	
}







