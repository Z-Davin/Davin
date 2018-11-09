package topmall.fas.manager;

import topmall.fas.model.MallBalanceDate;
import topmall.fas.util.CommonResult;
import cn.mercury.basic.query.Query;
import cn.mercury.manager.IManager;

public interface IMallBalanceDateManager extends IManager<MallBalanceDate, String> {

	/**
	 * 插入数据之前的验证方法
	 * @param shopBalanceDate 验证对象
	 * @return 返回结果集
	 */
	CommonResult validateCreate(MallBalanceDate mallBalanceDate);

	MallBalanceDate confirm(String id);
	
	/**
	 * 物业月结
	 * @param idList id列表用逗号分隔
	 */
	void monthBalance(String idList) throws Exception ;
	
	/**
	 * 查询唯一物业结算期
	 * @param query
	 * @return
	 */
	MallBalanceDate findByUnique(Query query);
}
