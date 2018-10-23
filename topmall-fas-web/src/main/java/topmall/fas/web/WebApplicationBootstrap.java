package topmall.fas.web;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

import topmall.framework.core.ApplicationBootstrap;
import topmall.framework.web.inspect.SecurityInterceptor;

@SpringBootApplication
@ComponentScan(basePackages = { "topmall.framework.web", "topmall.framework.web.controller", "topmall.framework.core",
		"topmall.fas.web.controller" })
@ServletComponentScan(basePackageClasses = { SecurityInterceptor.class })
public class WebApplicationBootstrap extends ApplicationBootstrap {

	public static void main(String[] args) {

		new WebApplicationBootstrap().run(args);
	}

}
