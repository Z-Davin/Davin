package topmall.fas.web.configuration;

import javax.servlet.Servlet;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import topmall.framework.web.configuration.AbstractWebMvcConfig;

@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass({ Servlet.class, DispatcherServlet.class, WebMvcConfigurerAdapter.class })
@Order(Ordered.LOWEST_PRECEDENCE)
@AutoConfigureAfter(DispatcherServletAutoConfiguration.class)
public class WebAutoConfig extends AbstractWebMvcConfig {
	public WebAutoConfig() {
		System.err.println("init web...");
	}
}
