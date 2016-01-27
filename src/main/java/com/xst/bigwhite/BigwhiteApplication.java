package com.xst.bigwhite;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.async.DeferredResult;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Predicate;
import com.xst.bigwhite.controllers.*;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableConfigurationProperties
@ComponentScan(basePackages ="com.xst.bigwhite")
public class BigwhiteApplication {

	private static final Logger log = LoggerFactory.getLogger(BigwhiteApplication.class);

	@Configuration
	  @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	  protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	    @Override
	    protected void configure(HttpSecurity http) throws Exception {
	      http.csrf().disable()
	        .httpBasic()
		      .and()
		        .authorizeRequests()
		          //.antMatchers("/index.html", "/home.html", "/login.html", "/").permitAll()
		          //.anyRequest().authenticated();
		        .anyRequest().permitAll();
	    }
	  }
	
	
	@Bean
    public Docket bigwhiteApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("bigwhite-api")
                .apiInfo(apiInfo())
                .select()
                .paths(bigwhitePaths())
                .build()
                .pathMapping("/")
                .enableUrlTemplating(true);
    }
	
	 @Autowired
	  private TypeResolver typeResolver;

	 
	
	 private Predicate<String> bigwhitePaths() {
	        return or(
	                regex("/api/account.*"),
	                regex("/api/device.*"),
	                regex("/api/upload.*")
	        );
	    }
	
	 private ApiInfo apiInfo() {
	        return new ApiInfoBuilder()
	                .title("bigwhite API")
	                .description("bigwhite")
	                .termsOfServiceUrl("http://10.100.64.102:8080")
	                .contact("bigwhite")
	                .license("Apache License Version 2.0")
	                .licenseUrl("https://github.com/springfox/springfox/blob/master/LICENSE")
	                .version("1.0")
	                .build();
	    }
	
    public static void main(String[] args) {
        SpringApplication.run(BigwhiteApplication.class, args);
        
    }
    
 
}
