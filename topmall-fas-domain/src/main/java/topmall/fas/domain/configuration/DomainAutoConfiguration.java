package topmall.fas.domain.configuration;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import topmall.common.repository.UnAccountBillPoolMapper;
import topmall.fas.repository.CounterCostRepository;



@Configuration
@MapperScan(basePackageClasses = {CounterCostRepository.class,UnAccountBillPoolMapper.class,})
@ComponentScan(basePackages = {
		"topmall.fas.manager.impl"
		,"topmall.fas.service.impl"
		,"topmall.common.manager.impl"
		,"topmall.common.service.impl"
		,"topmall.fas.service.report.impl"
		,"topmall.fas.manager.report.impl"
		})
public class DomainAutoConfiguration {
	
}
