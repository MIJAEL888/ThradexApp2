package org.thradex.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
@ComponentScan(basePackages = "org.thradex")
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
	
//	@Autowired
//	private DataSource dataSource;

	@Autowired
	private UserDetailsService customUserDetailsService;

//	  @Bean
//	    public UserDetailsService customUserDetailsService() {
//	        return new CustomUserDetailsService();
//	    }
//	  
	@Bean
	public DaoAuthenticationProvider authProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(customUserDetailsService);
		authProvider.setPasswordEncoder(new Md5PasswordEncoder());
		return authProvider;
	}
	
	
	  @Override
	    public void configure(AuthenticationManagerBuilder auth) throws Exception {
	        auth
	          .authenticationProvider(authProvider());
	        CharacterEncodingFilter filter = new CharacterEncodingFilter();
	        filter.setEncoding("UTF-8");
	        filter.setForceEncoding(true);
	    }

	    @Bean 
	    @Override
	    public AuthenticationManager authenticationManagerBean() throws Exception {
	         return super.authenticationManagerBean();
	    }
	
	/*
	 * Configuracion de HTTPSecurity (HTTPS)
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable() //authenticationProvider(authProvider())
			.authorizeRequests()
				//.antMatchers(HttpMethod.OPTIONS,"/").permitAll()
				.antMatchers("/bandeja/**").hasAnyRole("NOC_R","NOC_U","NOC_D","NOC_IR","NOC_CNA","NOC_EMB")
				.antMatchers("/rrhh/**").hasAnyRole("RH_R","RH")
				.antMatchers("/dash/welcome").permitAll()//.hasAnyRole("NOC_R","NOC_U","NOC_D","NOC_IR","NOC_CNA","NOC_EMB")
				.antMatchers("/auth/login").permitAll()
				.antMatchers("/auth/logout").permitAll()
				.antMatchers("/user/activation/**").permitAll()
				.anyRequest().authenticated()
				.and()
	        .requiresChannel()
		        .anyRequest()
		        .requiresSecure()
				.and()
			.formLogin()
				.usernameParameter("j_username") // BY DEFAULT IS username!!!
		        .passwordParameter("j_password") // BY DEFAULT IS password!!!
		        .loginProcessingUrl("/j_spring_security_check")
				.loginPage("/")
				.defaultSuccessUrl("/shift/getPage")
				.failureUrl("/auth/login?error=true")
				.permitAll()
				.and()
			.logout()
				.logoutUrl("/auth/logout")
				.invalidateHttpSession(true)
				.logoutSuccessUrl("/auth/login")
				.permitAll()
				.and()
			.sessionManagement()
				.maximumSessions(1)
				.expiredUrl("/auth/expiredSession");
	}
	
	@Override
    public void configure(WebSecurity web) throws Exception {
        web
            .ignoring()
                .antMatchers("/static/**");
    }

}
