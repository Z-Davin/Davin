package topmall.fas.api.scheduler;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import tomall.fas.api.IUnAccountBillSchedulerApi;
import topmall.common.manager.UnAccountBillPoolManager;
import topmall.common.model.UnAccountBillPool;
import topmall.framework.schedule.AbstractSchedulerApiImpl;

import com.alibaba.dubbo.config.annotation.Service;

@Service
public class UnAccountBillSchedulerApi extends AbstractSchedulerApiImpl implements IUnAccountBillSchedulerApi {

	@Autowired
	private UnAccountBillPoolManager unAccountBillPoolManager;

	@Override
	protected void runJobDetail(Map<String, Object> param) {
		String sourceSystem=(String)param.get("sourceSystem");
		List<UnAccountBillPool> billList = unAccountBillPoolManager.findBills(null,sourceSystem);
		if (billList == null || billList.isEmpty()) {
			return;
		}
		//每个消息独立的事务 即使一个消息失败了 不影响下一个
		for (UnAccountBillPool unAccountBillPool : billList) {
			try {
				unAccountBillPoolManager.send(unAccountBillPool);
			} catch (Exception e) {
				log.error(unAccountBillPool+" sendToMQ error", e);
			}
		}
		
	}

	@Override
	public String getJobName() {
		return "异步任务生产者";
	}

	 

}
