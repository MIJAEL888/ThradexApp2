package org.thradex.config;


import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

@Order(1)
public class MvcWebInitializer  extends
AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] {MvcConfiguration.class, RootConfig.class, SecurityConfiguration.class};
	}
	
	@Override
	protected Class<?>[] getServletConfigClasses() {
		//	return new Class[] { MvcConfiguration.class};
		return null;
	}
	
	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

}