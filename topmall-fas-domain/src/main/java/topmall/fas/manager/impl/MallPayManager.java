package topmall.fas.manager.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import topmall.fas.enums.StatusEnums;
import topmall.fas.manager.IMallPayManager;
import topmall.fas.model.MallBalanceDateDtl;
import topmall.fas.model.MallPay;
import topmall.fas.service.IMallPayService;
import topmall.fas.util.PublicConstans;
import topmall.framework.core.CodingRuleHelper;
import topmall.framework.manager.BaseManager;
import topmall.framework.service.IService;
import cn.mercury.basic.query.Query;

import com.alibaba.dubbo.common.utils.CollectionUtils;


@Service
public class MallPayManager extends BaseManager<MallPay,String> implements IMallPayManager{
    @Autowired
    private IMallPayService service;
    
    protected IService<MallPay,String> getService(){
        return service;
    }

	@Override
	public void generateMallPayData(MallBalanceDateDtl mallBalanceDateDtl) {
		Query query = Query.Where("mallNo", mallBalanceDateDtl.getMallNo())
				.and("shopNo",mallBalanceDateDtl.getShopNo())
				.and("settleStartDate", mallBalanceDateDtl.getSettleStartDate())
				.and("settleEndDate", mallBalanceDateDtl.getSettleEndDate())
				.and("settleMonth", mallBalanceDateDtl.getSettleMonth())
				.and("bunkGroupNo", mallBalanceDateDtl.getBunkGroupNo());
		//查询支付明细
		List<MallPay> mallPayList = service.selectMallPayList(query);
		//将数据插入到支付明细表中
		if (CollectionUtils.isNotEmpty(mallPayList)){
			String orderNo = mallPayList.get(0).getShopNo() 
					+ CodingRuleHelper.getBasicCoding(PublicConstans.COUNTER_COST_NO, mallPayList.get(0).getShopNo(), null);
			for (MallPay mallPay : mallPayList) {
				//将数据插入到支付明细表中
				mallPay.setId(generateId());
				mallPay.setUpdateTime(new Date());
				mallPay.setStatus(StatusEnums.EFFECTIVE.getStatus());
				mallPay.setMallPayAmount(mallPay.getPayAmount());
				if (null == mallPay.getSeqId()) {
					mallPay.setSeqId(orderNo);
				}
				service.insert(mallPay);
			}
		}
	}

	@Override
	public List<MallPay> queryConditionSum(Query q) {
		return service.queryConditionSum(q);
	}
	
	
    
}







