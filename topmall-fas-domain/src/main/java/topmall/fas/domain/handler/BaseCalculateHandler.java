/**
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
package topmall.fas.domain.handler;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.mercury.basic.query.Q;
import cn.mercury.basic.query.Query;
import cn.mercury.domain.IEntry;
import cn.mercury.manager.IManager;
import topmall.fas.enums.StatusEnums;
import topmall.fas.manager.impl.CommonStaticManager;
import topmall.fas.model.CounterCost;
import topmall.fas.model.ShopBalanceDateDtl;
import topmall.fas.util.CommonUtil;
import topmall.pos.api.IShopDaySaleApiService;

/**
 * 费用计算的基础算法类
 * 
 * @author dai.j
 * @date 2017-8-28 上午10:07:31
 * @version 0.1.0 
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
public abstract class BaseCalculateHandler<T extends IEntry> {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	protected IShopDaySaleApiService shopDaySaleApiService;
	
	/**
	 * 计算费用的Manager
	 */
	protected IManager<T, String> manager;
	
	/**
	 * 适用的条款列表
	 */
	protected List<T> contractPoolList; 
	
	/**
	 * 结算期的详细信息
	 */
	protected ShopBalanceDateDtl shopBalanceDateDtl;
	
	/**
	 * 结算总金额（毛收入）
	 */
	protected BigDecimal settleAmount;
	
	/**
	 * 终端收入总金额
	 */
	protected BigDecimal terminalAmount;
	
	/**
	 * 净收入
	 */
	protected BigDecimal netIncomeAmount;
	
	/**
	 * 税率 （不含百分号的数）
	 */
	protected BigDecimal raxRate;
	
	/**
	 * 是否含税(0:不含税;1:含税)
	 */
	protected Short taxFlag;
	
	/**
	 * 单一类型费用总和（例如：抽成费用的总和，用来取高使用）
	 */
	public BigDecimal totalCost = new BigDecimal(0);
	
	
	public BaseCalculateHandler(ShopBalanceDateDtl shopBalanceDateDtl, IManager<T, String> manager) {
		this.shopBalanceDateDtl = shopBalanceDateDtl;
		this.manager = manager;
		this.shopDaySaleApiService = CommonStaticManager.getShopDaySaleApiService();
	}
	
	/**
	 * 计算费用
	 */
	public abstract List<CounterCost> calculateCost(boolean isHisCost);
	
	/**
	 * 获取有效的合同条款
	 */
	protected void getEnableContractPool(){
		Query query = Q.where("balanceDateId", shopBalanceDateDtl.getId());
		contractPoolList = manager.selectByParams(query);
	}
	
	
	
	/**
	 * 计算费用  用含税标识来计算应结总额和应结价款  '是否含税(0:不含税;1:含税)'
	 * @param initCost 计算出来的费用
	 * @param counterCost 费用对象
	 */
	protected void setCounterCost(BigDecimal initCost, CounterCost counterCost) {
		if (0 == taxFlag.shortValue()) {
			BigDecimal taxCost = CommonUtil.getTaxCost(initCost, raxRate);
			counterCost.setAbleSum(taxCost);
			counterCost.setAbleAmount(initCost);
			counterCost.setTaxAmount(taxCost.subtract(initCost));
		} else if (1 == taxFlag.shortValue()) {
			BigDecimal taxFreeCost = CommonUtil.getTaxFreeCost(initCost, raxRate);
			counterCost.setAbleSum(initCost);
			counterCost.setAbleAmount(taxFreeCost);
			counterCost.setTaxAmount(initCost.subtract(taxFreeCost));
		}
	}
	
	
	
	/**
	 * 根据结算期构建费用对象(只构建了需要从结算期获取的字段)
	 * @param shopBalanceDate 结算期对象
	 * @return 费用对象
	 */
	public CounterCost getCounterCost() {
		CounterCost counterCost = new CounterCost();
		counterCost.setShopNo(shopBalanceDateDtl.getShopNo());
		counterCost.setCounterNo(shopBalanceDateDtl.getCounterNo());
		counterCost.setSettleMonth(shopBalanceDateDtl.getSettleMonth());
		counterCost.setSettleStartDate(shopBalanceDateDtl.getSettleStartDate());
		counterCost.setSettleEndDate(shopBalanceDateDtl.getSettleEndDate());
		
		// 设置实际的结算月，实际的结算开始日期，实际的结算结算日期
		counterCost.setActualMonth(shopBalanceDateDtl.getSettleMonth());
		counterCost.setActualStartDate(shopBalanceDateDtl.getSettleStartDate());
		counterCost.setActualEndDate(shopBalanceDateDtl.getSettleEndDate());
		
		counterCost.setSource((short) 1);//设置生成方式为系统生成
		counterCost.setStatus(StatusEnums.EFFECTIVE.getStatus());
		counterCost.setCreateUser("系统生成");
		counterCost.setCreateTime(new Date());
		return counterCost;
	}
}
