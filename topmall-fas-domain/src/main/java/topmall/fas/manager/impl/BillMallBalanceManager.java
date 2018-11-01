package topmall.fas.manager.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.dubbo.config.annotation.Reference;
import cn.mercury.basic.UUID;
import cn.mercury.basic.query.Q;
import cn.mercury.basic.query.Query;
import cn.mercury.manager.ManagerException;
import cn.mercury.security.IUser;
import tomall.contract.api.IConContractApi;
import topmall.common.enums.BillTypeEnums;
import topmall.contract.model.ConMallContract;
import topmall.fas.dto.ContractMainDto;
import topmall.fas.dto.MallBalanceSummary;
import topmall.fas.enums.AccountDebitEnums;
import topmall.fas.enums.PaidWayEnums;
import topmall.fas.enums.StatusEnums;
import topmall.fas.manager.IBillMallBalanceManager;
import topmall.fas.model.BillMallBalance;
import topmall.fas.model.MallBalanceDate;
import topmall.fas.model.MallBalanceDateDtl;
import topmall.fas.model.MallCost;
import topmall.fas.model.MallMinimumData;
import topmall.fas.model.MallPay;
import topmall.fas.model.MallPrepayDtl;
import topmall.fas.model.MallSaleCost;
import topmall.fas.service.IBillMallBalanceService;
import topmall.fas.service.IMallBalanceDateDtlService;
import topmall.fas.service.IMallBalanceDateService;
import topmall.fas.service.IMallCostService;
import topmall.fas.service.IMallMinimumDataService;
import topmall.fas.service.IMallPayService;
import topmall.fas.service.IMallPrepayDtlService;
import topmall.fas.service.IMallSaleCostService;
import topmall.fas.util.CommonResult;
import topmall.fas.util.CommonUtil;
import topmall.framework.core.CodingRuleHelper;
import topmall.framework.manager.BaseManager;
import topmall.framework.security.Authorization;
import topmall.framework.service.IService;

@Service
public class BillMallBalanceManager extends BaseManager<BillMallBalance, String> implements IBillMallBalanceManager {
	@Autowired
	private IBillMallBalanceService service;
	@Autowired
	private IMallCostService mallCostService;
	@Autowired
	private IMallSaleCostService mallSaleCostService;
	@Autowired
	private IMallPayService mallPayService;
	@Reference
	private IConContractApi conContractApi;
	@Autowired
	private IMallBalanceDateDtlService mallBalanceDateDtlService;
	@Autowired
	private IMallBalanceDateService mallBalanceDateService;
	@Autowired
	private IMallPrepayDtlService mallPrepayDtlService;
	@Autowired
	private IMallMinimumDataService minimumDataService;
	

