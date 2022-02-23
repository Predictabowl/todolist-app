package it.aldinucci.todoapp.webcommons.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import it.aldinucci.todoapp.webcommons.config.AppBaseUrls;

@Configuration
@EnableWebSecurity
@Order(value = 1)
public class AppRestSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
			.antMatcher(AppBaseUrls.BASE_REST_URL+"/**")
			// send csrf token back as a cookie for a REST GET 
//			.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//		.and()
			.csrf().disable()
//			.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
//			.authorizeRequests().anyRequest().permitAll();
//			.authorizeRequests().regexMatchers(HttpMethod.GET, ".*").permitAll()
//		.and()
			.authorizeRequests().anyRequest().authenticated()
		.and()
			.httpBasic();
	}
	
}
