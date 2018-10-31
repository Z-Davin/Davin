package topmall.fas.manager.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.mercury.basic.query.Q;
import cn.mercury.basic.query.Query;
import cn.mercury.manager.ManagerException;
import topmall.fas.domain.handler.GuaraMallCalculateHandler;
import topmall.fas.domain.handler.OtherMallCalculateHandler;
import topmall.fas.domain.handler.ProfitMallCalculateHandler;
import topmall.fas.domain.handler.RentMallCalculateHandler;
import topmall.fas.dto.ContractMainDto;
import topmall.fas.enums.StatusEnums;
import topmall.fas.enums.TaxFlagEnums;
import topmall.fas.manager.IBillMallBalanceManager;
import topmall.fas.manager.IContractDiscoPoolManager;
import topmall.fas.manager.IContractGuaraPoolManager;
import topmall.fas.manager.IContractOtherPoolManager;
import topmall.fas.manager.IContractProfitPoolManager;
import topmall.fas.manager.IContractRentPoolManager;
import topmall.fas.manager.IMallBalanceDateDtlManager;
import topmall.fas.manager.IMallCostManager;
import topmall.fas.manager.IMallPayManager;
import topmall.fas.manager.IMallSaleCostManager;
import topmall.fas.model.BillMallBalance;
import topmall.fas.model.MallBalanceDateDtl;
import topmall.fas.model.MallCost;
import topmall.fas.model.MallSaleCost;
import topmall.fas.service.IMallCostService;
import topmall.fas.service.IMallPayService;
import topmall.fas.service.IMallSaleCostService;
import topmall.fas.util.CommonResult;
import topmall.fas.util.CommonUtil;
import topmall.fas.util.PublicConstans;
import topmall.framework.core.CodingRuleHelper;
import topmall.framework.manager.BaseManager;
import topmall.framework.security.Authorization;
import topmall.framework.service.IService;
import topmall.mdm.model.Bunk;
import topmall.mdm.model.Counter;
import topmall.mdm.model.Depayment;
import topmall.mdm.open.api.IBunkApiService;
import topmall.mdm.open.api.IBunkGroupApiService;
import topmall.mdm.open.api.ICounterApiService;
import topmall.mdm.open.api.IDepaymentApiService;

@Service
public class MallCostManager extends BaseManager<MallCost, String> implements IMallCostManager {
	@Autowired
	private IMallCostService service;
	@Reference
	private IBunkGroupApiService bunkGroupApiService;
	@Reference
	private ICounterApiService counterApiService;
	@Reference
	private IBunkApiService bunkApiService;
	@Autowired
	private IContractDiscoPoolManager contractDiscoPoolManager;

	@Autowired
	private IContractGuaraPoolManager contractGuaraPoolManager;

	@Autowired
	private IContractOtherPoolManager contractOtherPoolManager;

	@Autowired
	private IContractProfitPoolManager contractProfitPoolManager;

	@Autowired
	private IContractRentPoolManager contractRentPoolManager;

	@Autowired
	private IMallPayManager mallPayManager;

	@Autowired
	private IMallBalanceDateDtlManager mallBalanceDateDtlManager;

	@Autowired
	private IBillMallBalanceManager billMallBalanceManager;

	@Autowired
	private IMallSaleCostManager mallSaleCostManager;

	@Reference
	private IDepaymentApiService depaymentApiService;
	
	@Autowired
	private IMallSaleCostService mallSaleCostService;
	
	@Autowired
	private IMallPayService mallPayService;

	
	protected IService<MallCost, String> getService() {
		return service;
	}