	protected IService<BillMallBalance, String> getService() {
		return service;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public BillMallBalance save(BillMallBalance mallBalance) {
		if (StringUtils.isEmpty(mallBalance.getBillNo())) {
			ConMallContract conMall = conContractApi.queryMallContract(mallBalance.getShopNo(), mallBalance.getMallNo(),mallBalance.getBunkGroupNo());
			if(null!=conMall){
				mallBalance.setAreaCompact(conMall.getarea());
				mallBalance.setBusinessType(conMall.getBusinessType());
			}
			Query baseQuery=mallBalance.asQuery().and("status", StatusEnums.GENERATE_COST.getStatus());
			List<MallBalanceDateDtl> list=mallBalanceDateDtlService.selectByParams(baseQuery);
			if (!CommonUtil.hasValue(list)) {
				return null;
			}
			String billNo = mallBalance.getShopNo() + CodingRuleHelper.getBasicCoding(
					BillTypeEnums.MALL_BALANCE.getRequestId().toString(), mallBalance.getShopNo(), null);
			mallBalance.setBillNo(billNo);
			mallBalance.setBillType(BillTypeEnums.MALL_BALANCE.getRequestId());
			mallBalance.setStatus(StatusEnums.MAKEBILL.getStatus());
			super.insert(mallBalance);
			// 关联到费用单据
			// 这里用的是结算期结束时间之前且没有完成的
			Query query = Q.And(Q.Equals("shopNo", mallBalance.getShopNo()),
					Q.LessThenEquals("settleEndDate", mallBalance.getSettleEndDate())).asQuery();
			query.and("settleStatus", StatusEnums.MAKEBILL.getStatus()).and("status", StatusEnums.EFFECTIVE.getStatus())
					.and("mallNo", mallBalance.getMallNo()).and("bunkGroupNo", mallBalance.getBunkGroupNo());
			List<MallCost> costList = mallCostService.selectByParams(query);
			for (MallCost cost : costList) {
				cost.setBalanceBillNo(billNo);
				cost.setStatus(StatusEnums.GENERATE_BALANCE.getStatus());
				mallCostService.update(cost);
			}
			// 关联到销售费用
			// 这里用的是结算期结束时间之前且没有完成的
			List<MallSaleCost> saleList = mallSaleCostService.selectByParams(query);
			for (MallSaleCost saleCost : saleList) {
				saleCost.setBalanceBillNo(billNo);
				saleCost.setStatus(StatusEnums.GENERATE_BALANCE.getStatus());
				mallSaleCostService.update(saleCost);
			}
			Query quer = Q.where("shopNo", mallBalance.getShopNo()).and("status", StatusEnums.EFFECTIVE.getStatus())
					.and("mallNo", mallBalance.getMallNo()).and("settleMonth", mallBalance.getSettleMonth()).and("bunkGroupNo", mallBalance.getBunkGroupNo());
			List<MallPay> payList = mallPayService.selectByParams(quer);
			for (MallPay mallPay : payList) {
				mallPay.setBalanceBillNo(billNo);
				mallPayService.update(mallPay);
			}
			for(MallBalanceDateDtl mallBalanceDateDtl:list){
				mallBalanceDateDtl.setStatus(StatusEnums.GENERATE_BALANCE.getStatus());
				mallBalanceDateDtlService.update(mallBalanceDateDtl);
			}
		} else {
			super.update(mallBalance);
			MallBalanceDateDtl balanceDateDtl = mallBalanceDateDtlService.findByParam(mallBalance.asQuery());
			// 新增的物业费用
			List<MallCost> inserteDtlList = mallBalance.getInsertMallCostList();
			if (CommonUtil.hasValue(inserteDtlList)) {
				for (MallCost mallCost : inserteDtlList) {
					mallCost.setDiffAmount(mallCost.getAbleSum().subtract(mallCost.getMallAmount()));
					mallCost.setBalanceBillNo(mallBalance.getBillNo());
					mallCost.setStatus(StatusEnums.GENERATE_BALANCE.getStatus());
					mallCost.setCurDiffAmount(mallCost.getDiffAmount().subtract(mallCost.getAdjustAmount()).subtract(mallCost.getBackAmount()));
					mallCostService.update(mallCost);
				}
			}
			// 删除物业费用
			List<MallCost> deleteDtlList = mallBalance.getDeleteMallCostList();
			if (CommonUtil.hasValue(deleteDtlList)) {
				for (MallCost mallCost : deleteDtlList) {
					mallCost.setStatus(StatusEnums.EFFECTIVE.getStatus());
					mallCost.setSettleStatus(StatusEnums.MAKEBILL.getStatus());
					mallCostService.updateToUnsettled(mallCost.getId());
				}
			}
			// 更新物业费用
			List<MallCost> updateDtlList = mallBalance.getUpdateMallCostList();
			for (MallCost mallCost : updateDtlList) {
				mallCost.setDiffAmount(mallCost.getAbleSum().subtract(mallCost.getMallAmount()));
				mallCost.setCurDiffAmount(mallCost.getDiffAmount().subtract(mallCost.getAdjustAmount())
						.subtract(mallCost.getBackAmount()));
				mallCostService.update(mallCost);
			}
			List<MallSaleCost> updateSaleCostList = mallBalance.getUpdateMallSaleCostList();

			for (MallSaleCost mallSaleCost : updateSaleCostList) {
				if(balanceDateDtl.getPointsCalculateFlag()==0){
					mallSaleCost.setDiffAmount(mallSaleCost.getDeductPointsAmount().subtract(mallSaleCost.getMallAmount()));
				}else{
					mallSaleCost.setDiffAmount(mallSaleCost.getSettleSum().subtract(mallSaleCost.getMallAmount()));
				}
				mallSaleCost.setCurDiffAmount(mallSaleCost.getDiffAmount().subtract(mallSaleCost.getAdjustAmount())
						.subtract(mallSaleCost.getBackAmount()));
				mallSaleCost.setDiffProfitAmount(mallSaleCost.getProfitAmount().subtract(mallSaleCost.getMallProfitAmount()));
				mallSaleCostService.update(mallSaleCost);
			}
			List<MallPay> updateMappPayList = mallBalance.getUpdateMallPayList();
			for (MallPay mallPay : updateMappPayList) {
				mallPay.setDiffPayAmount(mallPay.getPayAmount().subtract(mallPay.getMallPayAmount()));
				mallPayService.update(mallPay);
			}
		}
		
		mallBalance = service.findByUnique(Q.where("billNo", mallBalance.getBillNo()));
		return mallBalance;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public CommonResult deleteBill(String billNo) {
		BillMallBalance mallBalance = service.findByUnique(Q.where("billNo", billNo));
		// 解除结算单与专柜费用之间的关系
		Query query = Q.where("balanceBillNo", billNo);
		List<MallCost> list = mallCostService.selectByParams(query);
		for (MallCost cost : list) {
			mallCostService.updateToUnsettled(cost.getId());
		}
		// 解除结算单与专柜销售费用之间的关系
		List<MallSaleCost> saleList = mallSaleCostService.selectByParams(query);
		for (MallSaleCost saleCost : saleList) {
			mallSaleCostService.updateToUnsettled(saleCost.getId());
		}
		// 解除结算单与支付信息之间的关系
		List<MallPay> payList = mallPayService.selectByParams(query);
		for (MallPay mallPay : payList) {
			mallPayService.updateToUnsettled(mallPay.getId());
		}
		service.deleteByPrimaryKey(mallBalance.getId());
		List<MallBalanceDateDtl> dtlList = mallBalanceDateDtlService.selectByParams(mallBalance.asQuery());
		for(MallBalanceDateDtl mallBalanceDateDtl:dtlList){
			mallBalanceDateDtl.setStatus(StatusEnums.GENERATE_COST.getStatus());
			mallBalanceDateDtlService.update(mallBalanceDateDtl);
		}
		return CommonResult.getSucessResult();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public CommonResult verify(String billNo) {
		BillMallBalance mallBalance = service.findByUnique(Q.where("billNo", billNo));
		mallBalance.setStatus(StatusEnums.verify.getStatus());
		IUser user = Authorization.getUser();
		if(null!=user){
			mallBalance.setAuditor(user.getName());
		}
		mallBalance.setAuditTime(new Date());
		service.update(mallBalance);
		// 把专柜费用单更新为已结算状态
		Query query = Q.where("balanceBillNo", billNo);
		List<MallCost> list = mallCostService.selectByParams(query);
		for (MallCost mallCost : list) {
			mallCost.setStatus(StatusEnums.BALANCE.getStatus());
			mallCostService.update(mallCost);
		}
		// 把专柜销售费用单更新为已结算状态
		List<MallSaleCost> saleList = mallSaleCostService.selectByParams(query);
		for (MallSaleCost mallSaleCost : saleList) {
			mallSaleCost.setStatus(StatusEnums.BALANCE.getStatus());
			mallSaleCostService.update(mallSaleCost);
		}
		MallBalanceDateDtl dtl = mallBalanceDateDtlService.findByParam(mallBalance.asQuery().and("status", StatusEnums.GENERATE_BALANCE.getStatus()));
		dtl.setStatus(StatusEnums.BALANCE.getStatus());
		mallBalanceDateDtlService.update(dtl);
		MallMinimumData mallMinimum = new MallMinimumData();
		dtl.copyProperties(mallMinimum);
		mallMinimum.setAreaCompact(mallBalance.getAreaCompact());
		mallMinimum.setBusinessType(mallBalance.getBusinessType());
		mallMinimum.setMallMinimumAmount(mallBalance.getMallBillingAmount());
		mallMinimum.setId(UUID.newUUID().toString());
		minimumDataService.insertOrUpdate(mallMinimum);
		return CommonResult.sucess(mallBalance);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public CommonResult unVerify(String billNo) {
		BillMallBalance mallBalance = service.findByUnique(Q.where("billNo", billNo));
		mallBalance.setStatus(StatusEnums.MAKEBILL.getStatus());
		service.update(mallBalance);
		// 把专柜费用单更新为已结算状态
		Query query = Q.where("balanceBillNo", billNo);
		List<MallCost> list = mallCostService.selectByParams(query);
		for (MallCost mallCost : list) {
			mallCost.setStatus(StatusEnums.GENERATE_BALANCE.getStatus());
			mallCostService.update(mallCost);
		}
		// 把专柜销售费用单更新为已结算状态
		List<MallSaleCost> saleList = mallSaleCostService.selectByParams(query);
		for (MallSaleCost mallSaleCost : saleList) {
			mallSaleCost.setStatus(StatusEnums.GENERATE_BALANCE.getStatus());
			mallSaleCostService.update(mallSaleCost);
		}
		List<MallBalanceDateDtl> dtlList = mallBalanceDateDtlService.selectByParams(mallBalance.asQuery().and("status", StatusEnums.BALANCE.getStatus()));
		for(MallBalanceDateDtl mallBalanceDateDtl:dtlList){
			mallBalanceDateDtl.setStatus(StatusEnums.GENERATE_BALANCE.getStatus());
			mallBalanceDateDtlService.update(mallBalanceDateDtl);
		}
		return CommonResult.sucess("successful");
	}

	@Override
	public List<MallBalanceSummary> getMallBalanceSummary(String billNo) {
		BillMallBalance mallBalance = service.findByUnique(Q.where("billNo", billNo));
		Query dateQuery = Q.where("shopNo", mallBalance.getShopNo()).and("mallNo", mallBalance.getMallNo()).and("bunkGroupNo", mallBalance.getBunkGroupNo());
		MallBalanceDate mallBalanceDate = mallBalanceDateService.findByParam(dateQuery);
		
		List<MallBalanceSummary> list = new ArrayList<>();
		// 组装支付信息
		Query query = Q.where("balanceBillNo", billNo);
		List<MallPay> payList = mallPayService.getMallPayGroupPaidWay(query);
		//开票金额
		BigDecimal billingTotal = new BigDecimal(0);
		
		BigDecimal payAmountTotal = new BigDecimal(0);
		for (MallPay mallPay : payList) {
			MallBalanceSummary mallBalanceSummary = new MallBalanceSummary();
			mallBalanceSummary.setCurAmount(mallPay.getPayAmount());
			mallBalanceSummary.setMallAmount(mallPay.getMallPayAmount());
			mallBalanceSummary.setDiffAmount(mallPay.getDiffPayAmount());
			mallBalanceSummary.setDetail(PaidWayEnums.getTextByStatus(mallPay.getPaidWay()));
			mallBalanceSummary.setProjectName("销售");
			payAmountTotal = payAmountTotal.add(mallPay.getPayAmount());
			list.add(mallBalanceSummary);
		}
		// 如果不为零,则添加一下合计
		if (payAmountTotal.compareTo(new BigDecimal(0)) != 0) {
			MallBalanceSummary mallBalanceSummary = new MallBalanceSummary();
			mallBalanceSummary.setDetail("合计");
			mallBalanceSummary.setCurAmount(payAmountTotal);
			list.add(mallBalanceSummary);
		}
		// 组装促销计入保底数据
		query.and("shopNo", mallBalance.getShopNo()).and("mallNo", mallBalance.getMallNo())
				.and("settleStartDate", mallBalance.getSettleStartDate())
				.and("settleEndDate", mallBalance.getSettleEndDate()).and("bunkGroupNo", mallBalance.getBunkGroupNo());
		MallBalanceDateDtl balanceDateDtl = mallBalanceDateDtlService.findByParam(mallBalance.asQuery());
		if(balanceDateDtl.getPointsCalculateFlag()==0){
			query.and("pointsCalculateFlag", true);
		}
		BigDecimal proAmount = service.proMinimumSum(query);
		if (null != proAmount) {
			MallBalanceSummary mallBalanceSummary = new MallBalanceSummary();
			mallBalanceSummary.setDetail("促销计入保底");
			mallBalanceSummary.setCurAmount(proAmount);
			mallBalanceSummary.setProjectName("保底");
			list.add(mallBalanceSummary);
		}
		ContractMainDto  contractMain=  mallBalanceDateDtlService.selectContractBillNo(balanceDateDtl);
		if(null!=contractMain){
			query.and("conBillNo", contractMain.getBillNo());
			// 组装年保底金额
			BigDecimal yearGuaraAmount = service.yearGuaraSum(query);
			if (null != yearGuaraAmount) {
				MallBalanceSummary mallBalanceSummary = new MallBalanceSummary();
				mallBalanceSummary.setProjectName("保底");
				mallBalanceSummary.setDetail("年保底金额");
				mallBalanceSummary.setCurAmount(yearGuaraAmount);
				list.add(mallBalanceSummary);
			}
		}
		// 组装销售费用
		List<MallCost> costList = mallCostService.getCostGroupAccountDebit(query);
		BigDecimal curTotal= new BigDecimal(0);
		BigDecimal difTotal= new BigDecimal(0);
		BigDecimal mallTotal= new BigDecimal(0);
		for(MallCost cost :costList){
			MallBalanceSummary mallBalanceSummary = new MallBalanceSummary();
			mallBalanceSummary.setProjectName("费用");
			mallBalanceSummary.setDetail(AccountDebitEnums.getTextByStatus(cost.getAccountDebit()));
			mallBalanceSummary.setCurAmount(cost.getAbleSum());
			mallBalanceSummary.setMallAmount(cost.getMallAmount());
			mallBalanceSummary.setDiffAmount(cost.getAbleSum().subtract(cost.getMallAmount()));
			curTotal =curTotal.add(mallBalanceSummary.getCurAmount());
			difTotal=difTotal.add(mallBalanceSummary.getDiffAmount());
			mallTotal=mallTotal.add(mallBalanceSummary.getMallAmount());
			list.add(mallBalanceSummary);
		}
		List<MallPrepayDtl>  preList = mallPrepayDtlService.queryByMallBalance(dateQuery.and("settleMonth", mallBalance.getSettleMonth()));
		
		//1、同意抵扣：应收物业方=物业方收银-账扣费用(销售费用和物业费用)-预付(物业预付款)+现金(销售费用和物业费用); 应付物业方款项为0
		//2.不同意抵扣：应收物业方=物业方收银-费用为账扣费用(销售费用和物业费用); 应付物业方=0-预付(物业预付款)+费用为付现(销售费用和物业费用)
		BigDecimal receiveMallTotal = new BigDecimal(0);
		BigDecimal payMallTotal = new BigDecimal(0);
		
		BigDecimal mallReceiveMallTotal = new BigDecimal(0);
		BigDecimal mallPayMallTotal = new BigDecimal(0);
		if(0==mallBalanceDate.getBalanceType()){
			//同意抵扣
			for(MallPay mallPay: payList){
				if(mallPay.getPaidWay()==PaidWayEnums.Mall_CASHIER.getValue()){
					receiveMallTotal = receiveMallTotal.add(mallPay.getPayAmount());
					mallReceiveMallTotal=mallReceiveMallTotal.add(mallPay.getMallPayAmount());
				}
			}
			for(MallCost mallCost:costList){
				if(mallCost.getAccountDebit()==AccountDebitEnums.ACCOUNT.getValue()){
					receiveMallTotal=receiveMallTotal.subtract(mallCost.getAbleSum());
					mallReceiveMallTotal=mallReceiveMallTotal.subtract(mallCost.getMallAmount());
				}else{
					receiveMallTotal=receiveMallTotal.add(mallCost.getAbleSum());
					mallReceiveMallTotal= mallReceiveMallTotal.add(mallCost.getMallAmount());
				}
			}
			for(MallPrepayDtl dtl :preList){
				receiveMallTotal=receiveMallTotal.subtract(dtl.getPrepayAmount());
				mallReceiveMallTotal=mallReceiveMallTotal.subtract(dtl.getPrepayAmount());
			}
		}
		if(1==mallBalanceDate.getBalanceType()){
			//不同意抵扣
			for(MallPay mallPay: payList){
				if(mallPay.getPaidWay()==PaidWayEnums.Mall_CASHIER.getValue()){
					receiveMallTotal = receiveMallTotal.add(mallPay.getPayAmount());
					mallReceiveMallTotal = mallReceiveMallTotal.add(mallPay.getMallPayAmount());
				}
			}
			for(MallCost mallCost:costList){
				if(mallCost.getAccountDebit()==AccountDebitEnums.ACCOUNT.getValue()){
					receiveMallTotal=receiveMallTotal.subtract(mallCost.getAbleSum());
					mallReceiveMallTotal = mallReceiveMallTotal.subtract(mallCost.getMallAmount());
				}else{
					payMallTotal=payMallTotal.add(mallCost.getAbleSum());
					mallPayMallTotal=mallPayMallTotal.add(mallCost.getMallAmount());
				}
			}
			for(MallPrepayDtl dtl :preList){
				payMallTotal=payMallTotal.subtract(dtl.getPrepayAmount());
				mallPayMallTotal= mallPayMallTotal.subtract(dtl.getPrepayAmount());
			}
		}
		MallBalanceSummary mallBalanceSummary = new MallBalanceSummary();
		mallBalanceSummary.setDetail("合计");
		mallBalanceSummary.setCurAmount(curTotal);
		mallBalanceSummary.setDiffAmount(difTotal);
		mallBalanceSummary.setMallAmount(mallTotal);
		list.add(mallBalanceSummary);
		MallBalanceSummary receiveMall = new MallBalanceSummary();
		receiveMall.setDetail("应收物业方款项");
		receiveMall.setProjectName("付款");
		receiveMall.setCurAmount(receiveMallTotal);
		receiveMall.setMallAmount(mallReceiveMallTotal);
		receiveMall.setDiffAmount(receiveMallTotal.subtract(mallReceiveMallTotal));
		list.add(receiveMall);
		
		MallBalanceSummary payMall = new MallBalanceSummary();
		payMall.setDetail("应付物业方款项");
		payMall.setCurAmount(payMallTotal);
		payMall.setMallAmount(mallPayMallTotal);
		payMall.setDiffAmount(payMallTotal.subtract(mallPayMallTotal));
		payMall.setProjectName("付款");
		list.add(payMall);
		
		MallBalanceSummary billing = new MallBalanceSummary();
		BigDecimal mallBillingTotal = new BigDecimal(0);
		if(mallBalance.getBusinessType()==1){
			billingTotal = service.billingSum(query);
			if(null==billingTotal){
				billingTotal = new BigDecimal(0);
			}
			mallBillingTotal= mallBalance.getMallBillingAmount();
		}
		billing.setDetail("开票金额");
		billing.setCurAmount(billingTotal);
		billing.setMallAmount(mallBillingTotal);
		billing.setDiffAmount(billingTotal.subtract(mallBillingTotal));
		billing.setProjectName("付款");
		list.add(billing);
		return list;
	}

}
