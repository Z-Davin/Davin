package topmall.fas.manager;

import topmall.fas.model.MallBalanceDateDtl;
import topmall.fas.model.MallPay;
import java.util.List;
import cn.mercury.basic.query.Query;
import cn.mercury.manager.IManager;

public interface IMallPayManager extends IManager<MallPay,String>{
    void generateMallPayData(MallBalanceDateDtl mallBalanceDateDtl);
    
    
	/** 合计
	 * @param q
	 * @return
	 */
	public List<MallPay> queryConditionSum(Query q);
}







