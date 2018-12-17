/**
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
package topmall.fas.domain.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.mercury.basic.query.Q;
import cn.mercury.basic.query.Query;
import cn.mercury.manager.IManager;
import topmall.fas.dto.QuotaStepDTO;
import topmall.fas.manager.impl.CommonStaticManager;
import topmall.fas.manager.impl.ContractProfitPoolManager;
import topmall.fas.model.ContractProfitPool;
import topmall.fas.model.MallBalanceDateDtl;
import topmall.fas.model.MallCost;
import topmall.fas.util.CommonUtil;
import topmall.pos.dto.PosSaleSumCostDto;
import topmall.pos.dto.query.DaySaleQueryDto;

/**
 * 物业合同抽成费用计算
 * 
 * @author dai.j
 * @date 2017-10-17 上午11:53:08
 * @version 0.1.0 
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
public class ProfitMallCalculateHandler extends BaseMallCalculateHandler<ContractProfitPool> {

	public ProfitMallCalculateHandler(IManager<ContractProfitPool, String> manager,
			MallBalanceDateDtl mallBalanceDateDtl, String bunkGroupNo) {
		super(manager, mallBalanceDateDtl);
	}

	/**
	 * @see topmall.fas.domain.handler.BaseCalculateHandler#getEnableContractPool()
	 */
	@Override
	protected void getEnableContractPool() {
		ContractProfitPoolManager contractProfitPoolManager = (ContractProfitPoolManager) manager;
		Query query = Q.where("balanceDateId", mallBalanceDateDtl.getId());
		contractPoolList = contractProfitPoolManager.selectGroupProfitData(query);
	}

	/**
	 * @see topmall.fas.domain.handler.BaseMallCalculateHandler#calculateCost()
	 */
	@Override
	public List<MallCost> calculateCost() {

		ContractProfitPoolManager contractProfitPoolManager = (ContractProfitPoolManager) manager;

		List<MallCost> resultList = new ArrayList<>();

		//1. 获取结算期内专柜合同中抽成的有效条款(条款汇总)
		getEnableContractPool();

		//2. 根据有效的合同条款根据扣率类型来生成不同的扣率费用
		for (ContractProfitPool contractProfitPool : contractPoolList) {

			MallCost mallCost = getMallCost();
			mallCost.setTaxFlag(contractProfitPool.getTaxFlag());
			mallCost.setTaxRate(contractProfitPool.getRaxRate());
			mallCost.setBillDebit(contractProfitPool.getBillDebit());
			mallCost.setAccountDebit(contractProfitPool.getAccountDebit());
			mallCost.setCostNo(contractProfitPool.getCostNo());

			taxFlag = contractProfitPool.getTaxFlag();
			raxRate = contractProfitPool.getRaxRate();

			//①： 查询出合同条款的阶梯扣
			Query query = Q.where("balanceDateId", mallBalanceDateDtl.getId())
					.and("costNo", contractProfitPool.getCostNo())
					.and("discountType", contractProfitPool.getDiscountType())
					.and("divisionNo", contractProfitPool.getDivisionNo())
					.and("startDate", contractProfitPool.getStartDate())
					.and("endDate", contractProfitPool.getEndDate());

			//②： 查询阶梯扣的列表
			List<QuotaStepDTO> quotaStepList = contractProfitPoolManager.selectQuotaStep(query);

			BigDecimal profitCost = new BigDecimal(0);

			Short discountType = contractProfitPool.getDiscountType(); // '扣率类型(1:销售阶梯扣;2:销售码阶梯扣;3:折扣阶梯扣)'
			Short uniformDiscountFlag = contractProfitPool.getUniformDiscountFlag(); //是否统一扣率(0:否;1:是)
			Short reckonBase = contractProfitPool.getReckonBase();// 计算基数(1:毛收入;2:净收入)

			BigDecimal rechonBaseCost = new BigDecimal(0);

			// 获取物业的基础抽成
			Query saleProfitQuery = Q.where("shopNo", mallBalanceDateDtl.getShopNo())
					.and("startDate", mallBalanceDateDtl.getSettleStartDate())
					.and("endDate", mallBalanceDateDtl.getSettleEndDate())
					.and("priceNo", contractProfitPool.getDivisionNo()).and("mallNo", mallBalanceDateDtl.getMallNo())
					.and("bunkGroupNo", mallBalanceDateDtl.getBunkGroupNo());
			BigDecimal saleProfitSum = CommonStaticManager.getMallSaleCostManager()
					.queryMallSaleProfit(saleProfitQuery);

			//③ ：如果是销售阶梯扣或者是部类码阶梯扣，则需要获取结算期内所有销售或者部类码的总额
			if (1 == discountType.shortValue() || 2 == discountType.shortValue()) {

				DaySaleQueryDto saleDataQuery = new DaySaleQueryDto(mallBalanceDateDtl.getShopNo(),
						mallBalanceDateDtl.getSettleStartDate(), mallBalanceDateDtl.getSettleEndDate());
				saleDataQuery.setBunkGroupNo(mallBalanceDateDtl.getBunkGroupNo());
				saleDataQuery.setMallNo(mallBalanceDateDtl.getMallNo());

				if (2 == discountType.shortValue()) {
					saleDataQuery.setPriceNo(contractProfitPool.getDivisionNo());
				}

				PosSaleSumCostDto posSaleSumCostDto = mallDaySaleApiService.selectSaleDataSum(saleDataQuery, null);
				
				BigDecimal cashCost = new BigDecimal(0);
				// 查询积分抵现的金额,根据结算期明细的结算标识来判断积分抵现是否参与计算
				if(0 == mallBalanceDateDtl.getPointsCalculateFlag()) {
					saleDataQuery.setPayNo("P60");
					cashCost = mallDaySaleApiService.queryBalancePaySum(saleDataQuery, null);
				}

				if (1 == reckonBase.shortValue()) {
					rechonBaseCost = posSaleSumCostDto.getSaleSumAmount().subtract(cashCost);
				} else if (2 == reckonBase.shortValue()) {
					rechonBaseCost = posSaleSumCostDto.getNetIncomeSumAmount();
				}

				//根据阶梯扣算出扣率值
				profitCost = CommonUtil
						.getQuatoStepCost(quotaStepList, rechonBaseCost, (0 == uniformDiscountFlag.shortValue()))
						.subtract(saleProfitSum);
			}

			//④：将有效合同条款(抽成)池的结算期id更新进去
			for (QuotaStepDTO quotaStepDTO : quotaStepList) {
				ContractProfitPool tempProfilPool = new ContractProfitPool();
				tempProfilPool.setId(quotaStepDTO.getId());
				tempProfilPool.setBalanceDateId(mallBalanceDateDtl.getId());
				manager.update(tempProfilPool);
			}

			totalCost = totalCost.add(profitCost);

			setMallCost(profitCost, mallCost);

			mallCost.setRefId(contractProfitPool.getRefId());
			resultList.add(mallCost);
		}
		return resultList;
	}

}
