package topmall.fas.repository;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import topmall.fas.model.BillCounterBalance;
import topmall.framework.repository.IRepository;
import cn.mercury.basic.query.Pagenation;
@Mapper
public interface BillCounterBalanceRepository extends IRepository<BillCounterBalance,String> {
	
	/** 根据唯一索引查询
	 * @param params
	 * @return
	 */
	public BillCounterBalance findByUnique(@Param("params") Map<String, Object> params);
	
	/** 获取最后一次生成费用的时间
	 * @param params
	 * @return
	 */
	public BillCounterBalance getLastCounterBalanceDate(@Param("params") Map<String, Object> params);
	
	/**
	 * 更新单据状态
	 * @param params
	 */
	public Integer updateStatus (@Param("params") Map<String, Object> params);
	
	/**
	 * 查询分页数据的合计
	 * @param params 查询条件
	 * @param page 分页条件
	 * @param orderby 排序
	 * @return 合计行
	 */
	public List<BillCounterBalance> selectByPageTotal(@Param("params") Map<String, Object> params,
			@Param("page") Pagenation page,
			@Param("orderby") String orderby);
}

             
             
             
             
             
             
             
             
             
             
             
             
             