	@Override
	public Integer update(MallCost entry) {
		try {
			//防止税率改变,因此这里需要重新算费用
			if (entry.getTaxFlag().equals(TaxFlagEnums.INCLUDE.getFlag())) {
				entry.setAbleAmount(CommonUtil.getTaxFreeCost(entry.getAbleSum(), entry.getTaxRate()));
				entry.setTaxAmount(entry.getAbleSum().subtract(entry.getAbleAmount()));
			}
			return super.update(entry);
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public Integer insert(MallCost entry) {
		if (entry.getStatus() == null) {
			entry.setStatus(StatusEnums.INEFFECTIVE.getStatus());
		}
		if (entry.getTaxFlag() == null) {
			entry.setTaxFlag(TaxFlagEnums.INCLUDE.getFlag());
		}
		if (null == entry.getSeqId()) {
			String orderNo = entry.getShopNo()
					+ CodingRuleHelper.getBasicCoding(PublicConstans.MALL_COST_NO, entry.getShopNo(), null);
			entry.setSeqId(orderNo);
		}
		// 应结价款=应结总额/(1+税率)
		entry.setAbleAmount(CommonUtil.getTaxFreeCost(entry.getAbleSum(), entry.getTaxRate()));
		if (null == entry.getSource()) {
			entry.setSource((short) 3);
		}
		entry.setTaxAmount(entry.getAbleSum().subtract(entry.getAbleAmount()));
//		entry.setMallAmount(entry.getAbleSum());
		return super.insert(entry);
	}

	/**
	 * @see topmall.framework.manager.BaseManager#batchSave(java.util.List, java.util.List, java.util.List)
	 */
	@Override
	public Integer batchSave(List<MallCost> inserted, List<MallCost> updated, List<MallCost> deleted){
		if (inserted != null) {
			String orderNo = inserted.get(0).getShopNo()
					+ CodingRuleHelper.getBasicCoding(PublicConstans.MALL_COST_NO, inserted.get(0).getShopNo(), null);
			for (MallCost mallCost : inserted) {
				mallCost.setSeqId(orderNo);
//				mallCost.setMallAmount(mallCost.getAbleSum());
			}
		}
		return super.batchSave(inserted, updated, deleted);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public CommonResult batchConfirm(String[] ids) {
		Query query = Query.empty().and("keyValue", ids).and("operate", "auditor").and("operateTime", "audit_time")
				.and("user", Authorization.getUser().getName()).and("time", new Date())
				.and("status", StatusEnums.EFFECTIVE.getStatus())
				.and("originStatus", StatusEnums.INEFFECTIVE.getStatus()).and("keyName", "id");
		Integer result = this.service.updateStatus(query);
		if (result != ids.length) {
			throw new ManagerException("所选单据中存在非制单状态的单据，不能确认。");
		} else {
			return CommonResult.sucess("successful");
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public CommonResult batchUnConfirm(String[] ids) {
		Query query = Query.empty().and("keyValue", ids).and("operate", "auditor").and("operateTime", "audit_time")
				.and("operate", "auditor").and("user", null).and("time", null)
				.and("status", StatusEnums.INEFFECTIVE.getStatus())
				.and("originStatus", StatusEnums.EFFECTIVE.getStatus()).and("keyName", "id").and("unConfirm", true);

		Integer result = this.service.updateStatus(query);
		if (result != ids.length) {
			throw new ManagerException("所选单据中存在非生效状态的单据，不能确认。");
		} else {
			return CommonResult.sucess("successful");
		}
	}
	
	/**
	 * @see topmall.fas.manager.IMallCostManager#generateMallCost(topmall.fas.model.MallBalanceDateDtl)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public void generateMallCost(MallBalanceDateDtl mallBalanceDateDtl) {
		// 获取有效合同条款
		ContractMainDto contractMainDto = mallBalanceDateDtlManager.rebuildValidContract(mallBalanceDateDtl);
		if (null != contractMainDto && contractMainDto.isHasEnaleItem()) {
			// 生成物业合同下 所有费用
			createMallCost(mallBalanceDateDtl, contractMainDto, false);
			// 将结算期明细 修改状态为已生成费用
			mallBalanceDateDtl.setStatus(StatusEnums.GENERATE_COST.getStatus());
			mallBalanceDateDtlManager.update(mallBalanceDateDtl);
		} else {
			logger.info("未获取到有效合同, 生成费用失败, 店铺：" + mallBalanceDateDtl.getShopNo() + "物业  "
					+ mallBalanceDateDtl.getMallNo());
			throw new ManagerException("卖场  " + mallBalanceDateDtl.getShopNo() + "物业  "
					+ mallBalanceDateDtl.getMallNo() + "没有有效的合同条款");
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public CommonResult recalculationCost(MallBalanceDateDtl mallBalanceDateDtl) {

		CommonResult result = CommonResult.getSucessResult();

		//先删除结算期的有效合同条款 再重新获取有效合同条款
		ContractMainDto contractMainDto = mallBalanceDateDtlManager.rebuildValidContract(mallBalanceDateDtl);

		//删除费用
		delAllCost(mallBalanceDateDtl);

		if (null != contractMainDto && contractMainDto.isHasEnaleItem()) {

			// 生成物业合同下 所有费用
			createMallCost(mallBalanceDateDtl, contractMainDto, true);

			// 将结算期明细 修改状态为已生成费用
			mallBalanceDateDtl.setStatus(StatusEnums.GENERATE_COST.getStatus());
			mallBalanceDateDtlManager.update(mallBalanceDateDtl);
		} else {
			String errorMsg = "未获取到有效合同, 生成费用失败, 店铺：" + mallBalanceDateDtl.getShopNo() + "物业  "
					+ mallBalanceDateDtl.getMallNo() + "铺位组：" + mallBalanceDateDtl.getBunkGroupNo();
			logger.info(errorMsg);
			throw new ManagerException(errorMsg);
		}

		return result;
	}

	/**
	 * 生成物业所有费用
	 * @param mallBalanceDateDtl 物业结算期明细
	 * @param contractMainDto 合同主表
	 * @param isRecalculation 是否重算 true 是重算, false不是重算
	 */
	private void createMallCost(MallBalanceDateDtl mallBalanceDateDtl, ContractMainDto contractMainDto, boolean isRecalculation) {
		Query q = Q.where("shopNo", mallBalanceDateDtl.getShopNo()).and("bunkGroupNo", mallBalanceDateDtl.getBunkGroupNo());
		List<Bunk> list = bunkApiService.selectByParams(q);
		List<String> counterList = new ArrayList<>();
		for (Bunk bunk : list) {
			q.and("bunkNo", bunk.getBunkNo());
			List<Counter> cList = counterApiService.selectByParams(q);
			for (Counter counter : cList) {
				counterList.add(counter.getCounterNo());
			}
		}
		if (!CommonUtil.hasValue(counterList)) {
			logger.info("物业公司:" + mallBalanceDateDtl.getMallNo() + "卖场:" + mallBalanceDateDtl.getShopNo()
					+ "下面没有找到专柜信息....");
			throw new ManagerException("物业公司:" + mallBalanceDateDtl.getMallNo() + "卖场:"
					+ mallBalanceDateDtl.getShopNo() + "下面没有找到专柜信息....");
		}
		mallBalanceDateDtl.setCounterList(counterList);

		// 生成销售相关的费用
		contractDiscoPoolManager.createMallDiscoCost(mallBalanceDateDtl,isRecalculation);

		// 生成支付信息
		mallPayManager.generateMallPayData(mallBalanceDateDtl);
		// 生成物业的 租金、抽成、保底、其他的扣项费用
		List<MallCost> mallCostList = new ArrayList<>();
		GuaraMallCalculateHandler guaraMallCalculateHandler = new GuaraMallCalculateHandler(contractGuaraPoolManager,
				mallBalanceDateDtl);
		List<MallCost> guaraCostList = guaraMallCalculateHandler.calculateCost();

		OtherMallCalculateHandler otherMallCalculateHandler = new OtherMallCalculateHandler(contractOtherPoolManager,
				mallBalanceDateDtl);
		List<MallCost> otherCostList = otherMallCalculateHandler.calculateCost();

		// 获取合同主表表头信息  未达保底，不计算抽成(0-否，1-是)
		short unGuaraUnProfit = contractMainDto.getUnGuaraUnProfit();

		ProfitMallCalculateHandler profitMallCalculateHandler = new ProfitMallCalculateHandler(
				contractProfitPoolManager, mallBalanceDateDtl, contractMainDto.getBunkGroupNo());
		List<MallCost> profitCostList = new ArrayList<>();

		// 将基础抽成的 扣费对象构造出来
		MallCost baseProfitCost = profitMallCalculateHandler.getMallCost();

		Query query = Q.q("zoneNo", mallBalanceDateDtl.getZoneNo()).and("deductionNo", "1001130");
		Depayment depayment = depaymentApiService.findByParam(query);

		if (null == depayment) {
			logger.error("基础抽成的扣项->1001130  大区：" + mallBalanceDateDtl.getZoneNo() + "未配置");
			throw new ManagerException("基础抽成的扣项->1001130  大区：" + mallBalanceDateDtl.getZoneNo() + "未配置");
		}

		// 当未达保底，不计算抽成为否  或者  达到保底 就需要计算抽成
		if (0 == unGuaraUnProfit || guaraMallCalculateHandler.reachGuara) {
			profitCostList = profitMallCalculateHandler.calculateCost();
		}

		RentMallCalculateHandler rentMallCalculateHandler = new RentMallCalculateHandler(contractRentPoolManager,
				mallBalanceDateDtl, contractMainDto.getBunkGroupNo());
		List<MallCost> rentCostList = rentMallCalculateHandler.calculateCost();

		Query saleProfitQuery = Q.where("shopNo", mallBalanceDateDtl.getShopNo())
				.and("startDate", mallBalanceDateDtl.getSettleStartDate())
				.and("endDate", mallBalanceDateDtl.getSettleEndDate()).and("mallNo", mallBalanceDateDtl.getMallNo());
		BigDecimal saleProfitSum = CommonStaticManager.getMallSaleCostManager().queryMallSaleProfit(saleProfitQuery);

		baseProfitCost = setBaseCostValue(baseProfitCost, depayment, saleProfitSum.multiply(new BigDecimal(-1)));

		// 租金、抽成取高
		if (2 == contractMainDto.getBaseCostType().intValue()) {
			BigDecimal profitCost = profitMallCalculateHandler.totalCost;
			BigDecimal rentCost = rentMallCalculateHandler.totalCost;

			if ((profitCost.add(saleProfitSum)).compareTo(rentCost) <= 0) {
				setCostCancel(profitCostList, "租金、抽成取高");
				profitCostList.add(baseProfitCost);
			} else {
				setCostCancel(rentCostList, "租金、抽成取高");
			}
		} else if (3 == contractMainDto.getBaseCostType().intValue()) {// 租金、抽成+保底取高
			BigDecimal rentCost = rentMallCalculateHandler.totalCost;
			BigDecimal profitCost = profitMallCalculateHandler.totalCost;
			BigDecimal guaraCost = guaraMallCalculateHandler.totalCost;

			// 如果达到保底 就只需要比较 租金和抽成，否则 就要三者取其高
			if (guaraMallCalculateHandler.reachGuara) {
				if (rentCost.compareTo(profitCost.add(saleProfitSum)) <= 0) {
					setCostCancel(rentCostList, "租金、抽成+保底取高");
				} else {
					setCostCancel(profitCostList, "租金、抽成+保底取高");
					profitCostList.add(baseProfitCost);
				}
			} else {
				BigDecimal maxCost = rentCost.max(profitCost.add(saleProfitSum)).max(guaraCost.add(saleProfitSum));

				if (maxCost.compareTo(rentCost) == 0) {
					setCostCancel(profitCostList, "租金、抽成+保底取高");
					setCostCancel(guaraCostList, "租金、抽成+保底取高");
					profitCostList.add(baseProfitCost);
				} else if (maxCost.compareTo(profitCost.add(saleProfitSum)) == 0) {
					setCostCancel(rentCostList, "租金、抽成+保底取高");
					setCostCancel(guaraCostList, "租金、抽成+保底取高");
				} else {
					setCostCancel(rentCostList, "租金、抽成+保底取高");
					setCostCancel(profitCostList, "租金、抽成+保底取高");
				}
			}
		}

		mallCostList.addAll(guaraCostList);
		mallCostList.addAll(otherCostList);
		mallCostList.addAll(profitCostList);
		mallCostList.addAll(rentCostList);

		if (CommonUtil.hasValue(mallCostList)) {
			batchSave(mallCostList, null, null);
		} else {
			logger.info("系统自动生成费用为空: " + mallBalanceDateDtl.toString());
		}
	}

	/**
	 * 删除结算单、删除系统生成的费用、解除手动提交费用与结算单的关系
	 * @param mallBalanceDateDtl 物业结算期明细
	 */
	private void delAllCost(MallBalanceDateDtl mallBalanceDateDtl) {

		Query query = Q.where("shopNo", mallBalanceDateDtl.getShopNo()).and("mallNo", mallBalanceDateDtl.getMallNo())
				.and("settleMonth", mallBalanceDateDtl.getSettleMonth()).and("settleStartDate",mallBalanceDateDtl.getSettleStartDate()).and("settleEndDate",mallBalanceDateDtl.getSettleEndDate())
				.and("bunkGroupNo", mallBalanceDateDtl.getBunkGroupNo());

		List<BillMallBalance> billList = billMallBalanceManager.selectByParams(query);

		for (BillMallBalance billMallBalance : billList) {
			if (StatusEnums.MAKEBILL.getStatus() != billMallBalance.getStatus()) {
				throw new ManagerException("卖场:" + billMallBalance.getShopNo() + "--物业：" + billMallBalance.getMallNo()
						+ "已经生成了非制单状态的结算单,不能生成费用...");
			}
		}
		//先解绑数据,在删除
		for(BillMallBalance mallBalance:billList){
			Query q = Q.where("balanceBillNo", mallBalance.getBillNo());
			List<MallCost> list = service.selectByParams(q);
			for (MallCost cost : list) {
				service.updateToUnsettled(cost.getId());
			}
			// 解除结算单与专柜销售费用之间的关系
			List<MallSaleCost> saleList = mallSaleCostService.selectByParams(q);
			for (MallSaleCost saleCost : saleList) {
				mallSaleCostService.updateToUnsettled(saleCost.getId());
			}
		}
		
		//删除结算单
		billMallBalanceManager.deleteByParams(query);
		query=query.and("recalculation", true);
		//删除销售费用
		mallSaleCostManager.deleteByParams(query);
		//删除支付
		mallPayService.deleteByParams(query);
		// 删除系统生成的扣项费用
		query.and("source", 1);//生成方式(1系统生产, 2店铺录入,3手工提交)
		deleteByParams(query);

	}

	/**
	 * 将费用设置成未生效
	 * @param mallCostList 费用列表
	 * @param remark 将原因设置到备注字段中
	 */
	private void setCostCancel(List<MallCost> mallCostList, String remark) {
		for (MallCost mallCost : mallCostList) {
			mallCost.setStatus(StatusEnums.CANCEL.getStatus());
			mallCost.setRemark(remark);
		}
	}

	/**
	 * 基础扣费项 构建一个扣项类型
	 * @param counterCost 基础抽成的扣项
	 * @param depayment 基础抽成扣项的配置
	 * @param saleProfitSum 算出来的基础抽成
	 * @return 如果扣项的配置为空 则扣项也返回空
	 */
	private MallCost setBaseCostValue(MallCost mallCost, Depayment depayment, BigDecimal saleProfitSum) {

		mallCost.setTaxFlag(depayment.getTaxFlag());
		mallCost.setTaxRate(depayment.getTaxRate());
		mallCost.setBillDebit(depayment.getBillDebit().intValue());
		mallCost.setAccountDebit(depayment.getAccountDebit());
		mallCost.setCostNo(depayment.getDeductionNo());
		mallCost.setStatus(StatusEnums.EFFECTIVE.getStatus());
		mallCost.setRemark("取高原则抵冲基础抽成");
		mallCost.setRefType((short) 2); // 基础抽成抵扣的 显示为抽成
		if (0 == depayment.getTaxFlag().shortValue()) {
			BigDecimal taxCost = CommonUtil.getTaxCost(saleProfitSum, depayment.getTaxRate());
			mallCost.setAbleSum(taxCost);
			mallCost.setAbleAmount(saleProfitSum);
			mallCost.setTaxAmount(taxCost.subtract(saleProfitSum));
		} else if (1 == depayment.getTaxFlag().shortValue()) {
			BigDecimal taxFreeCost = CommonUtil.getTaxFreeCost(saleProfitSum, depayment.getTaxRate());
			mallCost.setAbleSum(saleProfitSum);
			mallCost.setAbleAmount(taxFreeCost);
			mallCost.setTaxAmount(saleProfitSum.subtract(taxFreeCost));
		}
		return mallCost;
	}

	/**
	 * @see topmall.fas.manager.IMallCostManager#findBySettle(cn.mercury.basic.query.Query)
	 */
	@Override
	public List<MallBalanceDateDtl> findBySettle(Query query) {
		return mallBalanceDateDtlManager.selectByParams(query);
	}

	/**
	 * @see topmall.fas.manager.IMallCostManager#importBatchSave(java.util.List)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public Integer importBatchSave(List<MallCost> inserted) {
		if (inserted != null) {
			for (MallCost entry : inserted) {
				if (entry.getTaxFlag() == null) {
					entry.setTaxFlag(TaxFlagEnums.INCLUDE.getFlag());
				}
				//含税 = 不含税 * (1+17%) 不含税 = 含税 / (1+17%) , tax_amount 税额 = 含税 - 不含税  
				if (entry.getTaxFlag().equals(TaxFlagEnums.INCLUDE.getFlag())) {//含税 设置able_sum（应结总额） 计算able_amount（应结价款）
					// 公式： 应结价款(不含税金额) = 应结总额(含税金额)/(100+税率)*100
					entry.setAbleAmount(CommonUtil.getTaxFreeCost(entry.getAbleSum(), entry.getTaxRate()));
				} else {
					entry.setAbleAmount(entry.getAbleSum());//不含税 设置able_amount（应结价款）
					// 应结总额 (含税金额)= 应结价款(不含税金额)*(100+税率)/100 
					entry.setAbleSum(CommonUtil.getTaxCost(entry.getAbleAmount(), entry.getTaxRate()));
				}
			}
		}
		return  this.batchSave(inserted, null, null);
	}

	@Override
	public List<MallCost> queryConditionSum(Query query) {
		return service.queryConditionSum(query);
	}
}
