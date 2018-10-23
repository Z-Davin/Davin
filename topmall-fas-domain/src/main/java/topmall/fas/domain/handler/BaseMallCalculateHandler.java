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
import topmall.fas.model.MallBalanceDateDtl;
import topmall.fas.model.MallCost;
import topmall.fas.util.CommonUtil;
import topmall.pos.api.IMallDaySaleApiService;

/**
 * 物业合同计算基类
 * 
 * @author dai.j
 * @date 2017-10-16 上午11:33:17
 * @version 0.1.0 
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
public abstract class BaseMallCalculateHandler<T extends IEntry>{

	protected final Log logger = LogFactory.getLog(getClass());
	
	/**
	 * 日结查询Service
	 */
	protected IMallDaySaleApiService mallDaySaleApiService;
	
	/**
	 * 计算费用的Manager
	 */
	protected IManager<T, String> manager;
	
	/**
	 * 适用的条款列表
	 */
	protected List<T> contractPoolList; 
	
	/**
	 * 物业结算期的详细信息
	 */
	protected MallBalanceDateDtl mallBalanceDateDtl;
	
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

	public BaseMallCalculateHandler(IManager<T, String> manager, MallBalanceDateDtl mallBalanceDateDtl) {
		this.manager = manager;
		this.mallBalanceDateDtl = mallBalanceDateDtl;
		this.mallDaySaleApiService = CommonStaticManager.getMallDaySaleApiService();
	}
	
	/**
	 * 计算费用
	 */
	public abstract List<MallCost> calculateCost();
	
	/**
	 * 获取有效的合同条款
	 */
	protected void getEnableContractPool(){
		Query query = Q.where("balanceDateId", mallBalanceDateDtl.getId());
		contractPoolList = manager.selectByParams(query);
	}
	
	/**
	 * 计算费用  用含税标识来计算应结总额和应结价款  '是否含税(0:不含税;1:含税)'
	 * @param initCost 计算出来的费用
	 * @param counterCost 费用对象
	 */
	protected void setMallCost(BigDecimal initCost, MallCost mallCost) {
		if (0 == taxFlag.shortValue()) {
			BigDecimal taxCost = CommonUtil.getTaxCost(initCost, raxRate);
			mallCost.setAbleSum(taxCost);
			mallCost.setAbleAmount(initCost);
			mallCost.setTaxAmount(taxCost.subtract(initCost));
		} else if (1 == taxFlag.shortValue()) {
			BigDecimal taxFreeCost = CommonUtil.getTaxFreeCost(initCost, raxRate);
			mallCost.setAbleSum(initCost);
			mallCost.setAbleAmount(taxFreeCost);
			mallCost.setTaxAmount(initCost.subtract(taxFreeCost));
		}
	}
	
	
	
	/**
	 * 根据结算期构建费用对象(只构建了需要从结算期获取的字段)
	 * @param shopBalanceDate 结算期对象
	 * @return 费用对象
	 */
	public MallCost getMallCost() {
		MallCost mallCost = new MallCost();
		mallCost.setShopNo(mallBalanceDateDtl.getShopNo());
		mallCost.setMallNo(mallBalanceDateDtl.getMallNo());
		mallCost.setBunkGroupNo(mallBalanceDateDtl.getBunkGroupNo());
		mallCost.setSettleMonth(mallBalanceDateDtl.getSettleMonth());
		mallCost.setSettleStartDate(mallBalanceDateDtl.getSettleStartDate());
		mallCost.setSettleEndDate(mallBalanceDateDtl.getSettleEndDate());
		mallCost.setSource(new Short((short) 1));//设置生成方式为系统生成
		mallCost.setStatus(StatusEnums.EFFECTIVE.getStatus());
		mallCost.setBunkGroupNo(mallBalanceDateDtl.getBunkGroupNo());
		mallCost.setCreateUser("系统生成");
		mallCost.setCreateTime(new Date());
		return mallCost;
	}
}
