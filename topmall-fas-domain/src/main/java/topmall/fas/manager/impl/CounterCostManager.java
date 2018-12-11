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

import cn.mercury.basic.query.IStatement;
import cn.mercury.basic.query.Pagenation;
import cn.mercury.basic.query.Q;
import cn.mercury.basic.query.Query;
import cn.mercury.manager.ManagerException;
import topmall.fas.domain.handler.GuaraCalculateHandler;
import topmall.fas.domain.handler.OtherCalculateHandler;
import topmall.fas.domain.handler.ProfitCalculateHandler;
import topmall.fas.domain.handler.RentCalculateHandler;
import topmall.fas.dto.ContractMainDto;
import topmall.fas.enums.StatusEnums;
import topmall.fas.enums.TaxFlagEnums;
import topmall.fas.manager.IContractDiscoPoolManager;
import topmall.fas.manager.IContractGuaraPoolManager;
import topmall.fas.manager.IContractOtherPoolManager;
import topmall.fas.manager.IContractProfitPoolManager;
import topmall.fas.manager.IContractRentPoolManager;
import topmall.fas.manager.ICounterCostManager;
import topmall.fas.manager.IShopBalanceDateDtlManager;
import topmall.fas.model.BillCounterBalance;
import topmall.fas.model.CounterCost;
import topmall.fas.model.ShopBalanceDateDtl;
import topmall.fas.service.IBillCounterBalanceService;
import topmall.fas.service.ICounterCostService;
import topmall.fas.service.ICounterSaleCostDtlService;
import topmall.fas.service.ICounterSaleCostService;
import topmall.fas.util.CommonResult;
import topmall.fas.util.CommonUtil;
import topmall.fas.util.PublicConstans;
import topmall.framework.core.CodingRuleHelper;
import topmall.framework.manager.BaseManager;
import topmall.framework.security.Authorization;
import topmall.framework.service.IService;
import topmall.mdm.model.Counter;
import topmall.mdm.model.Deduction;
import topmall.mdm.model.Depayment;
import topmall.mdm.open.api.ICounterApiService;
import topmall.mdm.open.api.IDeductionApiService;
import topmall.mdm.open.api.IDepaymentApiService;

@Service
public class CounterCostManager extends BaseManager<CounterCost, String> implements ICounterCostManager {

	@Autowired
	private ICounterCostService service;

	@Autowired
	private IShopBalanceDateDtlManager shopBalanceDateDtlManager;

	@Autowired
	private ICounterSaleCostService counterSaleCostService;

	@Autowired
	private IContractGuaraPoolManager contractGuaraPoolManager;

	@Autowired
	private IContractOtherPoolManager contractOtherPoolManager;

	@Autowired
	private IContractProfitPoolManager contractProfitPoolManager;

	@Autowired
	private IContractDiscoPoolManager contractDiscoPoolManager;

	@Autowired
	private IContractRentPoolManager contractRentPoolManager;

	@Reference
	private IDeductionApiService deductionApiService;

	@Autowired
	private IBillCounterBalanceService billCounterBalanceService;

	@Autowired
	private ICounterSaleCostDtlService counterSaleCostDtlService;

	@Reference
	private IDepaymentApiService depaymentApiService;

	@Reference
	private ICounterApiService counterApiService;

	protected IService<CounterCost, String> getService() {
		return service;
	}

