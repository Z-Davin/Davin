package topmall.fas.jms.handler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.mercury.basic.query.Q;
import cn.mercury.basic.query.Query;
import cn.mercury.manager.ManagerException;
import topmall.common.enums.BillTypeEnums;
import topmall.common.jms.handler.BaseBillPoolHandler;
import topmall.common.model.UnAccountBillPool;
import topmall.fas.enums.StatusEnums;
import topmall.fas.manager.ICounterCostManager;
import topmall.fas.manager.IMallBalanceDateDtlManager;
import topmall.fas.manager.IMallCostManager;
import topmall.fas.manager.IShopBalanceDateDtlManager;
import topmall.fas.manager.impl.CommonStaticManager;
import topmall.fas.model.MallBalanceDateDtl;
import topmall.fas.model.ShopBalanceDateDtl;
import topmall.fas.util.CommonUtil;

/**
 * 计算费用
 * @author dai.xw
 *
 */
public class CalcCostHandler extends BaseBillPoolHandler {

	private IShopBalanceDateDtlManager shopBalanceDateDtlManager;

	private ICounterCostManager counterCostManager;

	private IMallCostManager mallCostManager;

	private IMallBalanceDateDtlManager mallBalanceDateDtlManager;

	public CalcCostHandler(UnAccountBillPool unAccountBillPool) {
		super(unAccountBillPool);
	}

	@Override
	public void check() {
		this.shopBalanceDateDtlManager = CommonStaticManager.getShopBalanceDateDtlManager();
		this.counterCostManager = CommonStaticManager.getCounterCostManager();
		this.mallCostManager = CommonStaticManager.getMallCostManager();
		this.mallBalanceDateDtlManager = CommonStaticManager.getMallBalanceDateDtlManager();

	}

	@Override
	public void processDetail() {
		String billNo = unAccountBillPool.getBillNo(); // 专柜结算的单据号是 专柜编码 ;物业结算的单据号是 卖场
		String settleMonth = unAccountBillPool.getInOutFlag().toString();
		Integer batch = unAccountBillPool.getBatch(); //物业batch放的是结束时间

		Integer billType = unAccountBillPool.getBillType();
		String errorMsg;

		if (BillTypeEnums.GENERATE_COUNTER_COST.getRequestId().equals(billType)) {

			Query query = Q.where("counterNo", billNo).and("settleMonth", settleMonth).and("status",
					StatusEnums.EFFECTIVE.getStatus());

			List<ShopBalanceDateDtl> list = shopBalanceDateDtlManager.selectByParams(query);
			for (ShopBalanceDateDtl shopBalanceDateDtl : list) {
				try {
					counterCostManager.generateCounterCost(shopBalanceDateDtl);
				} catch (Exception e) {
					errorMsg = "生成专柜费用失败:" + shopBalanceDateDtl.toString() + e.getMessage();
					logger.error(errorMsg);
					throw new ManagerException(e.getMessage(), e);
				}
			}
		} else if (BillTypeEnums.GENERATE_MALL_COST.getRequestId().equals(billType)) {
			// 单据编码是=卖场-物业公司-铺位组
			//inOutFlag=放结算月
			//batch=批次号放结束时间
			String[] keys = billNo.split("-");
			Date settleEndDate;
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				settleEndDate = sdf.parse(String.valueOf(batch));
			} catch (Exception e1) {
				errorMsg = "生成物业费用失败:批次号有无  " + batch + e1.getMessage();
				logger.error(errorMsg);
				throw new ManagerException(e1.getMessage());
			}
			Query query = Q.where("shopNo", keys[0]).and("mallNo", keys[1]).and("bunkGroupNo", keys[2])
					.and("settleMonth", settleMonth).and("status", StatusEnums.EFFECTIVE.getStatus())
					.and("settleEndDate", settleEndDate);

			List<MallBalanceDateDtl> balanceDateDtlList = mallBalanceDateDtlManager.selectByParams(query);
			for (MallBalanceDateDtl mallBalanceDateDtl : balanceDateDtlList) {
				try {
					mallCostManager.generateMallCost(mallBalanceDateDtl);
				} catch (Exception e) {
					errorMsg = "生成物业费用出错:" + mallBalanceDateDtl.toString()+e.getMessage();
					logger.error(errorMsg);
					throw new ManagerException(e.getMessage());
				}
			}
		} else if (BillTypeEnums.GENERATE_HIS_COUNTER_COST.getRequestId().equals(billType)) {
			String [] keys = billNo.split("-");
			String counterNo = keys[0];
			String oldSettleMonth = settleMonth;
			String newSettleMonth = keys[1];

			//先查询需要重算的历史月份的结算期，如果历史月份的结算期有多个则重算失败
			Query oldQuery = Q.where("counterNo", counterNo).and("settleMonth", oldSettleMonth);
			List<ShopBalanceDateDtl> list = shopBalanceDateDtlManager.selectByParams(oldQuery);
			if (list.size() > 1) {
				errorMsg = "历史费用重算失败,专柜" + counterNo + "重算结算月" + oldSettleMonth + "有多个结算期";
				logger.error(errorMsg);
				throw new ManagerException(errorMsg);
			}

			ShopBalanceDateDtl oldDateDtl = list.get(0);
			if (!StatusEnums.BALANCE.getStatus().equals(oldDateDtl.getStatus())) {
				errorMsg = "历史费用重算失败,专柜" + counterNo + "重算结算月" + oldSettleMonth + "未结算";
				throw new ManagerException(errorMsg);
			}

			//结算月的结算期如果有多条 就取一条没有生成结算单的 结算期，如果多个结算期都结算，那么重算失败
			ShopBalanceDateDtl newDateDtl = new ShopBalanceDateDtl();
			Query newQuery = Q.where("counterNo", counterNo).and("settleMonth", newSettleMonth);
			List<ShopBalanceDateDtl> newList = shopBalanceDateDtlManager.selectByParams(newQuery);
			for (ShopBalanceDateDtl dateDtl : newList) {
				if (StatusEnums.EFFECTIVE.getStatus().equals(dateDtl.getStatus())
						|| StatusEnums.GENERATE_COST.getStatus().equals(dateDtl.getStatus())) {
					newDateDtl = dateDtl;
					break;
				}
			}

			// 没有获取到能重算的新结算期，
			if (!CommonUtil.hasValue(newDateDtl.getId())) {
				errorMsg = "费用重算失败,专柜" + counterNo + "结算月" + newSettleMonth + "未获取到有效结算期";
				throw new ManagerException(errorMsg);
			}
			
			try {
				counterCostManager.generateHisCounterCost(oldDateDtl, newDateDtl);
			} catch (Exception e) {
				errorMsg = "费用重算失败:" + oldDateDtl.getId() + "-" + newDateDtl.getId() + e.getMessage();
				logger.error(errorMsg);
				throw new ManagerException(e.getMessage(), e);
			}
		}
	}
}
