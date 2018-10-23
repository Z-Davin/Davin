package topmall.fas.service;

import java.util.List;

import topmall.fas.model.BillCounterBalance;
import topmall.framework.service.IService;
import cn.mercury.basic.query.Pagenation;
import cn.mercury.basic.query.Query;

public interface IBillCounterBalanceService extends IService<BillCounterBalance, String> {

	/** 根据唯一索引查询
	 * @param query
	 * @return
	 */
	public BillCounterBalance findByUnique(Query query);

	/** 获取最后一次生成费用的时间
	 * @param params
	 * @return
	 */
	public BillCounterBalance getLastCounterBalanceDate(Query query);

	/**
	 * 更新状态
	 * @param query
	 */
	public Integer updateStatus(Query query);
	
	/**
	 * 查询分页数据的合计
	 * @param query 查询条件
	 * @param page 分页条件
	 * @return  合计行
	 */
	public List<BillCounterBalance> selectByPageTotal(Query query, Pagenation page);
}