	@Override
	public Integer update(CounterCost entry) {
		try {
			//防止税率改变,因此这里需要重新算费用
			if (entry.getTaxFlag().equals(TaxFlagEnums.INCLUDE.getFlag())) {
				entry.setAbleAmount(CommonUtil.getTaxFreeCost(entry.getInputCostAmount(), entry.getTaxRate()));
				entry.setAbleSum(entry.getInputCostAmount());
			} else if (entry.getTaxFlag().equals(TaxFlagEnums.NOT_INCLUDE.getFlag())) {
				entry.setAbleSum(CommonUtil.getTaxCost(entry.getInputCostAmount(), entry.getTaxRate()));
				entry.setAbleAmount(entry.getInputCostAmount());
			}

			entry.setTaxAmount(entry.getAbleSum().subtract(entry.getAbleAmount()));
			return super.update(entry);
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public Integer insert(CounterCost entry) {
		//***entry.getAbleSum()是导入的不确定金额  "*扣项金额" : 'ableSum',
		if (entry.getStatus() == null) {
			entry.setStatus(StatusEnums.INEFFECTIVE.getStatus());
		}
		if (entry.getTaxFlag() == null) {
			entry.setTaxFlag(TaxFlagEnums.INCLUDE.getFlag());
		}
		if (null == entry.getSeqId()) {
			String orderNo = entry.getShopNo()
					+ CodingRuleHelper.getBasicCoding(PublicConstans.COUNTER_COST_NO, entry.getShopNo(), null);
			entry.setSeqId(orderNo);
		}

		//获取结算期 （根据结算月获取）
		Query query = Q.where("shopNo", entry.getShopNo()).and("counterNo", entry.getCounterNo())
				.and("settleMonth", entry.getSettleMonth()).and("settleStartDate", entry.getSettleStartDate())
				.and("settleEndDate", entry.getSettleEndDate());

		IStatement is = Q.In("status", new int[] { StatusEnums.EFFECTIVE.getStatus(),
				StatusEnums.GENERATE_COST.getStatus(), StatusEnums.GENERATE_BALANCE.getStatus() });
		query.and(is);

		List<ShopBalanceDateDtl> list = shopBalanceDateDtlManager.selectByParams(query);
		if (list != null && !list.isEmpty()) {
			entry.setSettleStartDate(list.get(0).getSettleStartDate());
			entry.setSettleEndDate(list.get(0).getSettleEndDate());
		} else {
			throw new ManagerException("卖场编码" + entry.getShopNo() + ",专柜编码" + entry.getCounterNo() + "没有找到有效的结算期");
		}

		if (null == entry.getSource()) {
			entry.setSource((short) 3);
		}
		entry.setTaxAmount(entry.getAbleSum().subtract(entry.getAbleAmount()));
		return super.insert(entry);

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public CounterCost confirm(String id) {
		CounterCost counterCost = service.findByPrimaryKey(id);
		counterCost.setAuditor(Authorization.getUser().getName());
		counterCost.setAuditTime(new Date());
		counterCost.setStatus(StatusEnums.EFFECTIVE.getStatus());
		service.update(counterCost);
		return counterCost;
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
			throw new ManagerException("所选单据中存在生效状态的单据，不能确认。");
		} else {
			return CommonResult.sucess("successful");
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public Integer batchSave(List<CounterCost> inserted, List<CounterCost> updated, List<CounterCost> deleted) {
		if (inserted != null) {
			String orderNo = inserted.get(0).getShopNo() + CodingRuleHelper
					.getBasicCoding(PublicConstans.COUNTER_COST_NO, inserted.get(0).getShopNo(), null);
			for (CounterCost t : inserted) {
				t.setSeqId(orderNo);
			}
		}
		return super.batchSave(inserted, updated, deleted);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public Integer importBatchSave(List<CounterCost> inserted, List<CounterCost> updated, List<CounterCost> deleted) {
		if (inserted != null) {
			for (CounterCost entry : inserted) {
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
		return this.batchSave(inserted, updated, deleted);
	}

	@Override
	public List<ShopBalanceDateDtl> findBySettle(Query query) {
		return shopBalanceDateDtlManager.selectByParams(query);
	}

	/**
	 * @see topmall.fas.manager.ICounterCostManager#generateCounterCost(topmall.fas.model.ShopBalanceDateDtl)
	 */
	@Override
	public void generateCounterCost(ShopBalanceDateDtl shopBalanceDateDtl) {
		// 先删除结算期的有效合同条款 再重新获取有效合同条款
		ContractMainDto contractMainDto = shopBalanceDateDtlManager.rebuildValidContract(shopBalanceDateDtl);
		if (null != contractMainDto && contractMainDto.isHasEnaleItem()) {

			//生成专柜 所有费用
			List<CounterCost> counterAllCost = createContractCost(shopBalanceDateDtl, contractMainDto, false,
					shopBalanceDateDtl);
			if (CommonUtil.hasValue(counterAllCost)) {
				batchSave(counterAllCost, null, null);
			} else {
				logger.info("系统自动生成费用为空->" + shopBalanceDateDtl.toString());
			}

			shopBalanceDateDtl.setStatus(StatusEnums.GENERATE_COST.getStatus());
			shopBalanceDateDtlManager.update(shopBalanceDateDtl);
		} else {

			Query query = Q.where("shopNo", shopBalanceDateDtl.getShopNo()).and("counterNo",
					shopBalanceDateDtl.getCounterNo());
			Counter counter = counterApiService.findByParam(query);

			// 如果装柜的状态是3 待清退状态 ，那就直接计算费用不用获取合同状态
			if (StatusEnums.CONFIRM.getStatus().equals(counter.getStatus().intValue())) {

				//专柜是待清退状态，只生成专柜销售费用
				contractDiscoPoolManager.createContractDiscoCost(shopBalanceDateDtl, false, null);

			} else {

				logger.info("未获取到有效合同, 生成费用失败, 店铺" + shopBalanceDateDtl.getShopNo() + "专柜  "
						+ shopBalanceDateDtl.getCounterNo());
				throw new ManagerException("卖场  " + shopBalanceDateDtl.getShopNo() + "专柜  "
						+ shopBalanceDateDtl.getCounterNo() + "没有有效的合同");
			}
		}
	}

	/**
	 * @see topmall.fas.manager.ICounterCostManager#recalculationCost(cn.mercury.basic.query.Query)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public CommonResult recalculationCost(ShopBalanceDateDtl shopBalanceDateDtl) {

		CommonResult result = CommonResult.getSucessResult();

		//先删除结算期的有效合同条款 再重新获取有效合同条款
		ContractMainDto contractMainDto = shopBalanceDateDtlManager.rebuildValidContract(shopBalanceDateDtl);
		//删除掉以前生成的费用
		checkCost(shopBalanceDateDtl);

		if (null != contractMainDto && contractMainDto.isHasEnaleItem()) {

			//生成专柜 所有费用
			List<CounterCost> counterAllCost = createContractCost(shopBalanceDateDtl, contractMainDto, false,
					shopBalanceDateDtl);
			if (CommonUtil.hasValue(counterAllCost)) {
				batchSave(counterAllCost, null, null);
			} else {
				logger.info("系统自动生成费用为空->" + shopBalanceDateDtl.toString());
			}

			shopBalanceDateDtl.setStatus(StatusEnums.GENERATE_COST.getStatus());
			shopBalanceDateDtlManager.update(shopBalanceDateDtl);
		} else {
			Query query = Q.where("shopNo", shopBalanceDateDtl.getShopNo()).and("counterNo",
					shopBalanceDateDtl.getCounterNo());
			Counter counter = counterApiService.findByParam(query);

			// 如果装柜的状态是3 待清退状态 ，那就直接计算费用不用获取合同状态
			if (StatusEnums.CONFIRM.getStatus().equals(counter.getStatus().intValue())) {

				//专柜是待清退状态，只生成专柜销售费用
				contractDiscoPoolManager.createContractDiscoCost(shopBalanceDateDtl, false, null);

			} else {
				logger.info("未获取到有效合同, 生成费用失败, 卖场->" + shopBalanceDateDtl.getShopNo() + "专柜->"
						+ shopBalanceDateDtl.getCounterNo());
				throw new ManagerException("卖场  " + shopBalanceDateDtl.getShopNo() + "专柜  "
						+ shopBalanceDateDtl.getCounterNo() + "没有有效的合同");
			}
		}
		return result;
	}

	/**
	 * 计算专柜所有费用
	 * @param shopBalanceDateDtl 专柜结算期明细(老结算期明细,如果不是历史月份重新,则是当前月的结算期明细)
	 * @param contractMainDto 合同主表
	 * @param isHisCost 是否是历史费用重算
	 * @param newDateDtl 新结算期明细
	 * @return 返回计算出来的专柜费用列表
	 */
	private List<CounterCost> createContractCost(ShopBalanceDateDtl shopBalanceDateDtl, ContractMainDto contractMainDto,
			boolean isHisCost, ShopBalanceDateDtl newDateDtl) {

		//生成专柜销售费用
		contractDiscoPoolManager.createContractDiscoCost(shopBalanceDateDtl, isHisCost, newDateDtl);

		// 生成专柜 租金、抽成、保底、其他扣项费用 
		List<CounterCost> counterAllCost = new ArrayList<>();
		GuaraCalculateHandler guaraCalculateHandler = new GuaraCalculateHandler(shopBalanceDateDtl,
				contractGuaraPoolManager);
		List<CounterCost> guaraCostList = guaraCalculateHandler.calculateCost(isHisCost);

		OtherCalculateHandler otherCalculateHandler = new OtherCalculateHandler(shopBalanceDateDtl,
				contractOtherPoolManager);
		List<CounterCost> otherCostList = otherCalculateHandler.calculateCost(isHisCost);

		// 获取合同主表表头信息  未达保底，不计算抽成(0-否，1-是)
		short unGuaraUnProfit = contractMainDto.getUnGuaraUnProfit();

		ProfitCalculateHandler profitCalculateHandler = new ProfitCalculateHandler(shopBalanceDateDtl,
				contractProfitPoolManager);
		List<CounterCost> profitCostList = new ArrayList<>();

		// 将基础抽成的 扣费对象构造出来
		CounterCost baseProfitCost = profitCalculateHandler.getCounterCost();

		Query query = Q.q("zoneNo", shopBalanceDateDtl.getZoneNo()).and("deductionNo", "1001110");
		Depayment depayment = depaymentApiService.findByParam(query);

		if (null == depayment) {
			logger.error("基础抽成的扣项->1001110  大区：" + shopBalanceDateDtl.getZoneNo() + "未配置");
			throw new ManagerException("基础抽成的扣项->1001110  大区：" + shopBalanceDateDtl.getZoneNo() + "未配置");
		}

		// 当未达保底，不计算抽成为否  或者  达到保底 就需要计算抽成
		if (0 == unGuaraUnProfit || guaraCalculateHandler.reachGuara) {
			profitCostList = profitCalculateHandler.calculateCost(isHisCost);
		}

		RentCalculateHandler rentCalculateHandler = new RentCalculateHandler(shopBalanceDateDtl,
				contractRentPoolManager);
		List<CounterCost> rentCostList = rentCalculateHandler.calculateCost(isHisCost);

		/**
		 * 获取整个结算期的抽成
		 */
		Query saleProfitQuery = Q.where("shopNo", shopBalanceDateDtl.getShopNo())
				.and("counterNo", shopBalanceDateDtl.getCounterNo())
				.and("startDate", shopBalanceDateDtl.getSettleStartDate())
				.and("endDate", shopBalanceDateDtl.getSettleEndDate()).and("isHisCost", isHisCost);
		BigDecimal saleProfitSum = CommonStaticManager.getCounterSaleCostDtlManager().querySaleProfit(saleProfitQuery);

		// 将基础的扣项全部构建完成, 因为可能是要减掉的 所以要变成负数
		setBaseCostValue(baseProfitCost, depayment, saleProfitSum.multiply(new BigDecimal(-1)));

		// 租金、抽成取高
		if (2 == contractMainDto.getBaseCostType().intValue()) {
			BigDecimal profitCost = profitCalculateHandler.totalCost;
			BigDecimal rentCost = rentCalculateHandler.totalCost;

			if ((profitCost.add(saleProfitSum)).compareTo(rentCost) <= 0) {
				setCostCancel(profitCostList, "租金、抽成取高");
				profitCostList.add(baseProfitCost);
			} else {
				setCostCancel(rentCostList, "租金、抽成取高");
			}
		} else if (3 == contractMainDto.getBaseCostType().intValue()) {// 租金、抽成+保底取高
			BigDecimal rentCost = rentCalculateHandler.totalCost;
			BigDecimal profitCost = profitCalculateHandler.totalCost;
			BigDecimal guaraCost = guaraCalculateHandler.totalCost;

			String remarkStr = "租金、抽成+保底取高";// 租金、抽成+保底取高 设置到费用备注的信息

			// 如果达到保底 就只需要比较 租金和抽成，否则 就要三者取其高
			if (guaraCalculateHandler.reachGuara) {
				if (rentCost.compareTo(profitCost.add(saleProfitSum)) <= 0) {
					setCostCancel(rentCostList, remarkStr);
				} else {
					setCostCancel(profitCostList, remarkStr);
					profitCostList.add(baseProfitCost);
				}
			} else {
				BigDecimal maxCost = rentCost.max(profitCost.add(saleProfitSum)).max(guaraCost.add(saleProfitSum));

				if (maxCost.compareTo(rentCost) == 0) {
					setCostCancel(profitCostList, remarkStr);
					setCostCancel(guaraCostList, remarkStr);
					profitCostList.add(baseProfitCost);
				} else if (maxCost.compareTo(profitCost.add(saleProfitSum)) == 0) {
					setCostCancel(rentCostList, remarkStr);
					setCostCancel(guaraCostList, remarkStr);
				} else {
					setCostCancel(rentCostList, remarkStr);
					setCostCancel(profitCostList, remarkStr);
				}
			}
		}
		counterAllCost.addAll(guaraCostList);
		counterAllCost.addAll(otherCostList);
		counterAllCost.addAll(profitCostList);
		counterAllCost.addAll(rentCostList);

		return counterAllCost;
	}

	/**
	 * 删除 专柜的各种费用 方便重算费用
	 * @param shopBalanceDateDtl 结算期明细
	 */
	private void checkCost(ShopBalanceDateDtl shopBalanceDateDtl) {
		Query query = Q.where("shopNo", shopBalanceDateDtl.getShopNo())
				.and("counterNo", shopBalanceDateDtl.getCounterNo())
				.and("settleMonth", shopBalanceDateDtl.getSettleMonth())
				.and("settleStartDate", shopBalanceDateDtl.getSettleStartDate())
				.and("settleEndDate", shopBalanceDateDtl.getSettleEndDate());
		List<BillCounterBalance> billList = billCounterBalanceService.selectByParams(query);
		for (BillCounterBalance counterBalance : billList) {
			if (counterBalance.getStatus() != StatusEnums.MAKEBILL.getStatus()) {
				throw new ManagerException("卖场->" + shopBalanceDateDtl.getShopNo() + "专柜："
						+ shopBalanceDateDtl.getCounterNo() + "已经生成了非制单状态的结算单,不能生成费用...");
			}
		}

		List<ShopBalanceDateDtl> dtlLlist = shopBalanceDateDtlManager.selectByParams(query);
		for (ShopBalanceDateDtl dtl : dtlLlist) {
			if (dtl.getStatus() == StatusEnums.GENERATE_BALANCE.getStatus()) {
				throw new ManagerException("卖场->" + shopBalanceDateDtl.getShopNo() + "专柜："
						+ shopBalanceDateDtl.getCounterNo() + "已经生成了结算单,请先删除结算单,再重算费用");
			}
		}

		billCounterBalanceService.deleteByParams(query);
		query.and("source", 1);//生成方式(1系统生产, 2店铺录入,3手工提交)'
		counterSaleCostService.deleteByParams(query);

		counterSaleCostDtlService.deleteByParams(query);
		service.deleteByParams(query);
		query.and("source", null);
		//解除绑定 
		List<CounterCost> list = service.selectByParams(query);
		for (CounterCost countercost : list) {
			//set balance_bill_no = null（结算单编码清空）,status=2 （生效） 
			service.updateToUnsettled(countercost.getId());
		}
	}

	/**
	 * 将费用设置成未生效
	 * @param counterCostList 费用列表
	 * @param remark 将原因设置到备注字段中
	 */
	private void setCostCancel(List<CounterCost> counterCostList, String remark) {
		for (CounterCost counterCost : counterCostList) {
			counterCost.setStatus(StatusEnums.CANCEL.getStatus());
			counterCost.setRemark(remark);
		}
	}

	@Override
	public List<CounterCost> selectByPage(Query query, Pagenation page) {
		try {
			List<CounterCost> list = getService().selectByPage(preHandleQuery(query), page);
			for (CounterCost counterCost : list) {
				Query q = Q.where("deductionNo", counterCost.getCostNo());
				Deduction deduction = deductionApiService.findByParam(q);
				if (null != deduction) {
					counterCost.setCostName(deduction.getName());
				}
			}
			return list;
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<CounterCost> queryConditionSum(Query query) {
		return service.queryConditionSum(query);
	}

	/**
	 * 基础扣费项 构建一个扣项类型
	 * @param counterCost 基础抽成的扣项
	 * @param depayment 基础抽成扣项的配置
	 * @param saleProfitSum 算出来的基础抽成
	 * @return 如果扣项的配置为空 则扣项也返回空
	 */
	private void setBaseCostValue(CounterCost counterCost, Depayment depayment, BigDecimal saleProfitSum) {

		counterCost.setTaxFlag(depayment.getTaxFlag());
		counterCost.setTaxRate(depayment.getTaxRate());
		counterCost.setBillDebit(depayment.getBillDebit().intValue());
		counterCost.setAccountDebit(depayment.getAccountDebit());
		counterCost.setCostNo(depayment.getDeductionNo());
		counterCost.setCostName(depayment.getName());
		counterCost.setStatus(StatusEnums.EFFECTIVE.getStatus());
		counterCost.setRemark("取高原则抵冲基础抽成");
		counterCost.setRefType((short) 2); // 基础抽成抵扣的 显示为抽成
		if (0 == depayment.getTaxFlag().shortValue()) {
			BigDecimal taxCost = CommonUtil.getTaxCost(saleProfitSum, depayment.getTaxRate());
			counterCost.setAbleSum(taxCost);
			counterCost.setAbleAmount(saleProfitSum);
			counterCost.setTaxAmount(taxCost.subtract(saleProfitSum));
		} else if (1 == depayment.getTaxFlag().shortValue()) {
			BigDecimal taxFreeCost = CommonUtil.getTaxFreeCost(saleProfitSum, depayment.getTaxRate());
			counterCost.setAbleSum(saleProfitSum);
			counterCost.setAbleAmount(taxFreeCost);
			counterCost.setTaxAmount(saleProfitSum.subtract(taxFreeCost));
		}
	}

	/**
	 * @see topmall.fas.manager.ICounterCostManager#generateHisCounterCost(topmall.fas.model.ShopBalanceDateDtl, topmall.fas.model.ShopBalanceDateDtl)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public void generateHisCounterCost(ShopBalanceDateDtl oldDateDtl, ShopBalanceDateDtl newDateDtl) {

		// 先删除结算期的有效合同条款 再重新获取有效合同条款
		ContractMainDto contractMainDto = shopBalanceDateDtlManager.rebuildValidContract(oldDateDtl);
		Query query = Q.where("counterNo", oldDateDtl.getCounterNo()).and("settleMonth", oldDateDtl.getSettleMonth())
				.and("settleStartDate", oldDateDtl.getSettleStartDate())
				.and("settleEndDate", oldDateDtl.getSettleEndDate()).and("status", StatusEnums.BALANCE.getStatus());

		if (null != contractMainDto && contractMainDto.isHasEnaleItem()) {

			//生成专柜 所有费用
			List<CounterCost> counterAllCost = createContractCost(oldDateDtl, contractMainDto, true, newDateDtl);
			if (CommonUtil.hasValue(counterAllCost)) {
				changeSettleMonth(counterAllCost, newDateDtl, false);
				batchSave(counterAllCost, null, null);
			} else {
				logger.info("系统自动生成费用为空->" + oldDateDtl.toString());
			}

			List<CounterCost> counterHisCost = service.selectByParams(query);
			changeSettleMonth(counterHisCost, newDateDtl, true);
			batchSave(counterHisCost, null, null);
		} else {
			logger.info("未获取到有效合同, 生成费用失败, 卖场->" + oldDateDtl.getShopNo() + "专柜->" + oldDateDtl.getCounterNo());
			throw new ManagerException(
					"卖场  " + oldDateDtl.getShopNo() + "专柜  " + oldDateDtl.getCounterNo() + "没有有效的合同");
		}
	}

	/**
	 * 将生成出来的历史费用 修改结算期的日期和月份为新的结算期
	 * @param counterAllCost 历史月份的费用
	 * @param newDateDtl 新的结算期
	 * @param isOffset 是否是抵冲费用
	 */
	private void changeSettleMonth(List<CounterCost> counterAllCost, ShopBalanceDateDtl newDateDtl, boolean isOffset) {
		for (CounterCost counterCost : counterAllCost) {
			counterCost.setSettleMonth(newDateDtl.getSettleMonth());
			counterCost.setSettleStartDate(newDateDtl.getSettleStartDate());
			counterCost.setSettleEndDate(newDateDtl.getSettleEndDate());
			counterCost.setSource((short) 2);// 将来源设置为2 ，用于区别系统计算和手动添加
			counterCost.setStatus(StatusEnums.EFFECTIVE.getStatus());
			counterCost.setBalanceBillNo(null);
			counterCost.setAuditor(null);
			counterCost.setAuditTime(null);
			if (isOffset) {
				BigDecimal miusOne = new BigDecimal(-1);
				counterCost.setAbleAmount(counterCost.getAbleAmount().multiply(miusOne));
				counterCost.setAbleSum(counterCost.getAbleSum().multiply(miusOne));
				counterCost.setTaxAmount(counterCost.getTaxAmount().multiply(miusOne));
			}
		}
	}
}
