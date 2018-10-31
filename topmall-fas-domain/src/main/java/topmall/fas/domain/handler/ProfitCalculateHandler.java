/**
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
package topmall.fas.domain.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import topmall.fas.dto.QuotaStepDTO;
import topmall.fas.manager.impl.CommonStaticManager;
import topmall.fas.manager.impl.ContractProfitPoolManager;
import topmall.fas.model.ContractProfitPool;
import topmall.fas.model.CounterCost;
import topmall.fas.model.ShopBalanceDateDtl;
import topmall.fas.util.CommonUtil;
import topmall.pos.dto.PosSaleSumCostDto;
import topmall.pos.dto.query.DaySaleQueryDto;
import cn.mercury.basic.query.Q;
import cn.mercury.basic.query.Query;
import cn.mercury.manager.IManager;

/**
 * 抽成费用计算
 * 
 * @author dai.j
 * @date 2017-8-29 下午5:28:05
 * @version 0.1.0 
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
public class ProfitCalculateHandler extends BaseCalculateHandler<ContractProfitPool> {

	public ProfitCalculateHandler(ShopBalanceDateDtl shopBalanceDateDtl, IManager<ContractProfitPool, String> manager) {
		super(shopBalanceDateDtl, manager);
	}

	/**
	 * @see topmall.fas.domain.handler.BaseCalculateHandler#getEnableContractPool()
	 */
	@Override
	protected void getEnableContractPool() {
		ContractProfitPoolManager contractProfitPoolManager = (ContractProfitPoolManager) manager;
		Query query = Q.where("balanceDateId", shopBalanceDateDtl.getId());
		contractPoolList = contractProfitPoolManager.selectGroupProfitData(query);
	}

	/**
	 * @see topmall.fas.domain.handler.BaseCalculateHandler#calculateCost()
	 */
	@Override
	public List<CounterCost> calculateCost(boolean isHisCost) {

		ContractProfitPoolManager contractProfitPoolManager = (ContractProfitPoolManager) manager;

		List<CounterCost> resultList = new ArrayList<>();

		//1. 获取结算期内专柜合同中抽成的有效条款(条款汇总)
		getEnableContractPool();

		//2. 根据有效的合同条款根据扣率类型来生成不同的扣率费用
		for (ContractProfitPool contractProfitPool : contractPoolList) {

			// 构造费用对象
			CounterCost counterCost = getCounterCost();
			counterCost.setTaxFlag(contractProfitPool.getTaxFlag());
			counterCost.setTaxRate(contractProfitPool.getRaxRate());
			counterCost.setBillDebit(contractProfitPool.getBillDebit());
			counterCost.setAccountDebit(contractProfitPool.getAccountDebit());
			counterCost.setCostNo(contractProfitPool.getCostNo());
			counterCost.setCostName(contractProfitPool.getCostName());

			taxFlag = contractProfitPool.getTaxFlag();
			raxRate = contractProfitPool.getRaxRate();

			//①： 查询出合同条款的阶梯扣
			Query query = Q.where("balanceDateId", shopBalanceDateDtl.getId())
					.and("costNo", contractProfitPool.getCostNo())
					.and("discountType", contractProfitPool.getDiscountType())
					.and("divisionNo", contractProfitPool.getDivisionNo())
					.and("startDate", contractProfitPool.getStartDate())
					.and("endDate", contractProfitPool.getEndDate());

			//②： 查询阶梯扣的列表
			List<QuotaStepDTO> quotaStepList = contractProfitPoolManager.selectQuotaStep(query);
			
			boolean isRebate = false;// 是否为返利抽成条款： 如果条款的阶梯中有负数的阶梯就是返利条款，否则就不是。
			
			//③ ：将有效合同条款(抽成)池的结算期id更新进去
			for (QuotaStepDTO quotaStepDTO : quotaStepList) {
				ContractProfitPool tempProfilPool = new ContractProfitPool();
				tempProfilPool.setId(quotaStepDTO.getId());
				tempProfilPool.setBalanceDateId(shopBalanceDateDtl.getId());
				
				if(quotaStepDTO.getDiscountRate().compareTo(new BigDecimal(0)) < 0 ){
					isRebate = true;
				}
				
				manager.update(tempProfilPool);
			}

			BigDecimal profitCost = new BigDecimal(0);

			Short discountType = contractProfitPool.getDiscountType(); // '扣率类型(1:销售阶梯扣;2:销售码阶梯扣;3:折扣阶梯扣)'
			Short uniformDiscountFlag = contractProfitPool.getUniformDiscountFlag(); //是否统一扣率(0:否;1:是)
			Short reckonBase = contractProfitPool.getReckonBase();// 计算基数(1:毛收入;2:净收入)
			BigDecimal rechonBaseCost = new BigDecimal(0);
			
			Query saleProfitQuery = Q.where("shopNo", shopBalanceDateDtl.getShopNo())
					.and("counterNo", shopBalanceDateDtl.getCounterNo())
					.and("startDate", shopBalanceDateDtl.getSettleStartDate())
					.and("endDate", shopBalanceDateDtl.getSettleEndDate())
					.and("divisionNo", contractProfitPool.getDivisionNo())
					.and("isHisCost", isHisCost);
			BigDecimal saleProfitSum = CommonStaticManager.getCounterSaleCostDtlManager().querySaleProfit(
					saleProfitQuery);

			//④：如果是销售阶梯扣或者是销售码阶梯扣，则需要获取结算期内所有销售或者销售码的总额
			if (1 == discountType.shortValue() || 2 == discountType.shortValue()) {

				DaySaleQueryDto saleDataQuery = new DaySaleQueryDto(shopBalanceDateDtl.getShopNo(),
						shopBalanceDateDtl.getSettleStartDate(), shopBalanceDateDtl.getSettleEndDate());
				saleDataQuery.setCounterNo(shopBalanceDateDtl.getCounterNo());

				if (2 == discountType.shortValue()) {
					saleDataQuery.setDivisionNo(contractProfitPool.getDivisionNo());
				}

				PosSaleSumCostDto posSaleSumCostDto = shopDaySaleApiService.selectSaleDataSum(saleDataQuery, null);

				if (1 == reckonBase.shortValue()) {
					rechonBaseCost = posSaleSumCostDto.getSaleSumAmount();
				} else if (2 == reckonBase.shortValue()) {
					rechonBaseCost = posSaleSumCostDto.getNetIncomeSumAmount();
				}

				//根据阶梯扣算出扣率值
				profitCost = CommonUtil.getQuatoStepCost(quotaStepList, rechonBaseCost,
						(0 == uniformDiscountFlag.shortValue()));
				
				// 如果是返利条款 则不需要减去销售提成
				if(!isRebate){
					profitCost = profitCost.subtract(saleProfitSum);
				}

				// 折扣阶梯扣，则需要按照折扣汇总结算期内的销售数据获取每个折扣的销售总额 
			} else if (3 == discountType.shortValue()) {

				DaySaleQueryDto saleDiscountQuery = new DaySaleQueryDto(shopBalanceDateDtl.getShopNo(),
						shopBalanceDateDtl.getSettleStartDate(), shopBalanceDateDtl.getSettleEndDate());
				saleDiscountQuery.setCounterNo(shopBalanceDateDtl.getCounterNo());

				// 把销售按照折扣汇总数据查询出来
				List<PosSaleSumCostDto> saleSumCostList = shopDaySaleApiService.queryDiscountGroup(saleDiscountQuery,
						null);
				for (PosSaleSumCostDto posSaleSumCostDto : saleSumCostList) {

					if (1 == reckonBase.shortValue()) {
						rechonBaseCost = posSaleSumCostDto.getSaleSumAmount();
					} else if (2 == reckonBase.shortValue()) {
						rechonBaseCost = posSaleSumCostDto.getNetIncomeSumAmount();
					}

					//根据阶梯扣算出扣率值
					profitCost = profitCost.add(CommonUtil.getDicountStepCost(quotaStepList, rechonBaseCost,
							posSaleSumCostDto.getDiscount()));
				}
				
				// 如果是返利条款 则不需要减去销售提成
				if(!isRebate){
					profitCost = profitCost.subtract(saleProfitSum);
				}
			}

			totalCost = totalCost.add(profitCost);

			setCounterCost(profitCost, counterCost);

			counterCost.setRefId(contractProfitPool.getRefId());
			counterCost.setRefType((short)2);
			
			resultList.add(counterCost);
		}

		return resultList;
	}

}
