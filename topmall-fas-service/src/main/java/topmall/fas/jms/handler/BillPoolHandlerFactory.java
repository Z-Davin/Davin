package topmall.fas.jms.handler;

import topmall.common.enums.BillTypeEnums;
import topmall.common.jms.handler.BaseBillPoolHandler;
import topmall.common.model.UnAccountBillPool;

public class BillPoolHandlerFactory {

	public static BaseBillPoolHandler getHandler(UnAccountBillPool unAccountBillPool) {
		if (BillTypeEnums.GENERATE_COUNTER_COST.getRequestId().equals(unAccountBillPool.getBillType())
				|| BillTypeEnums.GENERATE_MALL_COST.getRequestId().equals(unAccountBillPool.getBillType())
				|| BillTypeEnums.GENERATE_HIS_COUNTER_COST.getRequestId().equals(unAccountBillPool.getBillType())) {
			return new CalcCostHandler(unAccountBillPool);
		} else {
			return null;
		}
	}
}
