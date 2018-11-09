/**
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
package topmall.fas.manager.impl;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;

import topmall.common.enums.BillTypeEnums;
import topmall.common.manager.UnAccountBillPoolManager;
import topmall.common.model.UnAccountBillPool;
import topmall.fas.manager.ICounterCostManager;
import topmall.fas.manager.ICounterSaleCostDtlManager;
import topmall.fas.manager.ICounterSaleCostManager;
import topmall.fas.manager.IMallBalanceDateDtlManager;
import topmall.fas.manager.IMallCostManager;
import topmall.fas.manager.IMallSaleCostManager;
import topmall.fas.manager.IPropertyCostManager;
import topmall.fas.manager.IShopBalanceDateDtlManager;
import topmall.fas.manager.IShopCostManager;
import topmall.fas.manager.ISystemConfigManager;
import topmall.fas.service.IContractDiscoPoolService;
import topmall.mdm.open.api.ICounterApiService;
import topmall.mdm.open.api.IDepaymentApiService;
import topmall.mdm.open.api.ISupplierApiService;
import topmall.mps.api.client.IPromotionApi;
import topmall.pos.api.IMallDaySaleApiService;
import topmall.pos.api.IShopDaySaleApiService;

/**
 * Manager工具类
 * 
 * @author dai.j
 * @date 2017-8-28 下午2:39:22
 * @version 0.1.0 
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
@Component
public class CommonStaticManager {

	private static CommonStaticManager instance;

	@Reference
	private IShopDaySaleApiService shopDaySaleApiService;

	@Reference
	private IPromotionApi promotionApi;

	@Autowired
	private ICounterCostManager counterCostManager;

	@Autowired
	private IMallCostManager mallCostManager;

	@Autowired
	private IShopCostManager shopCostManager;

	@Autowired(required = false)
	protected UnAccountBillPoolManager unAccountBillPoolManager;

	@Resource
	private IShopBalanceDateDtlManager shopBalanceDateDtlManager;

	@Autowired
	private ICounterSaleCostManager counterSaleCostManager;

	@Autowired
	private ICounterSaleCostDtlManager counterSaleCostDtlManager;

	@Autowired
	private IPropertyCostManager propertyCostManager;

	@Autowired
	private IMallBalanceDateDtlManager mallBalanceDateDtlManager;

	@Reference
	private ICounterApiService counterApi;
	@Reference
	private ISupplierApiService supplierApi;

	@Autowired
	private ISystemConfigManager systemConfigManager;

	@Autowired
	private IMallSaleCostManager mallSaleCostManager;
	
	@Autowired
	private IContractDiscoPoolService contractDiscoPoolService;
	
	@Reference
	private IMallDaySaleApiService mallDaySaleApiService;
	
	@Reference
	private IDepaymentApiService depaymentApiService;
	
	@PostConstruct
	public void init() {
		instance = this;
	}
	
	public static IDepaymentApiService getDepaymentApiService(){
		return instance.depaymentApiService;
	}
	
	public static IContractDiscoPoolService getContractDiscoPoolService() {
		return instance.contractDiscoPoolService;
	}

	public static ICounterApiService getCounterApiService() {
		return instance.counterApi;
	}

	public static ISupplierApiService getSupplierApiService() {
		return instance.supplierApi;
	}

	public static ISystemConfigManager getSystemConfigManager() {
		return instance.systemConfigManager;
	}

	/**
	 * 获取POS销售日结的数据查询接口
	 */
	public static IShopDaySaleApiService getShopDaySaleApiService() {
		return instance.shopDaySaleApiService;
	}

	/**
	 * 获取专柜费用的Manager
	 */
	public static ICounterCostManager getCounterCostManager() {
		return instance.counterCostManager;
	}

	/**
	 * 获取专柜费用的Manager
	 */
	public static IShopBalanceDateDtlManager getShopBalanceDateDtlManager() {
		return instance.shopBalanceDateDtlManager;
	}

	/**
	 * 获取营促销的活动减免费用查询接口
	 */
	public static IPromotionApi getPromotionApi() {
		return instance.promotionApi;
	}

	/**
	 * 获取专柜费用Manager
	 */
	public static IShopCostManager getShopCostManager() {
		return instance.shopCostManager;
	}

	/**
	 * 获取物业费用Manager
	 */
	public static IMallCostManager getMallCostManager() {
		return instance.mallCostManager;
	}

	/**
	 * 获取销售费用manager
	 */
	public static ICounterSaleCostManager getCounterSaleCostManager() {
		return instance.counterSaleCostManager;
	}

	/**
	 * 获取销售费用明细manager
	 */
	public static ICounterSaleCostDtlManager getCounterSaleCostDtlManager() {
		return instance.counterSaleCostDtlManager;
	}

	/**
	 * 获取物业费用登记manager
	 */
	public static IPropertyCostManager getPropertyCostManager() {
		return instance.propertyCostManager;
	}

	/**
	 * 获取物业结算期明细manager
	 * @return
	 */
	public static IMallBalanceDateDtlManager getMallBalanceDateDtlManager() {
		return instance.mallBalanceDateDtlManager;
	}

	public static int insertUnAccountBill(String billNo, String shardingFlag, BillTypeEnums billType,
			Integer inOutFlag, int batch) {
		UnAccountBillPool billPool = new UnAccountBillPool(billNo, shardingFlag, billType.getRequestId(), inOutFlag, 2);
		billPool.setBatch(batch);
		return instance.unAccountBillPoolManager.insert(billPool);
	}
	
	/**
	 * fas产生其他系统的异步任务
	 */
	public static int insertUnAccountBill(String billNo, String shardingFlag, BillTypeEnums billType,
			Integer inOutFlag, int batch, Integer sourceSystem) {
		UnAccountBillPool billPool = new UnAccountBillPool(billNo, shardingFlag, billType.getRequestId(), inOutFlag, sourceSystem);
		billPool.setBatch(batch);
		return instance.unAccountBillPoolManager.insert(billPool);
	}
	
	/**
	 * 获取物业销售查询Manager
	 */
	public static IMallSaleCostManager getMallSaleCostManager() {
		return instance.mallSaleCostManager;
	}
	
	public static IMallDaySaleApiService getMallDaySaleApiService() {
		return instance.mallDaySaleApiService;
	}
}
