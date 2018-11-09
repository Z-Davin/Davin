package topmall.fas.service;


import cn.mercury.basic.query.Query;
import topmall.fas.model.MallBalanceDate; 
import topmall.framework.service.IService;

public interface IMallBalanceDateService extends IService<MallBalanceDate,String>{    
	/**
	 * 根据唯一条件查询出唯一值
	 * @param Query 查询出唯一结果的条件
	 * @return  唯一结果
	 */
	MallBalanceDate findByUnique(Query query);
}







