package topmall.fas.domain.configuration;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import topmall.common.repository.UnAccountBillPoolMapper;

@Configuration
@MapperScan(basePackageClasses = { UnAccountBillPoolMapper.class})
@ComponentScan(basePackages = {
		"topmall.fas.service.impl"
		,"topmall.fas.manager.impl"
		,"topmall.common.service.impl"
		,"topmall.common.manager.impl"
		})
//@EnableConfigurationProperties({ FasSetting.class,CommonSetting.class})
public class FasAutoConfiguration {
	public FasAutoConfiguration() {
		System.out.println("init FAS Domain...");
	}

}
