/**
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
package topmall.fas.domain.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.mercury.basic.query.Q;
import cn.mercury.basic.query.Query;
import cn.mercury.manager.IManager;
import cn.mercury.utils.DateUtil;
import topmall.fas.manager.impl.CommonStaticManager;
import topmall.fas.model.ContractGuaraPool;
import topmall.fas.model.CounterCost;
import topmall.fas.model.ShopBalanceDateDtl;
import topmall.fas.util.CommonUtil;
import topmall.fas.util.PublicConstans;
import topmall.pos.dto.PosSaleSumCostDto;
import topmall.pos.dto.query.DaySaleQueryDto;

/**
 * 保底费用计算
 * 
 * 毛收入 = 实收金额
 * 净收入 = 实收金额 - 卡券的虚金额（例如卡或者券 面值100，但是实际价值80块，那么虚金额就是20块）
 * 
 * @author dai.j
 * @date 2017-8-28 上午10:44:01
 * @version 0.1.0 
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
public class GuaraCalculateHandler extends BaseCalculateHandler<ContractGuaraPool> {

	/**
	 * 保底周期(1:月保底;2:季度保底;3:半年保底;4:年保底)
	 */
	private Short baseCycle;

	/**
	 * 保底算法(1:每月核算,月末通算;2:月末核算)
	 */
	private Short baseAlgorithm;

	/**
	 * 保底基数类型(1:净收入;2:毛收入;3:利润保底;4:以其他专柜销售保底;5:毛收入-积分抵现)
	 */
	private Short baseNumber;

	/**
	 * 保底计算基数（1:毛收入;2:保底金额-保底基数;3:保底金额;4:净收入;5:毛收入-积分抵现）
	 */
	private Short baseCalculateNum;

	/**
	 * 保底金额
	 */
	private BigDecimal baseAmount;

	/**
	 * 计算销售的开始日期
	 */
	private Date saleStartDate;

	/**
	 * 计算销售的结束日期
	 */
	private Date saleEndDate;

	/**
	 * 是否达到保底, 默认为false
	 */
	public boolean reachGuara = false;

	/**
	 * @param shopBalanceDateDtl
	 * @param manager
	 */
	public GuaraCalculateHandler(ShopBalanceDateDtl shopBalanceDateDtl, IManager<ContractGuaraPool, String> manager) {
		super(shopBalanceDateDtl, manager);
	}

	/**
	 * @see topmall.fas.domain.handler.BaseCalculateHandler#calculateCost(cn.mercury.domain.BaseEntity)
	 */
	@Override
	public List<CounterCost> calculateCost(boolean isHisCost) {

		List<CounterCost> resultList = new ArrayList<>();

		getEnableContractPool();

		for (ContractGuaraPool contractGuaraPool : contractPoolList) {

			// 构造费用对象
			CounterCost counterCost = getCounterCost();
			counterCost.setTaxFlag(contractGuaraPool.getTaxFlag());
			counterCost.setTaxRate(contractGuaraPool.getRaxRate());
			counterCost.setBillDebit(contractGuaraPool.getBillDebit());
			counterCost.setAccountDebit(contractGuaraPool.getAccountDebit());
			counterCost.setCostNo(contractGuaraPool.getCostNo());
			counterCost.setCostName(contractGuaraPool.getCostName());

			baseCycle = contractGuaraPool.getBaseCycle();// 保底周期(1:月保底;2:季度保底;3:半年保底;4:年保底)
			baseAlgorithm = contractGuaraPool.getBaseAlgorithm(); // 保底算法(1:每月核算,月末通算;2:月末核算)

			int calculateFlag = reckonGuara(contractGuaraPool);
			if (calculateFlag == 0) {
				continue;
			}

			/*
			 * 净收入 ：实收金额 - 卡券的扣率值
			 * 毛收入：实收金额
			 * 利润保底  ：销售乘以扣点
			 * 以其他专柜销售保底  ：其他专柜的终端销售收入*数量
			 */
			baseNumber = contractGuaraPool.getBaseNumber();
			baseCalculateNum = contractGuaraPool.getBaseCalculateNum();

			setQuerySaleDate(contractGuaraPool, calculateFlag);

			// 根据合同来算出是否根据合同上的保底金额计算 还是要分摊到天计算
			baseAmount = getBaseAmount(contractGuaraPool, calculateFlag);

			taxFlag = contractGuaraPool.getTaxFlag();
			raxRate = contractGuaraPool.getRaxRate();

			BigDecimal rechonBaseAmount = new BigDecimal(0);//保底基数
			BigDecimal guaraCost;//计算出的保底费用结果
			BigDecimal cashCost = new BigDecimal(0);//积分抵现金额

			// 保底基数的查询条件
			DaySaleQueryDto saleBaseQuery = new DaySaleQueryDto(shopBalanceDateDtl.getShopNo(), saleStartDate,
					saleEndDate);
			PosSaleSumCostDto posSaleSumCostDto = new PosSaleSumCostDto();

			//根据保底基数类型获取保底基数
			if (1 == baseNumber.shortValue()) { // 获取净收入
				saleBaseQuery.setCounterNo(shopBalanceDateDtl.getCounterNo());
				saleBaseQuery.setDivisionNo(contractGuaraPool.getDivisionNo());
				saleBaseQuery.setQueryType(1);
				posSaleSumCostDto = shopDaySaleApiService.selectSaleDataSum(saleBaseQuery, null);
				
				rechonBaseAmount = posSaleSumCostDto.getNetIncomeSumAmount();
			} else if (2 == baseNumber.shortValue()) { // 获取毛收入
				saleBaseQuery.setCounterNo(shopBalanceDateDtl.getCounterNo());
				saleBaseQuery.setDivisionNo(contractGuaraPool.getDivisionNo());
				saleBaseQuery.setQueryType(1);
				posSaleSumCostDto = shopDaySaleApiService.selectSaleDataSum(saleBaseQuery, null);
				
				rechonBaseAmount = posSaleSumCostDto.getSaleSumAmount();
			} else if (3 == baseNumber.shortValue()) { // 获取利润保底
				saleBaseQuery.setCounterNo(shopBalanceDateDtl.getCounterNo());
				saleBaseQuery.setDivisionNo(contractGuaraPool.getDivisionNo());
				saleBaseQuery.setQueryType(1);
				posSaleSumCostDto = shopDaySaleApiService.selectSaleDataSum(saleBaseQuery, null);
				
				rechonBaseAmount = posSaleSumCostDto.getProfitSumAmount();
			} else if (4 == baseNumber.shortValue()) { // 获取以其他专柜销售保底
				saleBaseQuery.setCounterNo(shopBalanceDateDtl.getCounterNo());
				saleBaseQuery.setDivisionNo(contractGuaraPool.getDivisionNo());
				saleBaseQuery.setQueryType(1);
				posSaleSumCostDto = shopDaySaleApiService.selectSaleDataSum(saleBaseQuery, null);
				
				saleBaseQuery.setCounterNo(contractGuaraPool.getCounterNo());
				saleBaseQuery.setQueryType(1);
				PosSaleSumCostDto otherPosSaleSumCostDto = shopDaySaleApiService.selectSaleDataSum(saleBaseQuery,
						null);
				Integer number = contractGuaraPool.getNumber();

				if (1 == baseCalculateNum.shortValue()) {//毛收入(实收金额)
					baseAmount = otherPosSaleSumCostDto.getSaleSumAmount().multiply(new BigDecimal(number));
					rechonBaseAmount = posSaleSumCostDto.getSaleSumAmount();
				} else if (4 == baseCalculateNum.shortValue()) {// 净收入
					baseAmount = otherPosSaleSumCostDto.getNetIncomeSumAmount().multiply(new BigDecimal(number));
					rechonBaseAmount = posSaleSumCostDto.getNetIncomeSumAmount();
				}

			} else if (5 == baseNumber.shortValue()) { //毛收入-积分抵现
				saleBaseQuery.setCounterNo(shopBalanceDateDtl.getCounterNo());
				saleBaseQuery.setDivisionNo(contractGuaraPool.getDivisionNo());
				posSaleSumCostDto = shopDaySaleApiService.selectSaleDataSum(saleBaseQuery, null);
				
				Query query = Q.where("shopNo",shopBalanceDateDtl.getShopNo()).and("counterNo", shopBalanceDateDtl.getCounterNo())
						.and("settleStartDate", shopBalanceDateDtl.getSettleStartDate())
						.and("settleEndDate", shopBalanceDateDtl.getSettleEndDate())
						.and("queryType", PublicConstans.POINTS_ITEMNO);
				
				BigDecimal desccashCost = CommonStaticManager.getContractDiscoPoolService().pointsAmount(query);
				if(desccashCost != null) {
					cashCost = desccashCost;
				}
				rechonBaseAmount = posSaleSumCostDto.getSaleSumAmount().subtract(cashCost);
			}

			// 当保底金额小于等于保底基数时，保底费用为0
			if (baseAmount.compareTo(rechonBaseAmount) <= 0) {
				guaraCost = BigDecimal.ZERO;

				//当达到保底的时候 设置达到保底标识为true
				reachGuara = true;
			} else {

				BigDecimal baseCalculateAmount = new BigDecimal(0);//保底计算基数

				if (1 == baseCalculateNum.shortValue()) {//毛收入(实收金额)
					baseCalculateAmount = posSaleSumCostDto.getSaleSumAmount();
				} else if (2 == baseCalculateNum.shortValue()) { // 保底-保底基数
					baseCalculateAmount = baseAmount.subtract(rechonBaseAmount);
				} else if (3 == baseCalculateNum.shortValue()) { // 保底金额
					baseCalculateAmount = baseAmount;
				} else if (4 == baseCalculateNum.shortValue()) { // 净收入
					baseCalculateAmount = posSaleSumCostDto.getNetIncomeSumAmount();
				} else if (5 == baseCalculateNum.shortValue()) { // 毛收入-积分抵现
					baseCalculateAmount = rechonBaseAmount;
				} 
				
				//如果保底计算基数是  保底-保底基数  或者 保底基数是 毛收入-积分抵现 ,那么就不需要减去销售提成
				if(2 == baseCalculateNum.shortValue() || 5 == baseNumber.shortValue()) {
					guaraCost = baseCalculateAmount.multiply(contractGuaraPool.getBaseDiscount())
							.divide(CommonUtil.ONE_HUNDRED, 2, BigDecimal.ROUND_HALF_UP);
				} else {
					
					Query saleProfitQuery = Q.where("shopNo", shopBalanceDateDtl.getShopNo())
							.and("counterNo", shopBalanceDateDtl.getCounterNo()).and("startDate", saleStartDate)
							.and("endDate", saleEndDate).and("divisionNo", contractGuaraPool.getDivisionNo()).and("isHisCost", isHisCost);
					BigDecimal saleProfitSum = CommonStaticManager.getCounterSaleCostDtlManager().querySaleProfit(
							saleProfitQuery);

					guaraCost = baseCalculateAmount.multiply(contractGuaraPool.getBaseDiscount())
							.divide(CommonUtil.ONE_HUNDRED, 2, BigDecimal.ROUND_HALF_UP).subtract(saleProfitSum);
				}
				
			}
			
			if (calculateFlag == 2) {
				guaraCost = getLastGuaraCost(guaraCost, contractGuaraPool);
			}

			// 将有效合同条款(保底)池的结算期id更新进去
			contractGuaraPool.setBalanceDateId(shopBalanceDateDtl.getId());
			manager.update(contractGuaraPool);

			totalCost = totalCost.add(guaraCost);

			setCounterCost(guaraCost, counterCost);

			// 将专柜合同的id设置到
			counterCost.setRefId(contractGuaraPool.getRefId());
			counterCost.setRefType((short)3);

			resultList.add(counterCost);
		}
		return resultList;
	}

	/**
	 * 判断此结算期内是否要计算保底
	 * @param contractGuaraPool 保底条款
	 * @return 0:不计算，1：计算但是不是月末通算，2：计算但是要月末通算
	 */
	private int reckonGuara(ContractGuaraPool contractGuaraPool) {
		if (null == baseAlgorithm) {
			return 1;
		} else {
			Date startDate = contractGuaraPool.getStartDate();
			int passMonth = getPassLength(startDate);
			int cycleMonthLen = 1;
			if (2 == baseCycle) {
				cycleMonthLen = 3;
			} else if (3 == baseCycle) {
				cycleMonthLen = 6;
			} else if (4 == baseCycle) {
				cycleMonthLen = 12;
			}
			int t = (passMonth + 1) % cycleMonthLen;
			if (t == 0) {
				return 2;
			} else {
				if (1 == baseAlgorithm) {
					return 1;
				} else {
					if (contractGuaraPool.getEndDate().before(shopBalanceDateDtl.getSettleEndDate())) {
						return 1;
					} else {
						return 0;
					}
				}
			}
		}
	}

	/**
	 * 使用一个日期计算这个日期开始的时间在结算期中算过几次了
	 * 例如：一个条款是生效日期2017-03-20 那么在计算期(2017-03-15 至2017-04-14)中就要开始第一次计算
	 * @param startDate 条款的开始日期 
	 * @param balanceDateDtl 结算期 
	 * @return 在结算期之前此条款算过几次了
	 */
	private int getPassLength(Date startDate) {
		int balanceEndDay = DateUtil.getDay(shopBalanceDateDtl.getSettleEndDate());
		int lastDay = DateUtil.getDay(startDate);

		Date tempDate;
		Calendar c1 = Calendar.getInstance();

		if (lastDay > balanceEndDay) {
			c1.setTime(shopBalanceDateDtl.getSettleStartDate());
			c1.set(Calendar.DAY_OF_MONTH, balanceEndDay);
			tempDate = c1.getTime();
		} else {
			c1.setTime(shopBalanceDateDtl.getSettleEndDate());
			c1.set(Calendar.DAY_OF_MONTH, balanceEndDay);
			tempDate = c1.getTime();
		}

		return DateUtil.getBetweenMonths(startDate, tempDate);
	}

	/**
	 * 获取实际的保底金额(如果保底实际计算周期不足，那么就必须分摊到天计算)
	 * @param contractGuaraPool 合同条款
	 * @param calculateFlag 1：不是月末通算 ，2：月末通算
	 * @return 算出实际的保底金额
	 */
	private BigDecimal getBaseAmount(ContractGuaraPool contractGuaraPool, int calculateFlag) {

		//合同的保底金额
		BigDecimal initCost = contractGuaraPool.getBaseAmount();

		// 实际需要比较的保底金额
		BigDecimal rechonBaseAmount;
		
		//分摊到天时候的 合同计算的开始日期
		Date balanceStartDate = shopBalanceDateDtl.getSettleStartDate();

		// 先按照预期的保底金额计算 如果需要分摊到天后面再计算`	``````````
		if (null == baseAlgorithm || 2 == baseAlgorithm || 2 == calculateFlag) {
			rechonBaseAmount = initCost;
			if (2 == baseCycle) {
				balanceStartDate = DateUtil.addMonth(shopBalanceDateDtl.getSettleStartDate(), -2);
			} else if (3 == baseCycle) {
				balanceStartDate = DateUtil.addMonth(shopBalanceDateDtl.getSettleStartDate(), -5);
			} else if (4 == baseCycle) {
				balanceStartDate = DateUtil.addMonth(shopBalanceDateDtl.getSettleStartDate(), -11);
			}
		} else {
			if (2 == baseCycle) {
				rechonBaseAmount = initCost.divide(new BigDecimal(3), 2, BigDecimal.ROUND_HALF_UP);
			} else if (3 == baseCycle) {
				rechonBaseAmount = initCost.divide(new BigDecimal(6), 2, BigDecimal.ROUND_HALF_UP);
			} else if (4 == baseCycle) {
				rechonBaseAmount = initCost.divide(new BigDecimal(12), 2, BigDecimal.ROUND_HALF_UP);
			} else {
				rechonBaseAmount = initCost;
			}
		}
		
		//获取到计算周期的预期天数
		int calculateLength = DateUtil.getBetweenDays(balanceStartDate, shopBalanceDateDtl.getSettleEndDate());

		//获取到计算周期的实际天数
		int factLength = DateUtil.getBetweenDays(saleStartDate, saleEndDate);

		// 如果预期天数和实际天数一致  那就说明 计算周期不用算到天，否则就要分摊到天计算
		if (calculateLength != factLength) {
			rechonBaseAmount = rechonBaseAmount.divide(new BigDecimal(calculateLength), 2, BigDecimal.ROUND_HALF_UP)
					.multiply(new BigDecimal(factLength));
		}
		return rechonBaseAmount;
	}

	/**
	 * 设置查询销售的有效开始日期和结束日期
	 * @param contractGuaraPool 合同保底条款
	 * @param calculateFlag  1：不是月末通算 ，2：月末通算
	 */
	private void setQuerySaleDate(ContractGuaraPool contractGuaraPool, int calculateFlag) {
		// 获取合同的开始日期和结束日期
		Date guaraStartDate = contractGuaraPool.getStartDate();
		Date guaraEndDate = contractGuaraPool.getEndDate();

		// 获取结算期的开始日期和结束日期+
		Date balanceStartDate = shopBalanceDateDtl.getSettleStartDate();
		Date balanceEndDate = shopBalanceDateDtl.getSettleEndDate();

		if (2 == calculateFlag) {
			int divMonth = 0;
			if (2 == baseCycle) {
				divMonth = -2;
			} else if (3 == baseCycle) {
				divMonth = -5;
			} else if (4 == baseCycle) {
				divMonth = -11;
			}
			balanceStartDate = DateUtil.addMonth(shopBalanceDateDtl.getSettleStartDate(), divMonth);
		}

		// 结算期的开始日期 和 合同的开始日期 谁大就取谁
		if (balanceStartDate.before(guaraStartDate)) {
			saleStartDate = guaraStartDate;
		} else {
			saleStartDate = balanceStartDate;
		}

		// 结算期的结算日期 和 合同的结束日期 谁小就取谁
		if (balanceEndDate.after(guaraEndDate)) {
			saleEndDate = guaraEndDate;
		} else {
			saleEndDate = balanceEndDate;
		}
	}

	/**
	 * 进行月末通算的时候 需要减去前几个月的保底金额（到底几个月是根据保底是季保底、半年保底、年保底）
	 * @param guaraCost 月末通算的保底费用
	 * @return 通算的保底费用要减去前几个月的保底金额
	 */
	private BigDecimal getLastGuaraCost(BigDecimal guaraCost, ContractGuaraPool contractGuaraPool) {
		String refId = contractGuaraPool.getRefId(); //获取到此专柜合同的原合同Id
		String settleMonth = shopBalanceDateDtl.getSettleMonth(); // 获取到此结算期的结算月
		String shopNo = shopBalanceDateDtl.getShopNo();
		String counterNo = shopBalanceDateDtl.getCounterNo();
		Query query = getSettleMonthQuery(settleMonth);
		query.and("shopNo", shopNo).and("counterNo", counterNo).and("refId", refId);
		List<CounterCost> counterCosts = CommonStaticManager.getCounterCostManager().selectByParams(query);
		for (CounterCost counterCost : counterCosts) {
			BigDecimal tempCost;
			if (1 == taxFlag) {
				tempCost = counterCost.getAbleSum();
			} else {
				tempCost = counterCost.getAbleAmount();
			}
			guaraCost = guaraCost.subtract(tempCost);
		}
		return guaraCost;
	}

	/**
	 * 根据当前的结算期还有保底周期 去获取需要查询的保底费用列表查询条件
	 * @param settleMonth 月末通算的结算期结算月 格式 (yyyy年MM月)
	 * @return 查询条件(多个结算期)
	 */
	private Query getSettleMonthQuery(String settleMonth) {
		Date settleDate = DateUtil.parseToDate(settleMonth, "yyyyMM");

		// 需要获取前几个月的数据
		int cycleLength = 0;
		if (2 == baseCycle) {
			cycleLength = 2;
		} else if (3 == baseCycle) {
			cycleLength = 5;
		} else if (4 == baseCycle) {
			cycleLength = 11;
		}
		String[] strArray = new String[cycleLength];

		for (int i = 0; i < cycleLength; i++) {
			Date tempDate = DateUtil.addMonth(settleDate, -1 - i);
			strArray[i] = DateUtil.format3(tempDate);
		}
		return Q.where(Q.In("settleMonth", strArray));
	}
}
