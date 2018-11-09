package topmall.fas.manager;

import java.util.List;
import topmall.fas.dto.BatchCounterBalanceDto;
import topmall.fas.dto.CounterBalancePrint;
import topmall.fas.model.BillCounterBalance;
import topmall.fas.util.CommonResult;
import cn.mercury.basic.query.Pagenation;
import cn.mercury.basic.query.Query;
import cn.mercury.manager.IManager;

public interface IBillCounterBalanceManager extends IManager<BillCounterBalance,String>{
	
	/**
	 * 保存专柜结算单
	 * @param counterBalance
	 * @param type 1 表示正常维度,2表示供应商维度
	 * @return
	 */
	public BillCounterBalance save(BillCounterBalance counterBalance,int type);
	
	/** 删除专柜结算单
	 * @param billNo
	 * @return
	 */
	public CommonResult deleteBill(String  billNo);
	
	/** 审核专柜结算单
	 * @param billNo
	 * @return
	 */
	public CommonResult verify(String  billNo);
	
	/** 反审核专柜结算单
	 * @param billNo
	 * @return
	 */
	public CommonResult unVerify(String  billNo);
	
	/** 批量审核专柜结算单
	 * @param billNo
	 * @return
	 */
	public CommonResult batchVerify(String[] billNos);
	
	/** 批量生成结算单
	 * @param counterBalanceDto
	 * @return
	 */
	public CommonResult batchCreate(BatchCounterBalanceDto counterBalanceDto);
	
	/** 获取最后一次生成费用的时间
	 * @param params
	 * @return
	 */
	public BillCounterBalance getLastCounterBalanceDate(Query query);
	
	/**
	 *专柜结算单打印
	 * @param billNo 专柜结算单
	 * @return
	 */
	public CounterBalancePrint print(String billNo,Integer templateType);
	
	/**
	 * 查询分页数据的合计
	 * @param query 查询条件
	 * @param page 分页条件
	 * @return  合计行
	 */
	public List<BillCounterBalance> selectByPageTotal(Query query, Pagenation page);

	List<CounterBalancePrint> batchPrint(Query query, Integer templateType);
	
	/** 
	 * @param billNo
	 */
	void printCount(String billNo);  
}







