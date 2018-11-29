package topmall.fas.domain.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import com.google.common.base.Function;
import cn.mercury.basic.UUID;
import cn.mercury.basic.query.Q;
import cn.mercury.basic.query.Query;
import cn.mercury.manager.ManagerException;
import topmall.fas.enums.StatusEnums;
import topmall.fas.manager.ISystemConfigManager;
import topmall.fas.manager.impl.CommonStaticManager;
import topmall.fas.model.ContractDiscoPool;
import topmall.fas.model.CounterCost;
import topmall.fas.model.CounterSaleCost;
import topmall.fas.model.CounterSaleCostDtl;
import topmall.fas.model.ShopBalanceDateDtl;
import topmall.fas.service.IContractDiscoPoolService;
import topmall.fas.util.CommonUtil;
import topmall.fas.util.GroupByUtils;
import topmall.fas.util.PublicConstans;
import topmall.fas.vo.CounterDaySale;
import topmall.mdm.model.Counter;
import topmall.mdm.model.Depayment;
import topmall.mdm.model.Supplier;
import topmall.mdm.open.api.ICounterApiService;
import topmall.mdm.open.api.IDepaymentApiService;
import topmall.mdm.open.api.ISupplierApiService;

public class ContractDiscoHandler {
	private ShopBalanceDateDtl shopBalanceDateDtl;
	private IContractDiscoPoolService service;
	//日结数据
	private List<CounterDaySale> list ;
	//记录00码
	private List<ContractDiscoPool> fullPriceList = new ArrayList<>();
	//批次号
	private String seqId;
	private ICounterApiService counterApi;
	private ISupplierApiService supplierApi;
	private Query baseQuery;
	private ISystemConfigManager systemConfigManager;
	//毛收入-净收入
	private BigDecimal reduceDiffAmount= new BigDecimal(0);
	//否是历史费用重算
	private boolean isHisCost;
	
	//新结算期明细(用于历史费用重算)
	private ShopBalanceDateDtl newDateDtl;
	//纸袋数量
	private Integer bagQty =0;
	
	private IDepaymentApiService depaymentApiService;
	
	
	public ContractDiscoHandler(ShopBalanceDateDtl shopBalanceDateDtl,IContractDiscoPoolService service,String seqId,boolean isHisCost,ShopBalanceDateDtl newDateDtl){
		this.shopBalanceDateDtl=shopBalanceDateDtl;
		this.service=service;
		this.counterApi=CommonStaticManager.getCounterApiService();
		this.supplierApi=CommonStaticManager.getSupplierApiService();
		this.systemConfigManager = CommonStaticManager.getSystemConfigManager();
		this.depaymentApiService=CommonStaticManager.getDepaymentApiService();
		this.seqId=seqId;
		this.isHisCost=isHisCost;
		this.newDateDtl=newDateDtl;
	}
	/**
	 * 准备销售数据
	 * 
	 */
	public void prepareDaySaleData(){
		Query query = Query.Where("balanceDateId", shopBalanceDateDtl.getId());
		//部类编码,税率,票扣标识,账扣标识,时间维度分组查询 财务结算期专柜合同扣率池表
		List<ContractDiscoPool> contractGroupDiscoPoolList = service.selectGroupContractDiscoData(query);
		//从日结表 根据 部类编码,销售时间,活动,商品折扣,扣率 维度分组 
		baseQuery = Q.where("counterNo", shopBalanceDateDtl.getCounterNo()).and("shopNo", shopBalanceDateDtl.getShopNo()).and("isHisCost", isHisCost)
				.and("settleStartDate", shopBalanceDateDtl.getSettleStartDate()).and("settleEndDate", shopBalanceDateDtl.getSettleEndDate());
		list = service.selectGroupShopDaySaleData(baseQuery);
		for (CounterDaySale daySale : list) {
			for (ContractDiscoPool discoPool : contractGroupDiscoPoolList) {
				if (daySale.getDivisionNo().equals(discoPool.getDivisionNo())&& valivateSaleDate(daySale, discoPool)) {//部类编码
					daySale.setAccountDebit(discoPool.getAccountDebit());//帐扣标识
					daySale.setBillDebit(discoPool.getBillDebit());//票扣标识
					daySale.setRaxRate(discoPool.getRaxRate());//税率
					break;
				}
			}
		}
		//取正价码的合同信息
		for(ContractDiscoPool discoPool: contractGroupDiscoPoolList){
			if(discoPool.getDivisionNo().endsWith("00")){
				fullPriceList.add(discoPool);
			}
		}
		//如果没有正价码,则随机取一个
		if(!CommonUtil.hasValue(fullPriceList)){
			fullPriceList.add(contractGroupDiscoPoolList.get(0));
		}
		//如果在上面没有取到税率,票扣标识,账扣标识 则取正价码的信息
		for(CounterDaySale daySale : list){
			if(null!=daySale.getAccountDebit()){
				continue;
			}
			for(ContractDiscoPool fullPrice: fullPriceList){
				daySale.setAccountDebit(fullPrice.getAccountDebit());//账扣标识
				daySale.setBillDebit(fullPrice.getBillDebit());//票扣标识
				daySale.setRaxRate(fullPrice.getRaxRate());//税率
				break;
			}
		}
		bagQty =service.bagSaleQty(baseQuery);
	}
	
