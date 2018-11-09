package topmall.fas.jms;

 
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;

import topmall.common.jms.BillPoolMsgConsumerInterface;
import topmall.common.jms.handler.BaseBillPoolHandler;
import topmall.common.manager.UnAccountBillPoolManager;
import topmall.common.model.UnAccountBillPool;
import topmall.fas.jms.handler.BillPoolHandlerFactory;
import topmall.framework.exception.TopmallException;
import cn.mercury.utils.JsonUtils;

public class BillPoolMessageConsumer implements BillPoolMsgConsumerInterface {
	
	protected Logger logger =LoggerFactory.getLogger(BillPoolMessageConsumer.class);
	
	@Resource
	private UnAccountBillPoolManager unAccountBillPoolManager;
	
	@JmsListener(destination = "${activemq.topmall.billPool.queue}_FAS")
	public void receiveQueueZ(String msg) {
			UnAccountBillPool billPool = JsonUtils.fromJson(msg,UnAccountBillPool.class);
			processMessage(billPool);
	}

	
	@Override
	public void processMessage(UnAccountBillPool unAccountBillPool) {
		BaseBillPoolHandler handler = BillPoolHandlerFactory.getHandler(unAccountBillPool);
		if(handler==null){
			throw new TopmallException("001", "暂不支持此异步任务");
		}
		handler.beforeProcess();
		handler.process();
	}

	@Override
	public void consumeOneMessage(String parentAccountBillId, String shardingFlag) throws Exception {
		UnAccountBillPool billPool=new UnAccountBillPool();
		billPool.setId(parentAccountBillId);
		billPool.setShardingFlag(shardingFlag);
		UnAccountBillPool  unAccountBillPool= unAccountBillPoolManager.findByUnique(billPool);
		if(unAccountBillPool!=null){
			unAccountBillPool.setQueueStatus(1);
			int updateFlag = unAccountBillPoolManager.update(unAccountBillPool);
			if(updateFlag>0){
				processMessage(unAccountBillPool);
			}else{
				logger.info("已处理,不需要重复:{}",unAccountBillPool);
			}
		}
		
	}
	
	public static String getErrorMsg(String msg){
		if(StringUtils.isNotEmpty(msg)){
			if(msg.length()>500){
				msg = msg.substring(0, 500);
			}
		}
		return msg;
	}

}
