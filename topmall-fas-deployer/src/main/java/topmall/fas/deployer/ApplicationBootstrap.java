package topmall.fas.deployer;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import topmall.framework.servicemodel.Bootstrap;

@SpringBootApplication
public class ApplicationBootstrap //extends Bootstrap
{

	public static void main(String[] args)   {
		Bootstrap.run(args, ApplicationBootstrap.class); 
	}
}
