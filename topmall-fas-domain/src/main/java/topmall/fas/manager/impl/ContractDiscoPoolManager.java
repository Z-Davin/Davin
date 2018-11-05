package topmall.fas.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.mercury.basic.query.Query;
import topmall.fas.domain.handler.ContractDiscoHandler;
import topmall.fas.domain.handler.MallConDiscoHandler;
import topmall.fas.manager.IContractDiscoPoolManager;
import topmall.fas.model.ContractDiscoPool;
import topmall.fas.model.CounterCost;
import topmall.fas.model.CounterSaleCost;
import topmall.fas.model.CounterSaleCostDtl;
import topmall.fas.model.MallBalanceDateDtl;
import topmall.fas.model.MallCost;
import topmall.fas.model.MallSaleCost;
import topmall.fas.model.ShopBalanceDateDtl;
import topmall.fas.service.IContractDiscoPoolService;
import topmall.fas.service.ICounterCostService;
import topmall.fas.service.ICounterSaleCostDtlService;
import topmall.fas.service.ICounterSaleCostService;
import topmall.fas.service.IMallCostService;
import topmall.fas.service.IMallMinimumDataService;
import topmall.fas.service.IMallSaleCostService;
import topmall.fas.util.CommonUtil;
import topmall.fas.util.PublicConstans;
import topmall.framework.core.CodingRuleHelper;
import topmall.framework.manager.BaseManager;
import topmall.framework.service.IService;
import topmall.mdm.open.api.ICounterApiService;
import topmall.mdm.open.api.ISupplierApiService;

@Service
public class ContractDiscoPoolManager extends BaseManager<ContractDiscoPool, String> implements
		IContractDiscoPoolManager {
	@Autowired
	private IContractDiscoPoolService service;
	@Autowired
	private ICounterSaleCostService counterSaleCostService;
	@Autowired
	private IMallSaleCostService mallSaleCostService;
	@Autowired
	private IMallCostService mallCostService;
	@Autowired
	private ICounterCostService counterCostService;
	@Reference
	private ICounterApiService counterApi;
	@Reference
	private ISupplierApiService supplierApi;
	@Autowired
	private ICounterSaleCostDtlService counterSaleCostDtlService;
	@Autowired
	private IMallMinimumDataService minimumDataService;

	protected IService<ContractDiscoPool, String> getService() {
		return service;
	}

	/**
	 * @see topmall.fas.manager.IContractDiscoPoolManager#selectValidDisco(cn.mercury.basic.query.Query)
	 */
	@Override
	public List<ContractDiscoPool> selectValidDisco(Query query) {
		return service.selectValidDisco(query);
	}

	@Override
	public void createContractDiscoCost(ShopBalanceDateDtl shopBalanceDateDtl,boolean isHisCost,ShopBalanceDateDtl newDateDtl) {
		String seqId =shopBalanceDateDtl.getShopNo()+ CodingRuleHelper.getBasicCoding(PublicConstans.COUNTER_SALE_COST,shopBalanceDateDtl.getShopNo(),null);
		ContractDiscoHandler discoHandler = new ContractDiscoHandler(shopBalanceDateDtl,service,seqId,isHisCost,newDateDtl);
		discoHandler.prepareDaySaleData();
		List<CounterSaleCost> saleCostList = discoHandler.calculateSaleCost();
		List<CounterSaleCostDtl> saleCostDtlList = discoHandler.calculateSaleCostDtl();
		List<CounterCost> counterCostList=discoHandler.calculateCounterCost();
		if(CommonUtil.hasValue(saleCostList)){
			for(CounterSaleCost saleCost:saleCostList){
				counterSaleCostService.insert(saleCost);
			}
		}
		if(CommonUtil.hasValue(saleCostDtlList)){
			for(CounterSaleCostDtl saleCostDtl:saleCostDtlList){
				counterSaleCostDtlService.insert(saleCostDtl);
			}
		}
		if(CommonUtil.hasValue(counterCostList)){
			for(CounterCost counterCost:counterCostList){
				counterCostService.insert(counterCost);
			}
		}
	}

	/* (non-Javadoc)
	 * @see topmall.fas.manager.IContractDiscoPoolManager#createMallDiscoCost(topmall.fas.model.MallBalanceDateDtl)
	 */
	@Override
	public void createMallDiscoCost(MallBalanceDateDtl mallBalanceDateDtl,boolean isRecalculation) {
		String seqId =mallBalanceDateDtl.getShopNo()+ CodingRuleHelper.getBasicCoding(PublicConstans.MALL_SALE_COST,mallBalanceDateDtl.getShopNo(),null);
		MallConDiscoHandler handler = new MallConDiscoHandler(mallBalanceDateDtl,service,seqId,isRecalculation,mallSaleCostService,mallCostService);
		handler.prepareDaySaleData();
		List<MallSaleCost> carryOverSaleList = handler.carryOverSaleData();
		List<MallCost> carryOverCostList = handler.carryOverCostData();
		List<MallSaleCost> list= handler.calculateMallSaleCost();
		for(MallSaleCost mallSaleCost:carryOverSaleList){
			mallSaleCostService.insert(mallSaleCost);
		}
		for(MallCost mallCost:carryOverCostList){
			mallCostService.insert(mallCost);
		}
		for(MallSaleCost mallSaleCost: list){
			mallSaleCostService.insert(mallSaleCost);
		}
		//填入保底信息
		minimumDataService.insertOrUpdate(handler.getMallMinimum());
	}
}
