package topmall.fas.domain.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import cn.mercury.basic.UUID;
import cn.mercury.basic.query.Q;
import cn.mercury.basic.query.Query;
import topmall.fas.enums.StatusEnums;
import topmall.fas.model.ContractDiscoPool;
import topmall.fas.model.MallBalanceDateDtl;
import topmall.fas.model.MallCost;
import topmall.fas.model.MallMinimumData;
import topmall.fas.model.MallSaleCost;
import topmall.fas.service.IContractDiscoPoolService;
import topmall.fas.service.IMallCostService;
import topmall.fas.service.IMallSaleCostService;
import topmall.fas.util.CommonUtil;
import topmall.fas.util.PublicConstans;
import topmall.fas.vo.CounterDaySale;

public class MallConDiscoHandler {
	private final String REMARK="系统自动结转";
	//物业计算期明细
	private MallBalanceDateDtl mallBalanceDateDtl;
	//组装过的销售日结数据
	private List<CounterDaySale> list;
	
	private IContractDiscoPoolService service;
	//批次号
	private String seqId;
	//结转需要数据
	private List<MallSaleCost>  carrySaleList;
	//结转需要数据
	private List<MallCost> carryCostList;
	@Autowired
	private IMallSaleCostService mallSaleCostService;
	@Autowired
	private IMallCostService mallCostService;
	//是否当月费用重算
	private boolean isRecalculation;
	
	private int status[] ={2,3,4,5,6,7};
	
	private List<CounterDaySale> minimumList;
	
	private MallMinimumData mallMinimum = new MallMinimumData(); 	
	
	public MallConDiscoHandler(MallBalanceDateDtl mallBalanceDateDtl,IContractDiscoPoolService service,String seqId,boolean isRecalculation,IMallSaleCostService mallSaleCostService,IMallCostService mallCostService){
		this.mallBalanceDateDtl=mallBalanceDateDtl;
		this.service=service;
		this.seqId=seqId;
		this.isRecalculation=isRecalculation;
		this.mallSaleCostService=mallSaleCostService;
		this.mallCostService=mallCostService;
	}
	
	/**
	 * 数据准备
	 */
	public void prepareDaySaleData(){
		Query query = Query.Where("balanceDateId", mallBalanceDateDtl.getId());
		//部类编码,税率,票扣标识,账扣标识,时间维度分组查询 
		List<ContractDiscoPool> contractGroupDiscoPoolList = service.selectGroupContractDiscoData(query);
		Query q = mallBalanceDateDtl.baseQuery();
		if(1==mallBalanceDateDtl.getCalculationMethod()){
			//表示按照净收入
			q.and("calculationMethod", true);
		}
		list = service.selectGroupShopDaySaleDataForMall(q);
		for(CounterDaySale daySale :list){
			for (ContractDiscoPool discoPool : contractGroupDiscoPoolList) {
				if (daySale.getPriceType().equals(discoPool.getDivisionNo())&& valivateSaleDate(daySale, discoPool)) {
					daySale.setAccountDebit(discoPool.getAccountDebit());
					daySale.setBillDebit(discoPool.getBillDebit());
					daySale.setRaxRate(discoPool.getRaxRate());
					break;
				}
			}
		}
		//判断合同里面有没有设置全部,如果有全部,则没有取到的取全部
		List<ContractDiscoPool> allList = new ArrayList<>();
		for(ContractDiscoPool discoPool: contractGroupDiscoPoolList){
			if(discoPool.getDivisionNo().equals("ALL")){
				allList.add(discoPool);
			}
		}
		if(allList.isEmpty()){
			for(ContractDiscoPool discoPool: contractGroupDiscoPoolList){
				if(discoPool.getDivisionNo().equals("00")){
					allList.add(discoPool);
				}
			}
		}
		for(CounterDaySale daySale :list){
			if(null==daySale.getAccountDebit()){
				for(ContractDiscoPool all: allList){
					if(valivateSaleDate(daySale, all)){
						daySale.setAccountDebit(all.getAccountDebit());
						daySale.setBillDebit(all.getBillDebit());
						daySale.setRaxRate(all.getRaxRate());
						break;
					}
				}
			}
		}
		if(!isRecalculation){
			Query carryQuery = Q.And(Q.Equals("shopNo", mallBalanceDateDtl.getShopNo()),
			Q.LessThen("settleEndDate", mallBalanceDateDtl.getSettleEndDate())).asQuery();
			carryQuery.and("settleStatus", StatusEnums.MAKEBILL.getStatus().toString()).and("mallNo", mallBalanceDateDtl.getMallNo()).and("bunkGroupNo", mallBalanceDateDtl.getBunkGroupNo());
			carrySaleList = mallSaleCostService.selectByParams(carryQuery);
			carryQuery.and(Q.In("status", status));
			carryCostList = mallCostService.selectByParams(carryQuery);
		}
		if(mallBalanceDateDtl.getPointsCalculateFlag()==0){
			q.and("pointsCalculateFlag", true);
		}
		minimumList=service.queryMinimumMall(q);
		this.calculateMinimum();
	}
	
