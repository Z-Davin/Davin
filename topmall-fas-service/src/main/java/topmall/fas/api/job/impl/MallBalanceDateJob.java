package topmall.fas.api.job.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import tomall.fas.api.job.IMallBalanceDateJob;
import topmall.fas.manager.IMallBalanceDateDtlManager;
import topmall.framework.schedule.AbstractSchedulerApiImpl;

import com.alibaba.dubbo.config.annotation.Service;
@Service
public class MallBalanceDateJob extends AbstractSchedulerApiImpl implements IMallBalanceDateJob {

	@Autowired
	private IMallBalanceDateDtlManager manager;
	
	@Override
	
	protected void runJobDetail(Map<String, Object> param) {
		manager.generateMallBalanceDateDtl();
	}

	@Override
	public String getJobName() {
		return "生成物业结算期明细";
	}

}
