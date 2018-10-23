/**
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
package topmall.fas.domain.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import topmall.fas.model.ContractRentPool;
import topmall.fas.model.CounterCost;
import topmall.fas.model.ShopBalanceDateDtl;
import cn.mercury.manager.IManager;
import cn.mercury.utils.DateUtil;

/**
 * 租金费用计算
 * 
 * @author dai.j
 * @date 2017-8-29 下午4:53:51
 * @version 0.1.0 
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
public class RentCalculateHandler extends BaseCalculateHandler<ContractRentPool> {

	/**
	 * @param shopBalanceDateDtl
	 * @param manager
	 */
	public RentCalculateHandler(ShopBalanceDateDtl shopBalanceDateDtl, IManager<ContractRentPool, String> manager) {
		super(shopBalanceDateDtl, manager);
	}

	/**
	 * @see topmall.fas.domain.handler.BaseCalculateHandler#calculateCost()
	 */
	@Override
	public List<CounterCost> calculateCost(boolean isHisCost) {
		List<CounterCost> resultList = new ArrayList<>();

		getEnableContractPool();

		//1. 获取月租的天数,先算出结算期开始日期加一个月再减去一天，按照这个天数差算出是否是满一个月的天数
		Date nextMonthDay = DateUtil.addDate(DateUtil.addMonth(shopBalanceDateDtl.getSettleStartDate(), 1), -1);
		int rentDays = DateUtil.getBetweenDays(shopBalanceDateDtl.getSettleStartDate(), nextMonthDay);

		for (ContractRentPool contractRentPool : contractPoolList) {

			//2.获取结算期的有效天数
			int dateLength = getEnableDays(contractRentPool);

			// 构造费用对象
			CounterCost counterCost = getCounterCost();
			counterCost.setTaxFlag(contractRentPool.getTaxFlag());
			counterCost.setTaxRate(contractRentPool.getRaxRate());
			counterCost.setBillDebit(contractRentPool.getBillDebit());
			counterCost.setAccountDebit(contractRentPool.getAccountDebit());
			counterCost.setCostNo(contractRentPool.getCostNo());
			counterCost.setCostName(contractRentPool.getCostName());

			taxFlag = contractRentPool.getTaxFlag();
			raxRate = contractRentPool.getRaxRate();

			//①：获取租金类别 和合同上的租金金额
			Short rentType = contractRentPool.getRentType();//租金类别 1：按月  2：按天
			BigDecimal preRentCost = contractRentPool.getRentCost(); // 合同上的租金金额
			BigDecimal rentCost = new BigDecimal(0); //实际租金费用

			//②：如果是按月计算还需要判断不足月时怎么计算
			if (1 == rentType.shortValue()) {
				Short lessMonth = contractRentPool.getLessMonth();//不足一个月(1:按整月计算;2:按天计算)
				if (2 == lessMonth.shortValue()) {
					Short monthDays = contractRentPool.getMonthDays(); // 月租天数:0,自然天数;1,28;2,29;3,30;4,31
					switch (monthDays) {
					case 1:
						rentDays = 28;
						break;
					case 2:
						rentDays = 29;
						break;
					case 3:
						rentDays = 30;
						break;
					case 4:
						rentDays = 31;
						break;
					default:
						break;
					}

					/**
					 * 按天计算的时候 的计算公式就是  合同月租/月租天数*实际天数
					 * 如果实际天数比设置的天数要大  就按照设置的天数计算
					 */
					if (dateLength >= rentDays) {
						rentCost = preRentCost;
					} else {
						rentCost = preRentCost.multiply(new BigDecimal(dateLength)).divide(new BigDecimal(rentDays), 2,
								BigDecimal.ROUND_HALF_UP);
					}

				} else {
					rentCost = preRentCost;
				}
			} else if (2 == rentType) {
				if (dateLength < rentDays) {
					rentCost = preRentCost.multiply(new BigDecimal(dateLength));
				} else {
					rentCost = preRentCost.multiply(new BigDecimal(rentDays));
				}
			}

			//③ ：将有效合同条款(租金)池的结算期id更新进去
			contractRentPool.setBalanceDateId(shopBalanceDateDtl.getId());
			manager.update(contractRentPool);

			totalCost = totalCost.add(rentCost);
			
			setCounterCost(rentCost, counterCost);
			counterCost.setRefId(contractRentPool.getRefId());
			counterCost.setRefType((short)1);
			
			resultList.add(counterCost);
		}
		return resultList;
	}

	/**
	 * 计算租金条款在结算期内的有效天数
	 * @param contractRentPool 租金条款
	 * @return 有效天数
	 */
	private int getEnableDays(ContractRentPool contractRentPool) {
		Date startDate;
		Date endDate;
		if (shopBalanceDateDtl.getSettleStartDate().before(contractRentPool.getStartDate())) {
			startDate = contractRentPool.getStartDate();
		} else {
			startDate = shopBalanceDateDtl.getSettleStartDate();
		}

		if (shopBalanceDateDtl.getSettleEndDate().after(contractRentPool.getEndDate())) {
			endDate = contractRentPool.getEndDate();
		} else {
			endDate = shopBalanceDateDtl.getSettleEndDate();
		}
		return DateUtil.getBetweenDays(startDate, endDate);
	}
}
