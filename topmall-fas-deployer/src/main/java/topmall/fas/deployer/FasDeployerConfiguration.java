package topmall.fas.deployer;

import javax.jms.Destination;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import topmall.common.jms.BillPoolMsgConsumerInterface;
import topmall.common.jms.BillPoolMsgProducerInterface;
import topmall.fas.jms.BillPoolMessageConsumer;
import topmall.fas.jms.UnAccountPoolMsgProducer;

@Configuration
public class FasDeployerConfiguration {

	public FasDeployerConfiguration(){
		System.out.println("init FasDeployer...");
	}

	@Value("${activemq.topmall.billPool.queue}")
	private String queueName;
	
	@Bean(name = "billPoolMessageConsumer")
	@ConditionalOnExpression("${activemq.topmall.billPool.queue.consumer.enable}")
	public BillPoolMsgConsumerInterface consumer(){
		return new BillPoolMessageConsumer();
	}
	
	@Bean(name = "billPoolQueueFAS")
	@ConditionalOnExpression("${activemq.topmall.billPool.queue.producer.enable}")
	public Destination queueFAS() {
		return new ActiveMQQueue(queueName+"_FAS");
	}
	
	@Bean(name = "unAccountPoolMsgProducer")
	@ConditionalOnExpression("${activemq.topmall.billPool.queue.producer.enable}")
	public BillPoolMsgProducerInterface producer(){
		return new UnAccountPoolMsgProducer();
	}
}	