	/**
	 * 计算销售费用
	 * @return
	 */
	public List<CounterSaleCost> calculateSaleCost(){
		List<CounterSaleCost> saleCostList =  new ArrayList<>();
		Map<String, List<CounterDaySale>> counterDayList = GroupByUtils.groupByKey(list,
				new Function<CounterDaySale, String>() {
					@Override
					public String apply(CounterDaySale dtl) {
						//部类编码+商品折扣+税率+票扣标识+账扣标识 分组
						return dtl.getSaleCostKey();
					}
				});
		for (Entry<String, List<CounterDaySale>> entry : counterDayList.entrySet()) {
			List<CounterDaySale> dtls = entry.getValue();
			CounterSaleCost saleCost = new CounterSaleCost();
			//设置属性
			shopBalanceDateDtl.copyProperties(saleCost);
			CounterDaySale counterDaySale = dtls.get(0);
			//设置属性
			counterDaySale.copyProperties(saleCost);
			//销售总额
			BigDecimal settleSum = new BigDecimal(0);
			//净收入总额
			BigDecimal netIncomeSum = new BigDecimal(0);
			//折扣金额
			BigDecimal discountAmount = new BigDecimal(0);
			//分摊金额
			BigDecimal absorptionAmount = new BigDecimal(0);
			//销售提成
			BigDecimal profitAmount = new BigDecimal(0);
			//销售成本
			BigDecimal sellingCost = new BigDecimal(0);
			Integer saleQty=0;
			for (CounterDaySale daySale : dtls) {
				//销售数量
				saleQty=saleQty+daySale.getSaleQty();
				// 销售总额
				settleSum = settleSum.add(daySale.getSettleAmount());
				//净收入总额
				netIncomeSum = netIncomeSum.add(daySale.getNetIncomeAmount());
				//折扣金额:如果设置了折扣分摊比例，则统计对应销售订单的销售折扣金额汇总
				if (null != daySale.getAbsorptionRate()&& daySale.getAbsorptionRate().compareTo(new BigDecimal(0)) != 0) {
					discountAmount = discountAmount.add(daySale.getTagPriceAmount().multiply(CommonUtil.ONE_HUNDRED.subtract(daySale.getDiscount()))
							.divide(CommonUtil.ONE_HUNDRED, PublicConstans.DECIMAL_PLACE, BigDecimal.ROUND_HALF_UP));
				}
				//分摊金额 :如果设置了折扣分摊比例，则统计对应销售订单的【销售折扣金额*分摊比例】
				//牌价*(100-商品折扣率)*分摊比例/10000
				if (null != daySale.getAbsorptionRate()&& daySale.getAbsorptionRate().compareTo(new BigDecimal(0)) != 0) {
					absorptionAmount = absorptionAmount.add(daySale.getTagPriceAmount().multiply(CommonUtil.ONE_HUNDRED.subtract(daySale.getDiscount()))
							.multiply(daySale.getAbsorptionRate()).divide(new BigDecimal(10000), PublicConstans.DECIMAL_PLACE, BigDecimal.ROUND_HALF_UP));
				}
				//销售提成: 销售总额*商品扣率
				//销售提成=销售总额*商品扣率/100
				profitAmount = profitAmount.add(daySale.getSettleAmount().multiply(daySale.getRateValue()).divide(CommonUtil.ONE_HUNDRED, PublicConstans.DECIMAL_PLACE, BigDecimal.ROUND_HALF_UP));
				//销售成本: 销售总额-销售提成
				sellingCost = sellingCost.add(daySale.getSettleAmount().subtract(daySale.getSettleAmount().multiply(daySale.getRateValue()).divide(CommonUtil.ONE_HUNDRED, PublicConstans.DECIMAL_PLACE, BigDecimal.ROUND_HALF_UP)));
			}
			saleCost.setSellingCost(sellingCost.setScale(PublicConstans.TWO_DECIMAL_PLACE, BigDecimal.ROUND_HALF_UP));
			saleCost.setProfitAmount(profitAmount.setScale(PublicConstans.TWO_DECIMAL_PLACE, BigDecimal.ROUND_HALF_UP));
			saleCost.setAbsorptionAmount(absorptionAmount.setScale(PublicConstans.TWO_DECIMAL_PLACE, BigDecimal.ROUND_HALF_UP));
			saleCost.setSettleSum(settleSum);
			saleCost.setNetIncomeSum(netIncomeSum);
			saleCost.setDiscountAmount(discountAmount);
			saleCost.setSaleQty(saleQty);
			saleCost.setSeqId(seqId);
			saleCost.setStatus(StatusEnums.EFFECTIVE.getStatus());
			saleCost.setId(UUID.newUUID().toString());
			saleCostList.add(saleCost);
			reduceDiffAmount= reduceDiffAmount.add(netIncomeSum.subtract(settleSum));
		}
		CounterSaleCost counterSaleCost= reduceDiffAmount();
		if(null!=counterSaleCost){
			saleCostList.add(counterSaleCost);
		}
		int count=0;
		for(CounterSaleCost saleCost : saleCostList){
			if(0==count){
				if(null==bagQty){
					bagQty=0;
				}
				saleCost.setSaleQty(saleCost.getSaleQty()-bagQty);
			}
			if(isHisCost){
				saleCost.setSettleMonth(newDateDtl.getSettleMonth());
				saleCost.setSettleStartDate(newDateDtl.getSettleStartDate());
				saleCost.setSettleEndDate(newDateDtl.getSettleEndDate());
			}
			saleCost.setActualMonth(shopBalanceDateDtl.getSettleMonth());
			saleCost.setActualStartDate(shopBalanceDateDtl.getSettleStartDate());
			saleCost.setActualEndDate(shopBalanceDateDtl.getSettleEndDate());
			count++;
		}
		return saleCostList;
	}
	/**
	 * 计算销售费明细费用
	 * @return
	 */
	public List<CounterSaleCostDtl> calculateSaleCostDtl(){
		List<CounterSaleCostDtl> saleCostDtlList = new ArrayList<>();
		Map<String, List<CounterDaySale>> counterDayList = GroupByUtils.groupByKey(list,
				new Function<CounterDaySale, String>() {
					@Override
					public String apply(CounterDaySale dtl) {
						//部类编码+商品折扣+税率+票扣标识+账扣标识 +销售日期 分组
						return dtl.getSaleCostDtlKey();
					}
				});
		for (Entry<String, List<CounterDaySale>> entry : counterDayList.entrySet()) {
			List<CounterDaySale> dtls = entry.getValue();
			CounterSaleCostDtl saleCostDtl = new CounterSaleCostDtl();
			//设置属性值
			shopBalanceDateDtl.copyProperties(saleCostDtl);
			CounterDaySale counterDaySale = dtls.get(0);
			//设置属性值
			counterDaySale.copyProperties(saleCostDtl);
			//销售总额
			BigDecimal settleSum = new BigDecimal(0);
			//净收入总额
			BigDecimal netIncomeSum = new BigDecimal(0);
			//折扣金额
			BigDecimal discountAmount = new BigDecimal(0);
			//分摊金额
			BigDecimal absorptionAmount = new BigDecimal(0);
			//销售提成
			BigDecimal profitAmount = new BigDecimal(0);
			//销售成本
			BigDecimal sellingCost = new BigDecimal(0);
			Integer saleQty=0;
			for (CounterDaySale daySale : dtls) {
				//销售数量
				saleQty=saleQty+daySale.getSaleQty();
				// 销售总额
				settleSum = settleSum.add(daySale.getSettleAmount());
				//净收入总额
				netIncomeSum = netIncomeSum.add(daySale.getNetIncomeAmount());
				//折扣金额:如果设置了折扣分摊比例，则统计对应销售订单的销售折扣金额汇总
				if (null != daySale.getAbsorptionRate()&& daySale.getAbsorptionRate().compareTo(new BigDecimal(0)) != 0) {
					discountAmount = discountAmount.add(daySale.getTagPriceAmount().multiply(daySale.getDiscount()).divide(CommonUtil.ONE_HUNDRED, PublicConstans.DECIMAL_PLACE, BigDecimal.ROUND_HALF_UP));
				}
				//分摊金额 :如果设置了折扣分摊比例，则统计对应销售订单的【销售折扣金额*分摊比例】
				//牌价*(100-商品折扣率)*分摊比例/10000
				if (null != daySale.getAbsorptionRate()&& daySale.getAbsorptionRate().compareTo(new BigDecimal(0)) != 0) {
					absorptionAmount = absorptionAmount.add(daySale.getTagPriceAmount().multiply(CommonUtil.ONE_HUNDRED.subtract(daySale.getDiscount()))
							.multiply(daySale.getAbsorptionRate()).divide(new BigDecimal(10000), PublicConstans.DECIMAL_PLACE, BigDecimal.ROUND_HALF_UP));
				}
				//销售提成: 销售总额*商品扣率
				//销售提成=销售总额*商品扣率/100
				profitAmount = profitAmount.add(daySale.getSettleAmount().multiply(daySale.getRateValue()).divide(
						CommonUtil.ONE_HUNDRED, PublicConstans.DECIMAL_PLACE, BigDecimal.ROUND_HALF_UP));
				//销售成本: 销售总额-销售提成
				sellingCost = sellingCost.add(daySale.getSettleAmount().subtract(daySale.getSettleAmount().multiply(daySale.getRateValue()).divide(
						CommonUtil.ONE_HUNDRED, PublicConstans.DECIMAL_PLACE, BigDecimal.ROUND_HALF_UP)));
			}
			saleCostDtl.setSellingCost(sellingCost.setScale(PublicConstans.TWO_DECIMAL_PLACE, BigDecimal.ROUND_HALF_UP));
			saleCostDtl.setProfitAmount(profitAmount.setScale(PublicConstans.TWO_DECIMAL_PLACE, BigDecimal.ROUND_HALF_UP));
			saleCostDtl.setAbsorptionAmount(absorptionAmount.setScale(PublicConstans.TWO_DECIMAL_PLACE, BigDecimal.ROUND_HALF_UP));
			saleCostDtl.setSettleSum(settleSum);
			//销售提成: 销售总额*商品扣率
			saleCostDtl.setNetIncomeSum(netIncomeSum);
			saleCostDtl.setDiscountAmount(discountAmount);
			saleCostDtl.setSeqId(seqId);
			saleCostDtl.setStatus(StatusEnums.EFFECTIVE.getStatus());
			saleCostDtl.setSaleQty(saleQty);
			saleCostDtl.setId(UUID.newUUID().toString());
			saleCostDtlList.add(saleCostDtl);
		}
		for(CounterSaleCostDtl saleCostDtl : saleCostDtlList){
			if(isHisCost){
				saleCostDtl.setSettleMonth(newDateDtl.getSettleMonth());
				saleCostDtl.setSettleStartDate(newDateDtl.getSettleStartDate());
				saleCostDtl.setSettleEndDate(newDateDtl.getSettleEndDate());
			}
			saleCostDtl.setActualMonth(shopBalanceDateDtl.getSettleMonth());
			saleCostDtl.setActualStartDate(shopBalanceDateDtl.getSettleStartDate());
			saleCostDtl.setActualEndDate(shopBalanceDateDtl.getSettleEndDate());
		}
		
		return saleCostDtlList;
	}
	
