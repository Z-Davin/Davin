package topmall.fas.manager.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.mercury.basic.query.Query;
import cn.mercury.manager.ManagerException;
import cn.mercury.utils.DateUtil;
import topmall.common.enums.BillTypeEnums;
import topmall.fas.enums.StatusEnums;
import topmall.fas.manager.IMallBalanceDateDtlManager;
import topmall.fas.manager.IMallBalanceDateManager;
import topmall.fas.model.MallBalanceDate;
import topmall.fas.model.MallBalanceDateDtl;
import topmall.fas.service.IMallBalanceDateService;
import topmall.fas.util.CommonResult;
import topmall.fas.util.CommonUtil;
import topmall.fas.util.PublicConstans;
import topmall.framework.manager.BaseManager;
import topmall.framework.service.IService;

@Service
public class MallBalanceDateManager extends BaseManager<MallBalanceDate, String> implements IMallBalanceDateManager {
	@Autowired
	private IMallBalanceDateService service;

	@Autowired
	private IMallBalanceDateDtlManager mallBalanceDateDtlManager;

	protected IService<MallBalanceDate, String> getService() {
		return service;
	}

	@Override
	public CommonResult validateCreate(MallBalanceDate mallBalanceDate) {
		// 表示是编辑
		if (!CommonUtil.hasValue(mallBalanceDate.getId())) {
			Query query = Query.Where("shopNo", mallBalanceDate.getShopNo()).and("mallNo", mallBalanceDate.getMallNo()).and("bunkGroupNo",mallBalanceDate.getBunkGroupNo());
			MallBalanceDate data = service.findByUnique(query);
			if (data != null) {
				return CommonResult.error(PublicConstans.VALIDATE_ERROR, "此门店信息已存在!");
			}
		}
		return CommonResult.sucess(null);

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public MallBalanceDate confirm(String id) {
		MallBalanceDate mallBalanceDate = service.findByPrimaryKey(id);
		mallBalanceDate.setStatus(StatusEnums.EFFECTIVE.getStatus());
		service.update(mallBalanceDate);
		return mallBalanceDate;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public Integer insert(MallBalanceDate entry) throws ManagerException {
		if (entry.getStatus() == null) {
			entry.setStatus(StatusEnums.INEFFECTIVE.getStatus());
		}
		return super.insert(entry);

	}

	/**
	 * @see topmall.fas.manager.IMallBalanceDateManager#monthBalance(java.lang.String)
	 */
	@Override
	public void monthBalance(String idList) throws Exception {
		String[] ids = idList.split(",");
		for (String id : ids) {
			MallBalanceDate mallBalanceDate = findByPrimaryKey(id);

			// 需要校验上次关账时间
			Query query =mallBalanceDate.baseQuery().and("status", StatusEnums.EFFECTIVE.getStatus());

			Date endDate = mallBalanceDateDtlManager.getUnGeneralCostMinDate(query);
			query.and("settleEndDate", endDate);

			MallBalanceDateDtl dtl = mallBalanceDateDtlManager.findByParam(query);

			if (null==dtl) {
				throw new ManagerException("卖场  " + mallBalanceDate.getShopNo() + "物业" + mallBalanceDate.getMallNo() + "没有未结算的结算期明细");
			}
			if (endDate.after(DateUtil.getCurrentDate())) {
				throw new ManagerException("卖场  " + mallBalanceDate.getShopNo() + "物业" + mallBalanceDate.getMallNo() + "关账时间不能大于当前时间");
			}

			if (null != mallBalanceDate.getCloseDate() && mallBalanceDate.getCloseDate().equals(endDate)) {
				throw new ManagerException("卖场  " + mallBalanceDate.getShopNo() + "物业" + mallBalanceDate.getMallNo() + "在" + dtl.getSettleMonth() + "该账期已经关账了");
			}
			mallBalanceDate.setCloseDate(endDate);

			update(mallBalanceDate);
			String billNo=dtl.getShopNo()+"-"+dtl.getMallNo()+"-"+dtl.getBunkGroupNo();
			// 单据编码是=卖场-物业公司-铺位组
			//inOutFlag放结算月
			//批次号放结束时间  异步任务先执行物业日结，物业日结完成后会再次放一个 生成物业费用计算的异步任务
			CommonStaticManager.insertUnAccountBill(billNo, mallBalanceDate.getZoneNo(),
					BillTypeEnums.MALL_DAY_SALE, Integer.parseInt(dtl.getSettleMonth()),Integer.parseInt(DateUtil.format2(dtl.getSettleEndDate())),1);
		}
	}

	/**
	 * @see topmall.fas.manager.IMallBalanceDateManager#findByUnique(cn.mercury.basic.query.Query)
	 */
	@Override
	public MallBalanceDate findByUnique(Query query) {
		return service.findByUnique(query);
	}

}
