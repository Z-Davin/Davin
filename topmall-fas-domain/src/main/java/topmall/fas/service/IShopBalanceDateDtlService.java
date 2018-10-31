package topmall.fas.service;

import topmall.fas.dto.ContractMainDto;
import topmall.fas.model.ShopBalanceDateDtl;
import topmall.framework.service.IService;

import java.util.Date;

import cn.mercury.basic.query.Query;

/**
 * dai.xw
 */
public interface IShopBalanceDateDtlService extends IService<ShopBalanceDateDtl,String>{    
    
	/**
	 * 根据店铺编码和专柜编码获取有效合同编号
	 * @param shopBalanceDateDtl 结算期对象
	 * @return 专柜合同
	 */
	ContractMainDto selectContractBillNo(ShopBalanceDateDtl shopBalanceDateDtl);
	
	/**
	 * 根据店铺编码、专柜编码、结算月、结算开始日期、结算结束日期 获取唯一的结算期
	 * @param query 查询条件
	 * @return 结算期明细
	 */
	ShopBalanceDateDtl findByUnique(Query query);
	
	/** 查询没有生成费用最小的结算期
	 * @param query
	 * @return
	 */
	String getUnGeneralCostMinMonth(Query query);
	/**
	 * 查询没有生成费用的最大时间
	 */
	Date getUnGeneralCostMaxDate(Query query);
}







