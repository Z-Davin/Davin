/**
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
package topmall.fas.domain.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.mercury.basic.query.Q;
import cn.mercury.basic.query.Query;
import cn.mercury.manager.IManager;
import topmall.fas.dto.QuotaStepDTO;
import topmall.fas.manager.impl.CommonStaticManager;
import topmall.fas.manager.impl.ContractOtherPoolManager;
import topmall.fas.model.ContractOtherPool;
import topmall.fas.model.CounterCost;
import topmall.fas.model.ShopBalanceDateDtl;
import topmall.fas.model.ShopCost;
import topmall.fas.util.CommonUtil;
import topmall.mps.api.client.dto.ReduceCostDto;
import topmall.pos.dto.PosSaleSumCostDto;
import topmall.pos.dto.query.DaySaleQueryDto;
import topmall.pos.dto.query.FreeCostQueryDto;

/**
 * 其他费用计算
 * 
 * @author dai.j
 * @date 2017-8-29 下午6:04:13
 * @version 0.1.0 
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
public class OtherCalculateHandler extends BaseCalculateHandler<ContractOtherPool> {

	public OtherCalculateHandler(ShopBalanceDateDtl shopBalanceDateDtl, IManager<ContractOtherPool, String> manager) {
		super(shopBalanceDateDtl, manager);
	}

	/**
	 * @see topmall.fas.domain.handler.BaseCalculateHandler#getEnableContractPool()
	 */
	@Override
	protected void getEnableContractPool() {
		ContractOtherPoolManager contractOtherPoolManager = (ContractOtherPoolManager) manager;
		Query query = Q.where("balanceDateId", shopBalanceDateDtl.getId());
		contractPoolList = contractOtherPoolManager.selectGroupOtherData(query);
	}

	/**
	 * @see topmall.fas.domain.handler.BaseCalculateHandler#calculateCost()
	 */
	@Override
	public List<CounterCost> calculateCost(boolean isHisCost) {

		List<CounterCost> resultList = new ArrayList<>();

		//1. 获取结算期内专柜合同中抽成的有效条款(条款汇总)
		getEnableContractPool();

		//2.从营促销获取减免费用的活动列表
		Map<String, List<ReduceCostDto>> freeProNoMap = getProNoMap(shopBalanceDateDtl);

		List<ShopCost> shopCostList = CommonStaticManager.getShopCostManager().queryByBalanceDate(shopBalanceDateDtl);

		//3. 循环其它条款，计算其它费用 
		for (ContractOtherPool contractOtherPool : contractPoolList) {

			// 构造费用对象
			CounterCost counterCost = getCounterCost();
			counterCost.setTaxFlag(contractOtherPool.getTaxFlag());
			counterCost.setTaxRate(contractOtherPool.getRaxRate());
			counterCost.setBillDebit(contractOtherPool.getBillDebit());
			counterCost.setAccountDebit(contractOtherPool.getAccountDebit());
			counterCost.setCostNo(contractOtherPool.getCostNo());
			counterCost.setCostName(contractOtherPool.getCostName());

			// 根据扣费编码 获取减免活动的列表
			List<FreeCostQueryDto> freeCostList = new ArrayList<>();
			if (null != freeProNoMap) {
				List<ReduceCostDto> freeProNoListStr = freeProNoMap.get(contractOtherPool.getCostNo());
				
				// 如果减免列表不为空  则将 减免的列表 封装成查询DTO
				if(CommonUtil.hasValue(freeProNoListStr)){
					for (ReduceCostDto freeDtl : freeProNoListStr) {
						FreeCostQueryDto dto = new FreeCostQueryDto(freeDtl.getProNo(), freeDtl.getDivisionNo());
						dto.setReduceRate(freeDtl.getReduceRate());
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
				Query query = Q.where("balanceDateId", shopBalanceDateDtl.getId())
						.and("costNo", contractOtherPool.getCostNo())
						.and("chargingType", contractOtherPool.getChargingType())
						.and("chargingBase", contractOtherPool.getChargingBase())
						.and("payNo", contractOtherPool.getPayNo()).and("startDate", contractOtherPool.getStartDate())
						.and("endDate", contractOtherPool.getEndDate());

				//②： 查询阶梯扣的列表
				List<QuotaStepDTO> quotaStepList = contractOtherPoolManager.selectQuotaStep(query);

				Short uniformDiscountFlag = contractOtherPool.getUniformDiscountFlag(); //是否统一扣率(0:否;1:是)
				Short chargingBase = contractOtherPool.getChargingBase(); //计费基数：1-毛收入,2-净收入,3-支付方式销售,4-会员卡折扣,5-团购折扣

				BigDecimal chargingBaseCost = BigDecimal.ZERO;

				DaySaleQueryDto saleDataQuery = new DaySaleQueryDto(shopBalanceDateDtl.getShopNo(),
						shopBalanceDateDtl.getSettleStartDate(), shopBalanceDateDtl.getSettleEndDate());
				saleDataQuery.setCounterNo(shopBalanceDateDtl.getCounterNo());


				if (1 == chargingBase.shortValue()) {
					PosSaleSumCostDto posSaleSumCostDto = shopDaySaleApiService.selectSaleDataSum(saleDataQuery,
							freeCostList);
					chargingBaseCost = posSaleSumCostDto.getSaleSumAmount();//毛收入
				} else if (2 == chargingBase.shortValue()) {
					PosSaleSumCostDto posSaleSumCostDto = shopDaySaleApiService.selectSaleDataSum(saleDataQuery,
							freeCostList);
					chargingBaseCost = posSaleSumCostDto.getNetIncomeSumAmount();//净收入
				} else if (3 == chargingBase.shortValue()) {
					saleDataQuery.setPayNo(contractOtherPool.getPayNo());
					chargingBaseCost = shopDaySaleApiService.queryBalancePaySum(saleDataQuery, freeCostList);//支付方式销售
				} else if (4 == chargingBase.shortValue()) {
					saleDataQuery.setQueryType(1);
					chargingBaseCost = shopDaySaleApiService.queryDicountSum(saleDataQuery, freeCostList);//会员卡折扣
				} else if (5 == chargingBase.shortValue()) {
					saleDataQuery.setQueryType(2);
					chargingBaseCost = shopDaySaleApiService.queryDicountSum(saleDataQuery, freeCostList);//团购折扣
				}
				
				BigDecimal freeCost = shopDaySaleApiService.queryFreeCost(saleDataQuery, freeCostList);
				
				//根据阶梯扣算出扣率值
				otherCost = CommonUtil.getQuatoStepCost(quotaStepList, chargingBaseCost,
						(0 == uniformDiscountFlag.shortValue())).add(freeCost);

				//④：将有效合同条款(其他)池的结算期id更新进去
				for (QuotaStepDTO quotaStepDTO : quotaStepList) {
					ContractOtherPool otherPool = new ContractOtherPool();
					otherPool.setId(quotaStepDTO.getId());
					otherPool.setBalanceDateId(shopBalanceDateDtl.getId());
					manager.update(otherPool);
				}
			} else if (3 == chargingType.shortValue()) {
				BigDecimal number = new BigDecimal(0);
				for (ShopCost shopCost : shopCostList) {
					if (contractOtherPool.getCostNo().equals(shopCost.getCostNo())) {
						number = BigDecimal.valueOf(shopCost.getNumber());
						break;
					}
				}
				otherCost = contractOtherPool.getUnitPrice().multiply(number);
			}

			totalCost = totalCost.add(otherCost);

			setCounterCost(otherCost, counterCost);
			counterCost.setRefId(contractOtherPool.getRefId());
			counterCost.setRefType((short) 4);

			resultList.add(counterCost);
		}
		return resultList;
	}

	/**
	 * 调用营促销接口查询所有减免费用的活动列表
	 * @param shopBalanceDateDtl 结算期
	 * @return 所有列表的集合
	 */
	private Map<String, List<ReduceCostDto>> getProNoMap(ShopBalanceDateDtl shopBalanceDateDtl) {
		return  CommonStaticManager.getPromotionApi().getReduceCostInfos(
				shopBalanceDateDtl.getSettleStartDate(), shopBalanceDateDtl.getSettleEndDate(),
				shopBalanceDateDtl.getShopNo(), shopBalanceDateDtl.getCounterNo());
	}
	
}
