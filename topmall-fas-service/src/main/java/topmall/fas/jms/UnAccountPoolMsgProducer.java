package topmall.fas.jms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.Destination;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import topmall.common.jms.BaseMessageProducer;
import topmall.common.jms.BillPoolMsgProducerInterface;
import topmall.common.model.UnAccountBillPool;
import topmall.framework.core.SpringContext;

public class UnAccountPoolMsgProducer implements BillPoolMsgProducerInterface {

	protected Log logger = LogFactory.getLog(getClass());

	@Override
	public <T extends UnAccountBillPool> void send(List<T> list) {
		for (UnAccountBillPool dto : list) {
			BaseMessageProducer producer = getProducer(dto);
			producer.send(dto);
			logger.info("send to mq " + dto);
		}
	}

	@Override
	public <T extends UnAccountBillPool> void send(T dto) {
		BaseMessageProducer producer = getProducer(dto);
		producer.send(dto);
		logger.info("send to mq  " + dto);
	}

	static Map<String, BaseMessageProducer> producerMap = new HashMap<>();

	@Override
	public BaseMessageProducer getProducer(UnAccountBillPool dto) {
		BaseMessageProducer messageProducer = producerMap.get("billPoolQueueFAS");
		if (messageProducer != null) {
			return messageProducer;
		}
		synchronized (this) {
			messageProducer = producerMap.get("billPoolQueueFAS");
			if (messageProducer != null) {
				return messageProducer;
			}
			Destination queue = null;
			try {
				queue = (Destination) SpringContext.getBean("billPoolQueueFAS");
			} catch (NoSuchBeanDefinitionException egnoreEx) {

			}
			messageProducer = new BaseMessageProducer(queue);
			producerMap.put("billPoolQueueFAS", messageProducer);
		}
		return messageProducer;
	}

	public static void main(String[] args) {
		//		for (int i = 0; i < 100; i++) {
		//			System.out.println( (int)(Math.random()*10%4) );
		//		}
		System.out.println("Z".substring("Z".length() - 1));
	}

}
