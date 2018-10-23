package topmall.fas.service;


import java.util.Date;

import topmall.fas.dto.ContractMainDto;
import topmall.fas.model.MallBalanceDateDtl;
import topmall.framework.service.IService;
import cn.mercury.basic.query.Query;

public interface IMallBalanceDateDtlService extends IService<MallBalanceDateDtl,String>{    
	
	
	/**
	 * 根据物业结算期获取物业合同
	 * @param 物业结算期明细
	 * @return 物业合同的主要数据
	 */
	ContractMainDto selectContractBillNo(MallBalanceDateDtl mallBalanceDateDtl);
	
	
	/**
	 * 查询没有生成费用最小的结算结束时间	
	 * @param query 卖场编码和物业编码
	 * @return 最小的结束时间
	 */
	Date getUnGeneralCostMinDate(Query query);
}







