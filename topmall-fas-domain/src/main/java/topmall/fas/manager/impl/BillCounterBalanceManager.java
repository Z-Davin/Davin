package topmall.fas.manager.impl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.dubbo.config.annotation.Reference;
import cn.mercury.basic.query.Pagenation;
import cn.mercury.basic.query.Q;
import cn.mercury.basic.query.Query;
import cn.mercury.manager.ManagerException;
import cn.mercury.security.IUser;
import topmall.common.enums.BillTypeEnums;
import topmall.fas.dto.BatchCounterBalanceDto;
import topmall.fas.dto.CounterBalancePrint;
import topmall.fas.enums.AccountDebitEnums;
import topmall.fas.enums.BillDebitEnums;
import topmall.fas.enums.StatusEnums;
import topmall.fas.manager.IBillCounterBalanceManager;
import topmall.fas.model.BillCounterBalance;
import topmall.fas.model.CounterCost;
import topmall.fas.model.CounterSaleCost;
import topmall.fas.model.ShopBalanceDateDtl;
import topmall.fas.model.SupplierAccount;
import topmall.fas.service.IBillCounterBalanceService;
import topmall.fas.service.ICounterCostService;
import topmall.fas.service.ICounterSaleCostService;
import topmall.fas.service.IShopBalanceDateDtlService;
import topmall.fas.service.ISupplierAccountService;
import topmall.fas.util.CommonResult;
import topmall.fas.util.CommonUtil;
import topmall.framework.core.CodingRuleHelper;
import topmall.framework.manager.BaseManager;
import topmall.framework.security.Authorization;
import topmall.framework.service.IService;
import topmall.mdm.model.Company;
import topmall.mdm.model.Counter;
import topmall.mdm.model.Deduction;
import topmall.mdm.model.Shop;
import topmall.mdm.model.Supplier;
import topmall.mdm.open.api.ICompanyApiService;
import topmall.mdm.open.api.ICounterApiService;
import topmall.mdm.open.api.IDeductionApiService;
import topmall.mdm.open.api.IShopApiService;
import topmall.mdm.open.api.ISupplierApiService;

/**
 * 专柜结算单
 * 
 * @author Administrator
 *
 */
