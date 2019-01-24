package org.thradex.config;

import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
//import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;
//import org.springframework.web.servlet.view.velocity.VelocityViewResolver;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

//@ComponentScan(basePackages = {"org.thradex.controller",
//"org.thradex.sis.service","org.thradex.sis.dao","org.thradex.sis.controller"})
//@ComponentScan(basePackages = { "org.thradex" }, excludeFilters = { 
//		@Filter(type = FilterType.ANNOTATION, value = Configuration.class) })

@EnableWebMvc  
@Configuration
@ComponentScan(basePackages = "org.thradex")
public class MvcConfiguration extends WebMvcConfigurerAdapter{
     
    @Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("/static/");
    }
 
    @Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
 
    @Bean
    static PropertySourcesPlaceholderConfigurer propertyPlaceHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(Include.NON_NULL);
        converter.setObjectMapper(objectMapper);
        converters.add(converter);
        super.configureMessageConverters(converters);
    }
    
//    @Bean
//    public HandlerExceptionResolver handlerExceptionResolver(){
//    	return new DefaultHandlerExceptionResolver();
//    }
//    @Bean
//	public UrlBasedViewResolver setupViewResolver() {
//		UrlBasedViewResolver resolver = new UrlBasedViewResolver();
//		resolver.setPrefix("/WEB-INF/pages/");
//		resolver.setSuffix(".jsp");
//		resolver.setViewClass(JstlView.class);
////		resolver.setViewNames(new String[] {"/jsp/*"});
//		resolver.setOrder(2);
//		return resolver;
//	}
    
    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver getMultipartResolver() {
    	CommonsMultipartResolver c = new CommonsMultipartResolver();
    	c.setMaxUploadSize(20971520); //20.0 Mb
//    	c.setResolveLazily(true);
        return c;
    }
 
//    @Bean(name = "messageSource")
//    public ReloadableResourceBundleMessageSource messageSource() {
//        ReloadableResourceBundleMessageSource resource = new ReloadableResourceBundleMessageSource();
//        resource.setBasename("classpath:messages");
//        resource.setDefaultEncoding("UTF-8");
//        return resource;
//    }
    
    @Bean(name = "messageAccessor")
    public MessageSourceAccessor messageAccessor() {
    	MessageSourceAccessor resource = new MessageSourceAccessor(messageSource());
        return resource;
    }
    @Bean
    @Description("Thymeleaf template resolver serving HTML 5")
    public ServletContextTemplateResolver templateResolver() {
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
        templateResolver.setPrefix("/WEB-INF/pages/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setOrder(1);
        templateResolver.setCacheable(false);
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.addTemplateAlias("headTemplate", "html/fragments/head");
        templateResolver.addTemplateAlias("headerTemplate", "html/fragments/header");
        templateResolver.addTemplateAlias("menuTemplate", "html/fragments/menu");
        templateResolver.addTemplateAlias("scriptTemplate", "html/fragments/script");
        templateResolver.addTemplateAlias("modalTemplate", "html/fragments/modals");
        return templateResolver;
    }
    
    @Bean
    @Description("Thymeleaf template engine with Spring integration")
    public SpringTemplateEngine templateEngine() {
    	Set<IDialect> dialects = new HashSet<IDialect>();
    	dialects.add(springSecurityDialect());
    	
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setAdditionalDialects(dialects);
        return templateEngine;
    }
    
    @Bean
    public SpringSecurityDialect springSecurityDialect(){
        return new SpringSecurityDialect();
    }
    
    @Bean
    @Description("Thymeleaf view resolver")
    public ThymeleafViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setCharacterEncoding("UTF-8");
        viewResolver.setContentType("text/html; charset=UTF-8");
//        viewResolver.setViewNames(new String[] {"/html/*"});
        return viewResolver;
    }
    
    @Bean
    @Description("Spring message resolver")
    public ResourceBundleMessageSource messageSource() {  
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();  
        messageSource.setBasename("messages");  
        return messageSource;  
    }
    
    @Bean
    @Description("Spring message resolver")
    public ResourceBundleMessageSource messageSourceI18n() {  
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();  
        messageSource.setBasename("i18n/messages");  
        return messageSource;  
    }
    
    @Bean
    public JavaMailSenderImpl mailSender(){
    	Properties javaMailProperties = new Properties() ;
    	javaMailProperties.setProperty("mail.mime.charset", "UTF-8");
    	javaMailProperties.setProperty("mail.smtp.usert", "app.manager@thradex.com");
    	javaMailProperties.setProperty("mail.smtp.password", "m41l.m4n4g3r");
    	javaMailProperties.setProperty("mail.smtp.host", "smtp.gmail.com");
    	javaMailProperties.setProperty("mail.smtp.port", "587");
    	javaMailProperties.setProperty("mail.smtp.auth", "true");
    	javaMailProperties.setProperty("mail.smtp.starttls.enable", "true");
    	javaMailProperties.setProperty("mail.debug", "false");
    	JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.gmail.com");
//        javaMailSender.setUsername("app.manager@thradex.com");
        javaMailSender.setUsername("app.managerXXX@thradex.com");
        javaMailSender.setPassword("m41l.m4n4g3r");
        javaMailSender.setDefaultEncoding("UTF-8");
        javaMailSender.setJavaMailProperties(javaMailProperties);
        return javaMailSender;
    }
    
    @Bean
    public VelocityConfigurer velocityConfig() {
        VelocityConfigurer velocityConfigurer = new VelocityConfigurer();
        velocityConfigurer.setResourceLoaderPath("/WEB-INF/velocity/");
        return velocityConfigurer;
    }
    
    @Bean
    public VelocityEngineFactoryBean velocityEngine() {
//    	Properties properties = new Properties() ;
//    	properties.setProperty("resource.loader", "class");
//    	properties.setProperty("class.resource.loader.cache", "true");
//    	properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
//    	properties.setProperty("class.resource.loader.path", "classpath:/WEB-INF/velocity/");
    	VelocityEngineFactoryBean velocityEngineFactoryBean = new VelocityEngineFactoryBean();
    	velocityEngineFactoryBean.setResourceLoaderPath("/WEB-INF/velocity/");
//    	velocityEngineFactoryBean.setVelocityProperties(properties);
    	return velocityEngineFactoryBean;
    }
//    
//    @Bean
//    public VelocityViewResolver velocityViewResolver(){
//    	VelocityViewResolver velocityViewResolver = new VelocityViewResolver();
//    	velocityViewResolver.setPrefix("");
//    	velocityViewResolver.setSuffix(".vm");
//    	velocityViewResolver.setCache(true);
//    	velocityViewResolver.setOrder(2);
//    	velocityViewResolver.setExposeSpringMacroHelpers(true);
//    	velocityViewResolver.setExposePathVariables(true);
//    	return velocityViewResolver;
//    }
}