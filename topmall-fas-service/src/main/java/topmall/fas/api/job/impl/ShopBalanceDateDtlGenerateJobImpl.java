package topmall.fas.api.job.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import tomall.fas.api.job.IShopBalanceDateDtlGenerateJob;
import topmall.fas.manager.IShopBalanceDateDtlManager;
import topmall.framework.schedule.AbstractSchedulerApiImpl;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * 生成结算日期设置明细
 * @author Administrator
 */
@Service
public class ShopBalanceDateDtlGenerateJobImpl extends AbstractSchedulerApiImpl implements
		IShopBalanceDateDtlGenerateJob {
	@Autowired
	private IShopBalanceDateDtlManager shopBalanceDateDtlManager;
	
	@Override
	protected void runJobDetail(Map<String, Object> param) {
		shopBalanceDateDtlManager.generateShopBalanceDateDtlAuto();
	}

	@Override
	public String getJobName() {
		return "IShopBalanceDateDtlGenerateJob";
	}
}