	/**计算固定费用(券分摊,vip分摊,物料费用,积分抵现等)
	 * @return
	 */
	public List<CounterCost>  calculateCounterCost(){
		List<CounterCost> counterCostList = new ArrayList<>();
		if(!isHisCost){
			//如果是历史费用重算,则不需要计算下面的这部分费用
			CounterCost ticketCost = recalculateTicketAmount();
			if(null!=ticketCost){
				counterCostList.add(ticketCost);
			}
			CounterCost vipCost = recalculateVIPAmount();
			if(null!=vipCost){
				counterCostList.add(vipCost);
			}
			CounterCost materialCost =recalculateMaterialAmount();
			if(null!=materialCost){
				counterCostList.add(materialCost);
			}
			CounterCost pointsCost = recalculatePointsAmount();
			if(null!=pointsCost){
				counterCostList.add(pointsCost);
			}
		}
		return counterCostList;
	}
	
	/** 积分底现费用
	 * @return
	 */
	private CounterCost recalculatePointsAmount(){
		BigDecimal pointsAmount = service.pointsAmount(baseQuery);
		return createCounterCost(pointsAmount,PublicConstans.POINTS_ITEMNO);
	}
	
	/** 券分摊
	 * @return 返回券分摊的费用
	 */
	private CounterCost recalculateTicketAmount(){
		BigDecimal ticketAmount = service.ticketAbsorptionAmonut(baseQuery);
		return  createCounterCost(ticketAmount, PublicConstans.TICKET_ITEMNO);
	}
	