	private void calculateMinimum(){
		BigDecimal ncDeductionAmount = new BigDecimal(0);
		for(CounterDaySale counterdaysale :minimumList){
			mallMinimum.setMinimumAmount(mallMinimum.getMinimumAmount().add(counterdaysale.getMinimumAmount()));
			mallMinimum.setNotMinimumAmount(mallMinimum.getNotMinimumAmount().add(counterdaysale.getNotMinimumAmount()));
			mallMinimum.setSettleAmount(mallMinimum.getSettleAmount().add(counterdaysale.getMinimumAmount()).add(counterdaysale.getNotMinimumAmount()));
			ncDeductionAmount=ncDeductionAmount.add(counterdaysale.getMinimumAmount().multiply(counterdaysale.getRateValue()).divide(CommonUtil.ONE_HUNDRED, PublicConstans.DECIMAL_PLACE, BigDecimal.ROUND_HALF_UP));
		}
		mallMinimum.setNcDeductionAmount(ncDeductionAmount.setScale(PublicConstans.TWO_DECIMAL_PLACE, BigDecimal.ROUND_HALF_UP));
		mallMinimum.setId(UUID.newUUID().toString());
		mallBalanceDateDtl.copyProperties(mallMinimum);
	}
	
	/**
	 * 计算销售费用
	 * @return
	 */
	public List<MallSaleCost> calculateMallSaleCost() {
		List<MallSaleCost> saleList =  new ArrayList<>();
		Map<String, List<CounterDaySale>> counterDayList = list.stream().collect(Collectors.groupingBy(CounterDaySale::getMallSaleCostKey));
		for (Entry<String, List<CounterDaySale>> entry : counterDayList.entrySet()) {
			List<CounterDaySale> dtls = entry.getValue();
			MallSaleCost mallsaleCost = new MallSaleCost();
			CounterDaySale counterDaySale = dtls.get(0);
			mallBalanceDateDtl.copyProperties(mallsaleCost);
			mallsaleCost.setPriceType(counterDaySale.getPriceType());
			mallsaleCost.setBillDebit(counterDaySale.getBillDebit());
			mallsaleCost.setAccountDebit(counterDaySale.getAccountDebit());
			mallsaleCost.setRaxRate(counterDaySale.getRaxRate());
			mallsaleCost.setRateValue(counterDaySale.getRateValue());
			//销售总额
			BigDecimal settleSum = new BigDecimal(0);
			BigDecimal profitAmount = new BigDecimal(0);
			BigDecimal pointsAmount = new BigDecimal(0);
			BigDecimal deductPointsAmount = new BigDecimal(0);
			
			for (CounterDaySale daySale : dtls) {
				// 销售总额
				settleSum = settleSum.add(daySale.getSettleAmount());
				pointsAmount=pointsAmount.add(daySale.getPointsAmount());
				deductPointsAmount=deductPointsAmount.add(daySale.getSettleAmount().subtract(daySale.getPointsAmount()));
				//销售提成: 销售总额*商品扣率
				//销售提成=销售总额*商品扣率/100
				if(mallBalanceDateDtl.getPointsCalculateFlag()==0){
					//0:不参与计算
					profitAmount=profitAmount.add((daySale.getSettleAmount().subtract(daySale.getPointsAmount())).multiply(daySale.getRateValue()).divide(CommonUtil.ONE_HUNDRED, PublicConstans.DECIMAL_PLACE, BigDecimal.ROUND_HALF_UP));
				}else if(mallBalanceDateDtl.getPointsCalculateFlag()==1){
					//1:参与计算
					profitAmount = profitAmount.add(daySale.getSettleAmount().multiply(daySale.getRateValue()).divide(CommonUtil.ONE_HUNDRED, PublicConstans.DECIMAL_PLACE, BigDecimal.ROUND_HALF_UP));
				}
			}
			mallsaleCost.setProfitAmount(profitAmount.setScale(PublicConstans.TWO_DECIMAL_PLACE, BigDecimal.ROUND_HALF_UP));
			mallsaleCost.setSettleSum(settleSum);
			mallsaleCost.setDeductPointsAmount(deductPointsAmount);
			mallsaleCost.setPointsAmount(pointsAmount);
			mallsaleCost.setSeqId(seqId);
			mallsaleCost.setStatus(StatusEnums.EFFECTIVE.getStatus());
			mallsaleCost.setId(UUID.newUUID().toString());
			saleList.add(mallsaleCost);
		}
		return saleList;
	}
	
