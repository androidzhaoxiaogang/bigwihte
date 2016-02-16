package com.xst.bigwhite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
public class OAuth2ServerConfig {
	
	private static final String RESOURCE_ID = "bigmax";
	
	@Configuration
	@EnableResourceServer
	protected static class ResourceServerConfiguration extends
			ResourceServerConfigurerAdapter {

		@Override
		public void configure(ResourceServerSecurityConfigurer resources) {
			// @formatter:off
			resources
				.resourceId(RESOURCE_ID);
			// @formatter:on
		}

		@Override
		public void configure(HttpSecurity http) throws Exception {
			// @formatter:off
			http
				.authorizeRequests()
					.antMatchers("/users").hasRole("ADMIN")
					//.antMatchers("/api/**").authenticated();
					.antMatchers("/api/**").permitAll();
			// @formatter:on
		}

	}
	
	
	@Configuration
	@EnableAuthorizationServer
	public static class AuthorizationServerConfiguration
		extends AuthorizationServerConfigurerAdapter {
			
		private TokenStore tokenStore = new InMemoryTokenStore();
		
		@Autowired
		private AuthenticationManager authenticationManager;
		
		@Autowired
		private CustomUserDetailsService userDetailsService;

		@Override
		public void configure(final ClientDetailsServiceConfigurer clients)
				throws Exception {
			clients.inMemory()
	        	.withClient("clientapp")
	        	.secret("123456")
	        	.authorizedGrantTypes("authorization_code", "refresh_token","password")
	        	.scopes("openid","read", "write")
	        	.resourceIds(RESOURCE_ID);
		}
	
		@Override
		public void configure(AuthorizationServerSecurityConfigurer oauthServer)
				throws Exception {
			oauthServer
				.tokenKeyAccess("permitAll()")
				.checkTokenAccess("isAuthenticated()");
		}
		
		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints)
				throws Exception {
			endpoints
					.tokenStore(this.tokenStore)
					.authenticationManager(authenticationManager)
					.userDetailsService(userDetailsService)
					.accessTokenConverter(defaultAccessTokenConverter());
		}
		
		@Bean
		public DefaultAccessTokenConverter defaultAccessTokenConverter() {
			return new DefaultAccessTokenConverter();
		}
		
		@Bean
		@Primary
		public DefaultTokenServices tokenServices() {
			DefaultTokenServices tokenServices = new DefaultTokenServices();
			tokenServices.setSupportRefreshToken(true);
			tokenServices.setTokenStore(this.tokenStore);
			return tokenServices;
		}


	}

}