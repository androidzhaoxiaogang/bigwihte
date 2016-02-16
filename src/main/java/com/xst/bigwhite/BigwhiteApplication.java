package com.xst.bigwhite;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
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
/* @EnableScheduling */
@ComponentScan(basePackages = "com.xst.bigwhite")
public class BigwhiteApplication {

	private static final Logger log = LoggerFactory.getLogger(BigwhiteApplication.class);

	@Bean
	public Docket bigwhiteApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("bigwhite-api").apiInfo(apiInfo()).select()
				.paths(bigwhitePaths()).build().pathMapping("/").enableUrlTemplating(true);
	}

	@Autowired
	private TypeResolver typeResolver;

	private Predicate<String> bigwhitePaths() {
		return or(regex("/api/account.*"), regex("/api/device.*"), regex("/api/conference.*"), regex("/api/upload.*"));
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("bigwhite API").description("bigwhite")
				.termsOfServiceUrl("http://10.100.64.102:8080").contact("bigwhite")
				.license("Apache License Version 2.0")
				.licenseUrl("https://github.com/springfox/springfox/blob/master/LICENSE").version("1.0").build();
	}

	//
	// curl -X POST -vu android-bookmarks:123456
	// http://localhost:8080/oauth/token -H "Accept: application/json" -d
	// "password=password&username=jlong&grant_type=password&scope=write&client_secret=123456&client_id=android-bookmarks"
	// curl -v POST http://127.0.0.1:8080/tags --data "tags=cows,dogs" -H
	// "Authorization: Bearer 66953496-fc5b-44d0-9210-b0521863ffcb"
	// CORS
	@Bean
	FilterRegistrationBean corsFilter(@Value("${tagit.origin:http://localhost:9000}") String origin) {
		return new FilterRegistrationBean(new Filter() {
			public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
					throws IOException, ServletException {
				HttpServletRequest request = (HttpServletRequest) req;
				HttpServletResponse response = (HttpServletResponse) res;
				String method = request.getMethod();
				// this origin value could just as easily have come from a
				// database
				response.setHeader("Access-Control-Allow-Origin", origin);
				response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
				response.setHeader("Access-Control-Max-Age", Long.toString(60 * 60));
				response.setHeader("Access-Control-Allow-Credentials", "true");
				response.setHeader("Access-Control-Allow-Headers",
						"Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization");
				if ("OPTIONS".equals(method)) {
					response.setStatus(HttpStatus.OK.value());
				} else {
					chain.doFilter(req, res);
				}
			}

			public void init(FilterConfig filterConfig) {
			}

			public void destroy() {
			}
		});
	}

	public static void main(String[] args) {
		SpringApplication.run(BigwhiteApplication.class, args);

	}

}