	/** 结算物料费用
	 * @return 返回结算物料费用
	 */
	private CounterCost recalculateMaterialAmount(){
		BigDecimal materialAmount = service.materialAmount(baseQuery);
		return createCounterCost(materialAmount, PublicConstans.MATERIAL_ITEMNO);
	}
	
	/**
	 * VIP分摊金额
	 * @return 返回VIP分摊金额
	 */
	private CounterCost recalculateVIPAmount(){
		Query query = Q.q("counterNo", shopBalanceDateDtl.getCounterNo());
		Counter counter =counterApi.findByParam(query);
		//处理华南的券分摊
		if("K".equals(counter.getZoneNo())){
			BigDecimal vipAmount = service.vipAbsorptionAmonut(baseQuery);
			return createCounterCost(vipAmount,PublicConstans.VIP_ITEMNO);
		}
		return null;
	}
	
	/**生成费用
	 * @param amount
	 * @param itemNo
	 * @return
	 * @throws Exception 
	 */
	private CounterCost createCounterCost(BigDecimal amount,String itemNo){
		if(null==amount||amount.compareTo(new BigDecimal(0))==0){
			return null;
		}
		CounterCost counterCost = new CounterCost();
		shopBalanceDateDtl.copyProperties(counterCost);
		counterCost.setCostNo(itemNo);
		counterCost.setSource((short) 1);//设置生成方式为系统生成
		counterCost.setStatus(StatusEnums.EFFECTIVE.getStatus());
		counterCost.setCreateUser("系统生成");
		counterCost.setCreateTime(new Date());
		counterCost.setRefType((short) 0);
		counterCost.setSeqId(seqId);
		Query query =Q.where("zoneNo", shopBalanceDateDtl.getZoneNo()).and("deductionNo", itemNo);
		Depayment depayment = depaymentApiService.findByParam(query);
		if(null==depayment){
			throw new ManagerException("大区:"+shopBalanceDateDtl.getZoneNo()+"扣项编码:"+itemNo+"没有配置");
		}
		counterCost.setTaxRate(depayment.getTaxRate());
		counterCost.setAccountDebit(depayment.getAccountDebit());
		counterCost.setBillDebit((int)depayment.getBillDebit());
		counterCost.setTaxFlag(depayment.getTaxFlag());
		counterCost.setId(UUID.newUUID().toString());
		//含税
		if(counterCost.getTaxFlag()==1){
			BigDecimal ableAmount = CommonUtil.getTaxFreeCost(amount, depayment.getTaxRate());
			counterCost.setAbleSum(amount);
			counterCost.setAbleAmount(ableAmount);
			counterCost.setTaxAmount(amount.subtract(ableAmount));
		}else if(counterCost.getTaxFlag()==0){
			counterCost.setAbleAmount(amount);
			BigDecimal taxCost = CommonUtil.getTaxCost(amount, depayment.getTaxRate());
			counterCost.setAbleSum(taxCost);
			counterCost.setTaxAmount(taxCost.subtract(amount));
		}
		return counterCost;
	}
	