	/**
	 * 结转以前销售数据
	 */
	public List<MallSaleCost> carryOverSaleData(){
		//先查询出结算结束时间之前全部为完结的数据
		List<MallSaleCost>  carrySale= new ArrayList<>();
		if(carrySaleList!=null&&!carrySaleList.isEmpty()){
			for (MallSaleCost mallSaleCost : carrySaleList) {
				MallSaleCost saleCost = new MallSaleCost();
				BeanUtils.copyProperties(mallSaleCost, saleCost);
				saleCost.setId(UUID.newUUID().toString());
				saleCost.setParentId(mallSaleCost.getId());
				saleCost.setPreDiffAmount(mallSaleCost.getCurDiffAmount());
				saleCost.setDiffReason(null);
				saleCost.setPointsAmount(null);
				saleCost.setDeductPointsAmount(null);
				saleCost.setAdjustAmount(null);
				saleCost.setBackAmount(null);
				saleCost.setSettleSum(null);
				saleCost.setMallAmount(null);
				saleCost.setCurDiffAmount(null);
				saleCost.setDiffAmount(null);
				saleCost.setProfitAmount(null);
				saleCost.setBalanceBillNo(null);
				saleCost.setStatus(StatusEnums.EFFECTIVE.getStatus());
				saleCost.setCreateTime(new Date());
				saleCost.setUpdateTime(new Date());
				saleCost.setCreateUser(REMARK);
				saleCost.setUpdateUser(REMARK);
				mallSaleCost.setSettleStatus(StatusEnums.OVER.getStatus());
				mallSaleCostService.update(mallSaleCost);
				carrySale.add(saleCost);
			}
		}
		return carrySale;
	}
	
	/**
	 * 结转数据
	 * @return
	 */
	public List<MallCost> carryOverCostData(){
		//先查询出结算结束时间之前全部为完结的数据
		List<MallCost>  carryCost= new ArrayList<>();
		if(carryCostList!=null&&!carryCostList.isEmpty()){
			for (MallCost mallCost : carryCostList) {
				MallCost cost = new MallCost();
				BeanUtils.copyProperties(mallCost, cost);
				cost.setAbleAmount(null);
				cost.setAbleSum(null);
				cost.setMallAmount(null);
				cost.setTaxAmount(null);
				cost.setId(UUID.newUUID().toString());
				cost.setParentId(mallCost.getId());
				cost.setPreDiffAmount(mallCost.getCurDiffAmount());
				cost.setDiffReason(null);
				cost.setAdjustAmount(null);
				cost.setBackAmount(null);
				cost.setBalanceBillNo(null);
				cost.setCreateTime(new Date());
				cost.setUpdateTime(new Date());
				cost.setAuditTime(new Date());
				cost.setAuditor(REMARK);
				cost.setCreateUser(REMARK);
				cost.setUpdateUser(REMARK);
				cost.setStatus(StatusEnums.EFFECTIVE.getStatus());
				mallCost.setSettleStatus(StatusEnums.OVER.getStatus());
				mallCostService.update(mallCost);
				carryCost.add(cost);
			}
		}
		return carryCost;
	}
	
	/**判断销售里面的销售时间是否包含在合同时间里面
	 * @param daySale
	 * @param discoPool
	 * @return
	 */
	private Boolean valivateSaleDate(CounterDaySale daySale, ContractDiscoPool discoPool) {
		Boolean flag = false;
		if ((!daySale.getSaleDate().before(discoPool.getStartDate()))&& (!daySale.getSaleDate().after(discoPool.getEndDate()))) {
			flag = true;
		}
		return flag;
	}

	public MallMinimumData getMallMinimum() {
		return mallMinimum;
	}
	
}