@Service
public class BillCounterBalanceManager extends BaseManager<BillCounterBalance, String>
		implements IBillCounterBalanceManager {
	@Autowired
	private IBillCounterBalanceService service;
	@Autowired
	private ICounterCostService counterCostService;
	@Autowired
	private ICounterSaleCostService counterSaleCostService;
	@Reference
	private IShopApiService shopService;
	@Reference
	private ISupplierApiService supplierService;
	@Reference
	private IDeductionApiService deductionApiService;
	@Reference
	private ICounterApiService counterApiService;
	@Autowired
	private IShopBalanceDateDtlService shopBalanceDateDtlService;
	@Reference
	private ICompanyApiService companyApiService;
	@Reference
	private IShopApiService shopApiService;
	@Autowired
	private ISupplierAccountService supplierAccountService;

	protected IService<BillCounterBalance, String> getService() {
		return service;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public BillCounterBalance save(BillCounterBalance counterBalance, int type) {
		counterBalance.setType(type);
		if (StringUtils.isEmpty(counterBalance.getBillNo())) {
			if (CommonUtil.hasValue(counterBalance.getCounterNo())
					&& !CommonUtil.hasValue(counterBalance.getSupplierNo())) {
				Counter counter = counterApiService.findByParam(Q.where("counterNo", counterBalance.getCounterNo()));
				counterBalance.setSupplierNo(counter.getSupplierNo());
			}
			if (CommonUtil.hasValue(counterBalance.getShopNo())
					&& !CommonUtil.hasValue(counterBalance.getCompanyNo())) {
				Shop shop = shopApiService.findByUnique(counterBalance.getShopNo());
				if (null != shop) {
					counterBalance.setCompanyNo(shop.getCompanyNo());
				}
			}
			Query query = counterBalance.asQuery().and("status", StatusEnums.GENERATE_COST.getStatus());
			List<ShopBalanceDateDtl> list = CommonStaticManager.getShopBalanceDateDtlManager().selectByParams(query);
			if (!CommonUtil.hasValue(list)) {
				return null;
			}
			String billNo = null;
			if (1 == type) {
				billNo = "JS" + counterBalance.getShopNo() + CodingRuleHelper.getBasicCoding(
						BillTypeEnums.COUNTER_BALANCE.getRequestId().toString(), counterBalance.getShopNo(), null);
			}
			if (2 == type) {
				billNo = "JS" + counterBalance.getCompanyNo() + CodingRuleHelper.getBasicCoding(
						BillTypeEnums.COUNTER_BALANCE.getRequestId().toString(), counterBalance.getShopNo(), null);
			}
			counterBalance.setBillNo(billNo);
			counterBalance.setBillType(BillTypeEnums.COUNTER_BALANCE.getRequestId());
			counterBalance.setStatus(StatusEnums.MAKEBILL.getStatus());
			if (!CommonUtil.hasValue(counterBalance.getCounterNo())
					&& !CommonUtil.hasValue(counterBalance.getSupplierNo())) {
				throw new ManagerException("供应商和专柜必须填一个");
			}

			super.insert(counterBalance);

			// 关联到费用单据
			query.and("status", StatusEnums.EFFECTIVE.getStatus());
			List<CounterCost> costList = counterCostService.selectByParams(query);
			for (CounterCost cost : costList) {
				cost.setBalanceBillNo(billNo);
				cost.setStatus(StatusEnums.GENERATE_BALANCE.getStatus());
				counterCostService.update(cost);
			}
			// 关联到销售费用
			List<CounterSaleCost> saleList = counterSaleCostService.selectByParams(query);
			for (CounterSaleCost saleCost : saleList) {
				saleCost.setBalanceBillNo(billNo);
				saleCost.setStatus(StatusEnums.GENERATE_BALANCE.getStatus());
				counterSaleCostService.update(saleCost);
			}
			updateBillBalanceAmount(counterBalance);
			for (ShopBalanceDateDtl shopBalanceDateDtl : list) {
				shopBalanceDateDtl.setStatus(StatusEnums.GENERATE_BALANCE.getStatus());
				CommonStaticManager.getShopBalanceDateDtlManager().update(shopBalanceDateDtl);
			}
		} else {
			super.update(counterBalance);
			// 新增的专柜费用
			List<CounterCost> insertedDtlList = counterBalance.getInsertedDtlList();
			if (CommonUtil.hasValue(insertedDtlList)) {
				for (CounterCost counterCost : insertedDtlList) {
					counterCost.setBalanceBillNo(counterBalance.getBillNo());
					counterCost.setStatus(StatusEnums.GENERATE_BALANCE.getStatus());
					counterCostService.update(counterCost);
				}
			}
			// 删除装柜费用
			List<CounterCost> deletedDtlList = counterBalance.getDeletedDtlList();
			if (CommonUtil.hasValue(deletedDtlList)) {
				for (CounterCost counterCost : deletedDtlList) {
					counterCost.setStatus(StatusEnums.EFFECTIVE.getStatus());
					counterCostService.updateToUnsettled(counterCost.getId());
				}
			}
			updateBillBalanceAmount(counterBalance);
		}
		counterBalance = service.findByUnique(Q.where("billNo", counterBalance.getBillNo()));
		return counterBalance;
	}

	/**
	 * 跟新表头的金额字段
	 * 
	 * @param counterBalance
	 */
	private void updateBillBalanceAmount(BillCounterBalance counter) {
		BillCounterBalance counterBalance = service.findByUnique(Q.where("billNo", counter.getBillNo()));
		Query query = Q.where("balanceBillNo", counterBalance.getBillNo());
		List<CounterCost> list = counterCostService.selectByParams(query);
		List<CounterSaleCost> saleList = counterSaleCostService.selectByParams(query);
		// 计算表头的金额
		// 应结款总额 = 货款汇总.{销售总额-销售提成+分摊金额} - 扣项明细.{账扣标识为账扣的合计}
		BigDecimal ableSum = new BigDecimal(0);
		// 应开票总额:货款汇总.{销售总额-销售提成+分摊金额} - 扣项明细.{票扣标识为票扣并且账扣标识为账扣的合计}
		BigDecimal ableBillingSum = new BigDecimal(0);

		// 提成总额
		BigDecimal profitAmount = new BigDecimal(0);
		// 销售总额
		BigDecimal settleAmount = new BigDecimal(0);
		// 费用总额
		BigDecimal costAmount = new BigDecimal(0);
		for (CounterSaleCost saleCost : saleList) {
			// 销售总额-销售提成+分摊金额
			ableSum = ableSum.add(saleCost.getSettleSum()).add(saleCost.getAbsorptionAmount())
					.subtract(saleCost.getProfitAmount());
			// 销售总额-销售提成+分摊金额
			ableBillingSum = ableBillingSum.add(saleCost.getSettleSum()).add(saleCost.getAbsorptionAmount())
					.subtract(saleCost.getProfitAmount());
			settleAmount = settleAmount.add(saleCost.getSettleSum());
			profitAmount = profitAmount.add(saleCost.getProfitAmount());
		}
		for (CounterCost cost : list) {
			if (cost.getBillDebit() == BillDebitEnums.YES.getValue()
					&& cost.getAccountDebit() == AccountDebitEnums.ACCOUNT.getValue()) {
				ableBillingSum = ableBillingSum.subtract(cost.getAbleSum());
			}
			if (cost.getAccountDebit() == AccountDebitEnums.ACCOUNT.getValue()) {
				ableSum = ableSum.subtract(cost.getAbleSum());
			}
			costAmount = costAmount.add(cost.getAbleSum());
		}
		// 应结款总额:货款汇总页签销售成本+货款汇总页签的分摊金额-扣项明细页签扣项总额汇总
		counterBalance.setAbleSum(ableSum);
		counterBalance.setAbleBillingSum(ableBillingSum);
		counterBalance.setPreAbleSum(counter.getPreAbleSum());
		counterBalance.setNotAbleSum(counterBalance.getAbleSum().subtract(counter.getPreAbleSum()));
		counterBalance.setPreBillingSum(counter.getPreBillingSum());
		counterBalance.setNotBillingSum(counterBalance.getAbleBillingSum().subtract(counter.getPreBillingSum()));
		counterBalance.setSettleAmount(settleAmount);
		counterBalance.setProfitAmount(profitAmount);
		counterBalance.setCostAmount(costAmount);
		super.update(counterBalance);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public CommonResult deleteBill(String billNo) {
		BillCounterBalance counterBalance = service.findByUnique(Q.where("billNo", billNo));
		Query query = Q.where("balanceBillNo", billNo);
		List<CounterCost> list = counterCostService.selectByParams(query);
		for (CounterCost cost : list) {
			counterCostService.updateToUnsettled(cost.getId());
		}
		List<CounterSaleCost> saleList = counterSaleCostService.selectByParams(query);
		for (CounterSaleCost saleCost : saleList) {
			counterSaleCostService.updateToUnsettled(saleCost.getId());
		}
		List<ShopBalanceDateDtl> dtlList = CommonStaticManager.getShopBalanceDateDtlManager()
				.selectByParams(counterBalance.asQuery());
		for (ShopBalanceDateDtl shopBalanceDateDtl : dtlList) {
			shopBalanceDateDtl.setStatus(StatusEnums.GENERATE_COST.getStatus());
			CommonStaticManager.getShopBalanceDateDtlManager().update(shopBalanceDateDtl);
		}
		service.deleteByPrimaryKey(counterBalance.getId());
		return CommonResult.getSucessResult();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public CommonResult verify(String billNo) {
		BillCounterBalance counterBalance = service.findByUnique(Q.where("billNo", billNo));
		counterBalance.setStatus(StatusEnums.verify.getStatus());
		IUser user = Authorization.getUser();
		if (null != user) {
			counterBalance.setAuditor(user.getName());
		}
		counterBalance.setAuditTime(new Date());
		service.update(counterBalance);
		// 把专柜费用单更新为已结算状态
		Query query = Q.where("balanceBillNo", billNo);
		List<CounterCost> list = counterCostService.selectByParams(query);
		for (CounterCost counterCost : list) {
			counterCost.setStatus(StatusEnums.BALANCE.getStatus());
			counterCostService.update(counterCost);
		}
		// 把专柜销售费用单更新为已结算状态
		List<CounterSaleCost> saleList = counterSaleCostService.selectByParams(query);
		for (CounterSaleCost counterSaleCost : saleList) {
			counterSaleCost.setStatus(StatusEnums.BALANCE.getStatus());
			counterSaleCostService.update(counterSaleCost);
		}
		List<ShopBalanceDateDtl> dtlList = CommonStaticManager.getShopBalanceDateDtlManager()
				.selectByParams(counterBalance.asQuery());
		for (ShopBalanceDateDtl shopBalanceDateDtl : dtlList) {
			shopBalanceDateDtl.setStatus(StatusEnums.BALANCE.getStatus());
			CommonStaticManager.getShopBalanceDateDtlManager().update(shopBalanceDateDtl);
		}
		return CommonResult.sucess(counterBalance);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public CommonResult unVerify(String billNo) {
		BillCounterBalance counterBalance = service.findByUnique(Q.where("billNo", billNo));
		counterBalance.setStatus(StatusEnums.MAKEBILL.getStatus());
		service.update(counterBalance);
		// 把专柜费用单更新为已结算状态
		Query query = Q.where("balanceBillNo", billNo);
		List<CounterCost> list = counterCostService.selectByParams(query);
		for (CounterCost counterCost : list) {
			counterCost.setStatus(StatusEnums.GENERATE_BALANCE.getStatus());
			counterCostService.update(counterCost);
		}
		// 把专柜销售费用单更新为已结算状态
		List<CounterSaleCost> saleList = counterSaleCostService.selectByParams(query);
		for (CounterSaleCost counterSaleCost : saleList) {
			counterSaleCost.setStatus(StatusEnums.GENERATE_BALANCE.getStatus());
			counterSaleCostService.update(counterSaleCost);
		}
		List<ShopBalanceDateDtl> dtlList = CommonStaticManager.getShopBalanceDateDtlManager()
				.selectByParams(counterBalance.asQuery());
		for (ShopBalanceDateDtl shopBalanceDateDtl : dtlList) {
			shopBalanceDateDtl.setStatus(StatusEnums.GENERATE_BALANCE.getStatus());
			CommonStaticManager.getShopBalanceDateDtlManager().update(shopBalanceDateDtl);
		}
		return CommonResult.sucess(counterBalance);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public CommonResult batchVerify(String[] billNos) {
		Query query = Query.empty().and("status", StatusEnums.verify.getStatus())
				.and("originStatus", StatusEnums.MAKEBILL.getStatus()).and("keyName", "bill_no")
				.and("keyValue", billNos).and("operate", "auditor").and("operateTime", "audit_time")
				.and("user", Authorization.getUser().getName()).and("time", new Date());

		Integer result = this.service.updateStatus(query);
		if (result != billNos.length) {
			throw new ManagerException("所选单据中存在非制单状态的单据，不能审核。");
		} else {
			// 把专柜费用单更新为已结算状态
			query = Query.empty().and("keyValue", billNos).and("status", StatusEnums.BALANCE.getStatus()).and("keyName",
					"balance_bill_no");
			this.counterCostService.updateStatus(query);

			// 把专柜销售费用单更新为已结算状态
			query = Query.empty().where("balanceBillNos", billNos);
			List<CounterSaleCost> saleList = counterSaleCostService.selectByParams(query);
			for (CounterSaleCost counterSaleCost : saleList) {
				counterSaleCost.setStatus(StatusEnums.BALANCE.getStatus());
				counterSaleCostService.update(counterSaleCost);
			}
			Query q = Q.where("billNos", billNos);

			List<BillCounterBalance> list = service.selectByParams(q);
			for (BillCounterBalance counterBalance : list) {
				List<ShopBalanceDateDtl> dtlList = CommonStaticManager.getShopBalanceDateDtlManager()
						.selectByParams(counterBalance.asQuery());
				for (ShopBalanceDateDtl shopBalanceDateDtl : dtlList) {
					shopBalanceDateDtl.setStatus(StatusEnums.BALANCE.getStatus());
					CommonStaticManager.getShopBalanceDateDtlManager().update(shopBalanceDateDtl);
				}
			}
			return CommonResult.sucess("successful");
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public CommonResult batchCreate(BatchCounterBalanceDto counterBalanceDto) {
		// 先查询出店铺
		Query query = Q.q("companyNo", counterBalanceDto.getCompanyNo()).and("status", 1);
		if (CommonUtil.hasValue(counterBalanceDto.getShopNo())) {
			query.and("shopNo", counterBalanceDto.getShopNo());
		}
		List<Shop> shopList = shopService.selectByParams(query);
		List<Counter> list = new ArrayList<>();
		// 查询专柜信息
		for (Shop shop : shopList) {

			// 需要生成1:"启用",2:"撤柜",3:"待清退"
			Query queryCounter = Q
					.where(Q.And(Q.Equals("shopNo", shop.getShopNo()), Q.In("status", new int[] { 1, 2, 3 })));

			if (CommonUtil.hasValue(counterBalanceDto.getSupplierNo())) {
				queryCounter.and("supplierNo", counterBalanceDto.getSupplierNo());
			}
			if (CommonUtil.hasValue(counterBalanceDto.getCounterNo())) {
				queryCounter.and("counterNo", counterBalanceDto.getCounterNo());
			}
			if (null != counterBalanceDto.getBusinessType()) {
				queryCounter.and("businessType", counterBalanceDto.getBusinessType());
			}

			List<Counter> counterList = counterApiService.selectByParams(queryCounter);
			list.addAll(counterList);
		}
		for (Counter counter : list) {
			Query balanceQuery = Q.where("counterNo", counter.getCounterNo())
					.and("status", StatusEnums.GENERATE_COST.getStatus())
					.and("settleMonth", counterBalanceDto.getSettleMonth()).and("shopNo", counter.getShopNo()).and("companyNo",counterBalanceDto.getCompanyNo());
			List<ShopBalanceDateDtl> balanceDatelist = shopBalanceDateDtlService.selectByParams(balanceQuery);
			for (ShopBalanceDateDtl balanceDateDtl : balanceDatelist) {
				BillCounterBalance counterBalance = new BillCounterBalance();
				counterBalance.setShopNo(balanceDateDtl.getShopNo());
				counterBalance.setCounterNo(balanceDateDtl.getCounterNo());
				counterBalance.setSettleMonth(balanceDateDtl.getSettleMonth());
				counterBalance.setSettleStartDate(balanceDateDtl.getSettleStartDate());
				counterBalance.setSettleEndDate(balanceDateDtl.getSettleEndDate());
				// 1.先生成结算单
				this.save(counterBalance, 1);
			}
		}

		return CommonResult.getSucessResult();
	}

	@Override
	public BillCounterBalance getLastCounterBalanceDate(Query query) {
		return service.getLastCounterBalanceDate(query);
	}

	@Override
	public CounterBalancePrint print(String billNo, Integer templateType) {
		CounterBalancePrint counterBalancePrint = new CounterBalancePrint();

		BillCounterBalance counterBalance = service.findByUnique(Q.where("billNo", billNo));
		if (counterBalance.getStatus() == StatusEnums.MAKEBILL.getStatus()) {
			throw new ManagerException("制单状态不让操作");
		}
		counterBalancePrint.setPrintDate(counterBalance.getCreateTime());
		// 数据准备
		Query query = Q.where("supplierNo", counterBalance.getSupplierNo());
		Supplier supplier = supplierService.findByParam(query);
		Shop shop = shopService.findByUnique(counterBalance.getShopNo());
		Counter counter = null;
		if (CommonUtil.hasValue(counterBalance.getCounterNo())) {
			query.and("counterNo", counterBalance.getCounterNo());
			counter = counterApiService.findByParam(query);
		}
		Company company = companyApiService.findByUnique(counterBalance.getCompanyNo());

		if (null != company) {
			copyCompanyInfo(counterBalancePrint, company);
		}
		if (null != shop) {
			counterBalancePrint.setShopName(shop.getName());
			counterBalancePrint.setZipCode(shop.getZipCode());
			counterBalancePrint.setZoneNo(shop.getZoneNo());
			counterBalancePrint.setShopNo(shop.getShopNo());
		}
		if (counter != null) {
			counterBalancePrint.setCounterNo(counterBalance.getCounterNo());
			counterBalancePrint.setCounterName(counter.getName());
		}
		if (null != supplier) {
			copySupplierInfo(counterBalancePrint, supplier, templateType);
		}
		handlePrintVo(counterBalance, counterBalancePrint, templateType);
		return counterBalancePrint;
	}

	/**
	 * 把公司的属性赋值给打印
	 * 
	 * @param counterBalancePrint
	 * @param company
	 */
	private void copyCompanyInfo(CounterBalancePrint counterBalancePrint, Company company) {
		counterBalancePrint.setBankAccount(company.getBankAccount());
		counterBalancePrint.setBankName(company.getBankName());
		counterBalancePrint.setTaxNo(company.getTaxRegistryNo());
		counterBalancePrint.setAddress(company.getTaxAddress());
		counterBalancePrint.setCompanyName(company.getName());
		counterBalancePrint.setTel(company.getTicketTel());
		counterBalancePrint.setCreateUser(company.getTicketName());
		counterBalancePrint.setRemark1(company.getTicRemark());
		counterBalancePrint.setRemark2(company.getTicketRemark());
	}

	/**
	 * 把供应商的属性赋值给打印
	 * 
	 * @param counterBalancePrint
	 * @param supplier
	 */
	private void copySupplierInfo(CounterBalancePrint counterBalancePrint, Supplier supplier, int templateType) {
		counterBalancePrint.setSupplierName(supplier.getName());
		counterBalancePrint.setSupplierNo(supplier.getSupplierNo());
		if (2 != templateType) {
			// 表示供应商模版
			Query query = Q.where("supplierNo", supplier.getSupplierNo()).and("shopNo", counterBalancePrint.getShopNo())
					.and("counterNo", counterBalancePrint.getCounterNo()).and("status", StatusEnums.ENABLE.getStatus());
			SupplierAccount supplierAccount = supplierAccountService.findByParam(query);
			if (null != supplierAccount) {
				counterBalancePrint.setSuAddress(supplierAccount.getAddress());
				counterBalancePrint.setSuBankAccount(supplierAccount.getBankAccount());
				counterBalancePrint.setSuBankName(supplierAccount.getBankName());
				counterBalancePrint.setSuTaxNo(supplierAccount.getTaxNo());
				counterBalancePrint.setSuCompanyName(supplierAccount.getSuCompanyName());
				counterBalancePrint.setSubBankAccountName(supplierAccount.getBankAccountName());
			}
		} else {
			counterBalancePrint.setSuAddress(supplier.getAddress());
			counterBalancePrint.setSuBankAccount(supplier.getBankAccount());
			counterBalancePrint.setSuBankName(supplier.getBankName());
			counterBalancePrint.setSuTaxNo(supplier.getTaxNo());
			counterBalancePrint.setSuCompanyName(supplier.getName());
		}

	}

	/**
	 * 处理打印实体，组装必要信息
	 * 
	 * @param counterBalance
	 *            结算单
	 * @param counterBalancePrint
	 *            结算单打印实体
	 * @param templateType
	 *            模板类型
	 */
	private void handlePrintVo(BillCounterBalance counterBalance, CounterBalancePrint counterBalancePrint,
			Integer templateType) {
		// 横排的处理方式
		String billNo = counterBalance.getBillNo();
		Query query2 = Q.where("balanceBillNo", billNo);
		List<CounterCost> costList = counterCostService.selectByParams(query2);
		//division_no,counter_no,shop_no,rax_rate 分组
		if(0!=templateType){
			query2.and("templateType", templateType);
		}
		List<CounterSaleCost> saleCostList = counterSaleCostService.queryListGroupDivisionNo(query2);
		List<CounterCost> counterCostList = new ArrayList<>();
		List<CounterCost> depositList = new ArrayList<>();
		for (CounterCost counterCost : costList) {
			Query q = Q.where("deductionNo", counterCost.getCostNo());
			Deduction deduction = deductionApiService.findByParam(q);
			if (null != deduction) {
				counterCost.setCostName(deduction.getName());
			}
		}
		// 计算销售总量
		Integer saleQty = 0;
		BigDecimal saleAmount = new BigDecimal(0);
		for (CounterSaleCost saleCost : saleCostList) {
			saleQty = saleQty + saleCost.getSaleQty();
			saleAmount = saleAmount.add(saleCost.getSettleSum());
		}

		counterBalancePrint.setSaleAmount(saleAmount);
		counterBalancePrint.setSaleQty(saleQty);
		if (CommonUtil.hasValue(saleCostList)) {
			for (CounterSaleCost saleCost : saleCostList) {
				if (saleCost.getDivisionNo().endsWith("00")) {
					counterBalancePrint.setRaxRate(saleCost.getRaxRate());
					break;
				}
			}
			if (counterBalancePrint.getRaxRate() == null) {
				counterBalancePrint.setRaxRate(saleCostList.get(0).getRaxRate());
			}
		}
		if (templateType == 0) {
			for (CounterCost counterCost : costList) {
				// 如果是 抽成、保底、租金,记成本 的扣项 需要放入CounterSaleCostList中显示
				if (1 == counterCost.getRefType() || 2 == counterCost.getRefType() || 3 == counterCost.getRefType()
						|| 5 == counterCost.getRefType()) {
					CounterSaleCost counterSaleCost = convertObj(counterCost);
					saleCostList.add(counterSaleCost);
				} else {
					if (counterCost.getTaxRate() != null && counterCost.getTaxRate().doubleValue() > 0.00) {
						// （二，费用结算部分）费用项目税率不为0
						counterCostList.add(counterCost);
					} else {
						// （三、其他部分）费用项目税率为0
						depositList.add(counterCost);
					}
				}
			}

		} else if (templateType == 1) {
			for (CounterCost counterCost : costList) {

				// 如果是 抽成、保底、租金,记成本 的扣项 需要放入CounterSaleCostList中显示
				if (1 == counterCost.getRefType() || 2 == counterCost.getRefType() || 3 == counterCost.getRefType()
						|| 5 == counterCost.getRefType()) {
					CounterSaleCost counterSaleCost = convertObj(counterCost);
					saleCostList.add(counterSaleCost);
				} else {
					// 修改为 根据扣项明细账扣标识来标示，账扣显示在费用扣款明细，现金显示在其他部分
					if (counterCost.getAccountDebit() == 1) {
						// （费用扣款明细）扣项标识为账扣
						counterCostList.add(counterCost);
					} else if (counterCost.getAccountDebit() == 2) {
						// 其他部分）扣项标识为现金
						depositList.add(counterCost);
					}
				}
			}
		} else if (templateType == 2) {
			Map<String, Object> params = new HashMap<>();
			for (CounterCost counterCost : costList) {
				if (null == params.get(counterCost.getCounterNo())) {
					Counter counter = counterApiService.findByParam(Q.where("counterNo", counterCost.getCounterNo()));
					if (null != counter) {
						counterCost.setCounterName(counter.getName());
						params.put(counter.getCounterNo(), counter.getName());
					}
				} else {
					counterCost.setCounterName(params.get(counterCost.getCounterNo()).toString());
				}
				if (null == params.get(counterCost.getShopNo())) {
					Shop shop = shopService.findByParam(Q.where("shopNo",counterCost.getShopNo()));
					if(null!=shop){
						counterCost.setShopName(shop.getName());
						params.put(shop.getShopNo(), shop.getName());
					}
				}else{
					counterCost.setShopName(params.get(counterCost.getShopNo()).toString());
				}
				counterCostList.add(counterCost);
			}
			for (CounterSaleCost counterSaleCost : saleCostList) {
				if (null == params.get(counterSaleCost.getCounterNo())) {
					Counter counter = counterApiService
							.findByParam(Q.where("counterNo", counterSaleCost.getCounterNo()));
					if (null != counter) {
						counterSaleCost.setCounterName(counter.getName());
						params.put(counter.getCounterNo(), counter.getName());
					}
				} else {
					counterSaleCost.setCounterName(params.get(counterSaleCost.getCounterNo()).toString());
				}
				if (null == params.get(counterSaleCost.getShopNo())) {
					Shop shop = shopService.findByParam(Q.where("shopNo",counterSaleCost.getShopNo()));
					if(null!=shop){
						counterSaleCost.setShopName(shop.getName());
						params.put(shop.getShopNo(), shop.getName());
					}
				}else{
					counterSaleCost.setShopName(params.get(counterSaleCost.getShopNo()).toString());
				}
			}
		}
		counterBalancePrint.setCounterCostList(counterCostList);// 扣项集合
		counterBalancePrint.setDepositList(depositList);// 其他部分集合
		counterBalancePrint.setCounterSaleCostList(saleCostList);// 销售扣费集合
		counterBalancePrint.setSettleStartDate(counterBalance.getSettleStartDate());
		counterBalancePrint.setSettleEndDate(counterBalance.getSettleEndDate());
		counterBalancePrint.setSettleMonth(counterBalance.getSettleMonth());
		counterBalancePrint.setCounterNo(counterBalance.getCounterNo());
		counterBalancePrint.setBillNo(billNo);
		counterBalancePrint.setBusinessType(counterBalance.getBusinessType());
		counterBalancePrint.setAbleSum(counterBalance.getAbleSum());
		counterBalancePrint.setAbleBillingSum(counterBalance.getAbleBillingSum());
	}

	@Override
	public List<CounterBalancePrint> batchPrint(Query query, Integer templateType) {
		List<CounterBalancePrint> balancePrints = new ArrayList<>();
		List<BillCounterBalance> counterBalances = service.selectByParams(query);

		Map<String, List<Supplier>> supplierMap = new HashMap<>();
		Map<String, List<Shop>> shopMap = new HashMap<>();
		Map<String, List<Counter>> counterMap = new HashMap<>();
		Map<String, List<Company>> compnayMap = new HashMap<>();

		Set<String> supplierNoSet = new HashSet<>();
		Set<String> shopNoSet = new HashSet<>();
		Set<String> counterNoSet = new HashSet<>();
		Set<String> companyNoSet = new HashSet<>();

		for (BillCounterBalance balance : counterBalances) {
			if (balance.getStatus() == StatusEnums.MAKEBILL.getStatus()) {
				throw new ManagerException("制单状态不让操作");
			}
			supplierNoSet.add(balance.getSupplierNo());
			shopNoSet.add(balance.getShopNo());
			counterNoSet.add(balance.getCounterNo());
			companyNoSet.add(balance.getCompanyNo());
		}
		String[] supplierNoArray = new String[supplierNoSet.size()];
		supplierNoSet.toArray(supplierNoArray);

		String[] shopNoArray = new String[shopNoSet.size()];
		shopNoSet.toArray(shopNoArray);

		String[] counterNoArray = new String[counterNoSet.size()];
		counterNoSet.toArray(counterNoArray);

		String[] companyNoArray = new String[companyNoSet.size()];
		companyNoSet.toArray(companyNoArray);

		// 数据准备 批量查询
		List<Supplier> suppliers = supplierService.selectByParams(Q.where(Q.In("supplier_no", supplierNoArray)));
		List<Company> companys = companyApiService.selectByParams(Q.where(Q.In("company_no", companyNoArray)));
		supplierMap = suppliers.stream().collect(Collectors.groupingBy(Supplier::getSupplierNo));
		compnayMap = companys.stream().collect(Collectors.groupingBy(Company::getCompanyNo));
		List<Shop> shops = null;
		List<Counter> counters = null;
		if (templateType != 2) {
			shops = shopService.selectByParams(Q.where(Q.In("shop_no", shopNoArray)));
			counters = counterApiService.selectByParams(Q.where(Q.In("counter_no", counterNoArray)));
			shopMap = shops.stream().collect(Collectors.groupingBy(Shop::getShopNo));
			counterMap=counters.stream().collect(Collectors.groupingBy(Counter::getCounterNo));
		}

		for (BillCounterBalance balance : counterBalances) {
			CounterBalancePrint counterBalancePrint = new CounterBalancePrint();
			counterBalancePrint.setPrintDate(balance.getCreateTime());
			counterBalancePrint.setShopNo(balance.getShopNo());
			counterBalancePrint.setCounterNo(balance.getCounterNo());
			List<Supplier> curSupplier = supplierMap.get(balance.getSupplierNo());
			if (null != curSupplier && !curSupplier.isEmpty()) {
				copySupplierInfo(counterBalancePrint, curSupplier.get(0), templateType);
			}

			List<Company> curCompanies = compnayMap.get(balance.getCompanyNo());
			if (null != curCompanies && !curCompanies.isEmpty()) {
				copyCompanyInfo(counterBalancePrint, curCompanies.get(0));
			}
			if (templateType != 2) {
				List<Shop> curShops = shopMap.get(balance.getShopNo());
				if (null != curShops && !curShops.isEmpty()) {
					counterBalancePrint.setShopName(curShops.get(0).getName());
					counterBalancePrint.setZipCode(curShops.get(0).getZipCode());
					counterBalancePrint.setZoneNo(curShops.get(0).getZoneNo());
				}
				// 如果按照供应商维度,专柜有可能没有
				if (CommonUtil.hasValue(balance.getCounterNo())) {
					List<Counter> curCounters = counterMap.get(balance.getCounterNo());
					if (curCounters != null && !curCounters.isEmpty()) {
						counterBalancePrint.setCounterName(curCounters.get(0).getName());
					}
				}
			}
			// 处理打印实体，组装必要信息
			handlePrintVo(balance, counterBalancePrint, templateType);

			balancePrints.add(counterBalancePrint);
		}
		return balancePrints;
	}

	/**
	 * 将CounterCost转换成CounterSaleCost对象
	 */
	private CounterSaleCost convertObj(CounterCost counterCost) {
		CounterSaleCost counterSaleCost = new CounterSaleCost();
		BeanUtils.copyProperties(counterCost, counterSaleCost);
		counterSaleCost.setDivisionName(counterCost.getCostName());
		counterSaleCost.setSettleSum(null);
		counterSaleCost.setRateValue(null);
		counterSaleCost.setProfitAmount(counterCost.getAbleSum());// 放入计算项目中是需要加上税费的
		return counterSaleCost;
	}

	/**
	 * @see topmall.fas.manager.IBillCounterBalanceManager#selectByPageTotal(cn.mercury.basic.query.Query,
	 *      cn.mercury.basic.query.Pagenation)
	 */
	@Override
	public List<BillCounterBalance> selectByPageTotal(Query query, Pagenation page) {
		return service.selectByPageTotal(query, page);
	}

	@Override
	public void printCount(String billNo) {
		BillCounterBalance counterBalance = service.findByUnique(Q.where("billNo", billNo));
		counterBalance.setPrintCount(counterBalance.getPrintCount() + 1);
		service.update(counterBalance);
	}
}