	/**
	 * 计算净收入与毛收入只差
	 * @param shopBalanceDateDtl
	 */
	private CounterSaleCost reduceDiffAmount(){
		Query query = Q.q("counterNo", shopBalanceDateDtl.getCounterNo());
		Counter counter =counterApi.findByParam(query);
		query.and("supplierNo", counter.getSupplierNo());
		Supplier supplier = supplierApi.findByParam(query);
		//1自营/2招商/3场地出租/0其他
		//如果是自营的,则需要判断是否需要计算
		if(supplier.getCategory()==1){
			//根据大区,状态,参数查
			Query q =Q.where("zoneNo", shopBalanceDateDtl.getZoneNo()).and("parameterNo", PublicConstans.COUNTER_BALANCE_DIFF).and("status", 1);
			Integer count= systemConfigManager.selectCount(q);
			if(0<count){
				if(null!=reduceDiffAmount&&reduceDiffAmount.compareTo(new BigDecimal(0))!=0){
					CounterSaleCost counterSaleCost=new CounterSaleCost();
					shopBalanceDateDtl.copyProperties(counterSaleCost);
					counterSaleCost.setAccountDebit(fullPriceList.get(0).getAccountDebit());
					counterSaleCost.setBillDebit(fullPriceList.get(0).getBillDebit());
					counterSaleCost.setSettleSum(reduceDiffAmount);
					counterSaleCost.setType("0");
					counterSaleCost.setDivisionNo("99999999");
					counterSaleCost.setStatus(StatusEnums.EFFECTIVE.getStatus());
					counterSaleCost.setSeqId(seqId);
					counterSaleCost.setId(UUID.newUUID().toString());
					return counterSaleCost;
				}
			}
		}
		return null;
	}
	/**
	 * 校验时间
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
}
