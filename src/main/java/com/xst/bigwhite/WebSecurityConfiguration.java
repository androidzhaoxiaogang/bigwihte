package com.xst.bigwhite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.xst.bigwhite.daos.AccountRepository;

@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	//@Autowired
	//private AuthenticationManager authenticationManager;
	
	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
	 /*auth.inMemoryAuthentication()
			.withUser("user").password("userpwd").roles("USER")
			.and()
			.withUser("admin").password("adminpwd").roles("ADMIN")
			 FIXME : check_token api validates client credentials via basic authorization 
			.and()
			.withUser("soncrserv").password("soncrserv").roles("CLIENT");
		
		auth.parentAuthenticationManager(authenticationManager);*/
		
		auth.userDetailsService(userDetailsService);	
	}
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
			.formLogin().loginPage("/login").permitAll()
			.and()
				.logout().logoutSuccessUrl("/").permitAll()
			//.and()
				//.requestMatchers().antMatchers("/", "/login", "/oauth/token", "/oauth/authorize", "/oauth/confirm_access","")
			.and()
				//.authorizeRequests().anyRequest().authenticated();
			     .authorizeRequests().anyRequest().permitAll();
	}
}

