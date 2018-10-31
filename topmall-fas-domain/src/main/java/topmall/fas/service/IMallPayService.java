package topmall.fas.service;


import java.util.List;

import topmall.fas.model.MallPay;
import topmall.framework.service.IService;
import cn.mercury.basic.query.Query;

public interface IMallPayService extends IService<MallPay,String>{

	List<MallPay> selectMallPayList(Query query);  
	
	/** 解除和结算单的关系
	 * @param id
	 */
	public void updateToUnsettled(String id);
	
	/** 根据收银方式汇总数据
	 * @param query
	 * @return
	 */
	public List<MallPay> getMallPayGroupPaidWay(Query query);
	
	/** 合计
	 * @param q
	 * @return
	 */
	public List<MallPay> queryConditionSum(Query q);
	
    
}







