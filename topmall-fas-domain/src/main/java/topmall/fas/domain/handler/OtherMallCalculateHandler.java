/**
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
package topmall.fas.domain.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.mercury.basic.query.IStatement;
import cn.mercury.basic.query.Q;
import cn.mercury.basic.query.Query;
import cn.mercury.manager.IManager;
import topmall.fas.dto.QuotaStepDTO;
import topmall.fas.manager.impl.CommonStaticManager;
import topmall.fas.manager.impl.ContractOtherPoolManager;
import topmall.fas.model.ContractOtherPool;
import topmall.fas.model.MallBalanceDateDtl;
import topmall.fas.model.MallCost;
import topmall.fas.model.PropertyCost;
import topmall.fas.util.CommonUtil;
import topmall.mps.api.client.dto.ReduceCostDto;
import topmall.pos.dto.PosSaleSumCostDto;
import topmall.pos.dto.query.DaySaleQueryDto;
import topmall.pos.dto.query.FreeCostQueryDto;

/**
 * 物业合同其他条款的计算
 * 
 * @author dai.j
 * @date 2017-10-17 下午2:44:13
 * @version 0.1.0 
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
public class OtherMallCalculateHandler extends BaseMallCalculateHandler<ContractOtherPool> {

	public OtherMallCalculateHandler(IManager<ContractOtherPool, String> manager,
			MallBalanceDateDtl mallBalanceDateDtl) {
		super(manager, mallBalanceDateDtl);
	}

	/**
	 * @see topmall.fas.domain.handler.BaseCalculateHandler#getEnableContractPool()
	 */
	@Override
	protected void getEnableContractPool() {
		ContractOtherPoolManager contractOtherPoolManager = (ContractOtherPoolManager) manager;
		Query query = Q.where("balanceDateId", mallBalanceDateDtl.getId());
		contractPoolList = contractOtherPoolManager.selectGroupOtherData(query);
	}

	/**
	 * @see topmall.fas.domain.handler.BaseMallCalculateHandler#calculateCost()
	 */
	@Override
	public List<MallCost> calculateCost() {

		List<MallCost> resultList = new ArrayList<>();
		
		//1. 获取结算期内专柜合同中抽成的有效条款(条款汇总)
		getEnableContractPool();
		
		//2.从营促销获取减免费用的活动列表
		Map<String, List<ReduceCostDto>> freeProNoMap = getProNoMap(mallBalanceDateDtl);

		List<PropertyCost> propertyCostList = CommonStaticManager.getPropertyCostManager()
				.selectGroupNum(mallBalanceDateDtl);

		//3. 循环其它条款，计算其它费用 
		for (ContractOtherPool contractOtherPool : contractPoolList) {
			MallCost mallCost = getMallCost();
			mallCost.setTaxFlag(contractOtherPool.getTaxFlag());
			mallCost.setTaxRate(contractOtherPool.getRaxRate());
			mallCost.setBillDebit(contractOtherPool.getBillDebit());
			mallCost.setAccountDebit(contractOtherPool.getAccountDebit());
			mallCost.setCostNo(contractOtherPool.getCostNo());
			
			
			// 根据扣费编码 获取减免活动的列表
			List<FreeCostQueryDto> freeCostList = new ArrayList<>();
			if (null != freeProNoMap) {
				List<ReduceCostDto> freeProNoListStr = freeProNoMap.get(contractOtherPool.getCostNo());
				
				// 如果减免列表不为空  则将 减免的列表 封装成查询DTO
				if(CommonUtil.hasValue(freeProNoListStr)){
					for (ReduceCostDto reduceCostDto : freeProNoListStr) {
						FreeCostQueryDto dto = new FreeCostQueryDto(reduceCostDto.getProNo(), reduceCostDto.getDivisionNo());
						dto.setReduceRate(reduceCostDto.getReduceRate());
						freeCostList.add(dto);
					}
				}
			}
			
			taxFlag = contractOtherPool.getTaxFlag();
			raxRate = contractOtherPool.getRaxRate();

			Short chargingType = contractOtherPool.getChargingType();//计费模式: 1-金额模式, 2-费率模式, 3-单价
			BigDecimal otherCost = new BigDecimal(0);

			if (1 == chargingType.shortValue()) {
				otherCost = contractOtherPool.getAmount();
			} else if (2 == chargingType.shortValue()) {
				ContractOtherPoolManager contractOtherPoolManager = (ContractOtherPoolManager) manager;
				//①： 查询出合同条款的阶梯扣
				Query query = Q.where("balanceDateId", mallBalanceDateDtl.getId())
						.and("costNo", contractOtherPool.getCostNo())
						.and("chargingType", contractOtherPool.getChargingType())
						.and("chargingBase", contractOtherPool.getChargingBase())
						.and("payNo", contractOtherPool.getPayNo()).and("startDate", contractOtherPool.getStartDate())
						.and("endDate", contractOtherPool.getEndDate());

				//②： 查询阶梯扣的列表
				List<QuotaStepDTO> quotaStepList = contractOtherPoolManager.selectQuotaStep(query);

				Short uniformDiscountFlag = contractOtherPool.getUniformDiscountFlag(); //是否统一扣率(0:否;1:是)
				Short chargingBase = contractOtherPool.getChargingBase(); //计费基数：1-毛收入,2-净收入,3-支付方式销售,4-会员卡折扣,5-团购折扣

				BigDecimal chargingBaseCost =  BigDecimal.ZERO;
				BigDecimal freeCost = BigDecimal.ZERO;

				DaySaleQueryDto saleDataQuery = new DaySaleQueryDto(mallBalanceDateDtl.getShopNo(),
						mallBalanceDateDtl.getSettleStartDate(), mallBalanceDateDtl.getSettleEndDate());
				saleDataQuery.setMallNo(mallBalanceDateDtl.getMallNo());
				saleDataQuery.setBunkGroupNo(mallBalanceDateDtl.getBunkGroupNo());

				if (1 == chargingBase.shortValue()) {
					PosSaleSumCostDto posSaleSumCostDto = mallDaySaleApiService.selectSaleDataSum(saleDataQuery, freeCostList);
					freeCost = mallDaySaleApiService.queryFreeCost(saleDataQuery, freeCostList);
					
					BigDecimal cashCost = new BigDecimal(0);
					// 查询积分抵现的金额,根据结算期明细的结算标识来判断积分抵现是否参与计算
					if(0 == mallBalanceDateDtl.getPointsCalculateFlag()) {
						saleDataQuery.setPayNo("P60");
						cashCost = mallDaySaleApiService.queryBalancePaySum(saleDataQuery, null);
					}
					chargingBaseCost = posSaleSumCostDto.getSaleSumAmount().subtract(cashCost);//毛收入-积分抵现
				} else if (2 == chargingBase.shortValue()) {
					PosSaleSumCostDto posSaleSumCostDto = mallDaySaleApiService.selectSaleDataSum(saleDataQuery, freeCostList);
					freeCost = mallDaySaleApiService.queryFreeCost(saleDataQuery, freeCostList);
					
					BigDecimal cashCost = new BigDecimal(0);
					// 查询积分抵现的金额,根据结算期明细的结算标识来判断积分抵现是否参与计算
					if(0 == mallBalanceDateDtl.getPointsCalculateFlag()) {
						saleDataQuery.setPayNo("P60");
						cashCost = mallDaySaleApiService.queryBalancePaySum(saleDataQuery, null);
					}
					chargingBaseCost = posSaleSumCostDto.getNetIncomeSumAmount().subtract(cashCost);//净收入
				} else if (3 == chargingBase.shortValue()) {
					String[] payNoList = contractOtherPool.getPayNo().split(",");
					IStatement is = Q.In("dtl.payNo", payNoList);
					saleDataQuery.setStatement(is);
					
					freeCost = mallDaySaleApiService.queryFreeCost(saleDataQuery, freeCostList);
					chargingBaseCost = mallDaySaleApiService.queryBalancePaySum(saleDataQuery, freeCostList);//支付方式销售 
					
					
				}

				//根据阶梯扣算出扣率值
				otherCost = CommonUtil.getQuatoStepCost(quotaStepList, chargingBaseCost,
						(0 == uniformDiscountFlag.shortValue())).add(freeCost);

				//④：将有效合同条款(其他)池的结算期id更新进去
				for (QuotaStepDTO quotaStepDTO : quotaStepList) {
					ContractOtherPool otherPool = new ContractOtherPool();
					otherPool.setId(quotaStepDTO.getId());
					otherPool.setBalanceDateId(mallBalanceDateDtl.getId());
					manager.update(otherPool);
				}

			} else if (3 == chargingType.shortValue()) {
				BigDecimal number = new BigDecimal(0);
				for (PropertyCost propertyCost : propertyCostList) {
					if (contractOtherPool.getCostNo().equals(propertyCost.getCostNo())) {
						number = propertyCost.getNumber();
						break;
					}
				}
				otherCost = contractOtherPool.getUnitPrice().multiply(number);
			}

			totalCost = totalCost.add(otherCost);

			setMallCost(otherCost, mallCost);
			mallCost.setRefId(contractOtherPool.getRefId());
			mallCost.setRefType((short) 4);

			resultList.add(mallCost);
		}
		return resultList;
	}

	/**
	 * 调用营促销接口查询物业所有减免费用的活动列表
	 * @param mallBalanceDateDtl 物业结算期
	 * @return 所有列表的集合
	 */
	private Map<String, List<ReduceCostDto>> getProNoMap(MallBalanceDateDtl mallBalanceDateDtl) {
		return CommonStaticManager.getPromotionApi().getMallReduceCostInfos(mallBalanceDateDtl.getSettleStartDate(),
				mallBalanceDateDtl.getSettleEndDate(), mallBalanceDateDtl.getShopNo(), mallBalanceDateDtl.getMallNo(),
				mallBalanceDateDtl.getBunkGroupNo());
	}
}
