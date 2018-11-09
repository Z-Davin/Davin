package topmall.fas.manager.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.mercury.basic.query.Q;
import cn.mercury.basic.query.Query;
import cn.mercury.manager.ManagerException;
import cn.mercury.utils.DateUtil;
import topmall.common.enums.BillTypeEnums;
import topmall.fas.enums.StatusEnums;
import topmall.fas.manager.IShopBalanceDateManager;
import topmall.fas.model.ShopBalanceDate;
import topmall.fas.model.ShopBalanceDateDtl;
import topmall.fas.service.IShopBalanceDateDtlService;
import topmall.fas.service.IShopBalanceDateService;
import topmall.fas.util.CommonResult;
import topmall.fas.util.CommonUtil;
import topmall.fas.util.PublicConstans;
import topmall.framework.manager.BaseManager;
import topmall.framework.service.IService;

@Service
public class ShopBalanceDateManager extends BaseManager<ShopBalanceDate, String> implements IShopBalanceDateManager {
	@Autowired
	private IShopBalanceDateService service;

	@Autowired
	private IShopBalanceDateDtlService shopBalanceDateDtlService;

	protected IService<ShopBalanceDate, String> getService() {
		return service;
	}

	/**
	 * @see topmall.fas.manager.IShopBalanceDateManager#validateCreate(topmall.fas.model.ShopBalanceDate)
	 */
	@Override
	public CommonResult validateCreate(ShopBalanceDate shopBalanceDate) {
		if (!CommonUtil.hasValue(shopBalanceDate.getId())) {
			Query query = Query.Where("shopNo", shopBalanceDate.getShopNo());
			ShopBalanceDate data = service.findByUnique(query);
			if (data != null) {
				return CommonResult.error(PublicConstans.VALIDATE_ERROR, "此门店信息已存在!");
			}
		}
		return CommonResult.sucess(null);

	}

	//结算期确认
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public ShopBalanceDate confirm(String id) {
		ShopBalanceDate shopBalanceDate = service.findByPrimaryKey(id);
		shopBalanceDate.setStatus(StatusEnums.EFFECTIVE.getStatus());
		service.update(shopBalanceDate);
		return shopBalanceDate;
	}

	//结算期插入
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public Integer insert(ShopBalanceDate entry) throws ManagerException {
		if (entry.getStatus() == null) {
			entry.setStatus(StatusEnums.INEFFECTIVE.getStatus());
		}
		return super.insert(entry);

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public CommonResult monthBalance(String ids,int targetStatus) {
		try {
			int i=service.selectCount(Q.where("targetStatus", 2).and("ids", ids));
			if(i!=0){ 
				return CommonResult.error("0001", "所选择的数据有非生效状态的数据");
			}
			List<ShopBalanceDate> list = service.selectByParams(Q.where("ids", ids));
			for (ShopBalanceDate shopBalanceDate : list) {
				//需要校验上次关账时间
				Query query = Q.where("shopNo", shopBalanceDate.getShopNo()).and("status",StatusEnums.EFFECTIVE.getStatus());
				String settleMonth = shopBalanceDateDtlService.getUnGeneralCostMinMonth(query);
				query.and("settleMonth", settleMonth);
				Date endDate = shopBalanceDateDtlService.getUnGeneralCostMaxDate(query);
				query.and(Q.LessThenEquals("settleEndDate",endDate));
				List<ShopBalanceDateDtl> dtlList = shopBalanceDateDtlService.selectByParams(query);
				if (!CommonUtil.hasValue(dtlList)) {
					throw new ManagerException("卖场  " + shopBalanceDate.getShopNo() + "没有未结算的结算期明细");
				}
				if (endDate.after(DateUtil.getCurrentDate())) {
					throw new ManagerException("卖场  " + shopBalanceDate.getShopNo() + "关账时间不能大于当前时间");
				}

				if (null != shopBalanceDate.getCloseDate() && shopBalanceDate.getCloseDate().equals(endDate)) {
					throw new ManagerException("卖场  " + shopBalanceDate.getShopNo() + "在"
							+ dtlList.get(0).getSettleMonth() + "该账期已经关账了");
				}
				shopBalanceDate.setCloseDate(endDate);
				service.update(shopBalanceDate);
				int batch=0;
				//保证一个专柜一个结算期明细只有一个异步.
				//防止一个结算期多个明细导致计算重复,因为异步只有专柜和结算期
				HashMap<String,Boolean> flag = new HashMap<>();
				for (ShopBalanceDateDtl shopBalanceDateDtl : dtlList) {
					//把结算月放到In_out_flag当中, 专柜编码放入单据编号
					try {
						if(null==flag.get(shopBalanceDateDtl.getCounterNo())){
							CommonStaticManager.insertUnAccountBill(shopBalanceDateDtl.getCounterNo(),shopBalanceDateDtl.getZoneNo() ,BillTypeEnums.GENERATE_COUNTER_COST,Integer.parseInt(shopBalanceDateDtl.getSettleMonth()),batch);
							flag.put(shopBalanceDateDtl.getCounterNo(), true);
						}
					} catch (Exception e) {
						if (e.getCause() != null && e.getCause() instanceof DuplicateKeyException) {
							logger.info("重复的异步任务:" + shopBalanceDateDtl.toString());
							continue;
						} else {
							logger.error("月结异常：" + e.getMessage(), e);
							throw new ManagerException("月结异常：" + e.getMessage(), e);
						}
					}
					batch++;
				}
			}
			return CommonResult.getSucessResult();
		} catch (Exception e) {
			logger.error("月结异常：" + e.getMessage(), e);
			throw new ManagerException("月结异常：" + e.getMessage());
		}

	}

	@Override
	public ShopBalanceDate findByUnique(Query query) {
		return service.findByUnique(query);
	}

}
