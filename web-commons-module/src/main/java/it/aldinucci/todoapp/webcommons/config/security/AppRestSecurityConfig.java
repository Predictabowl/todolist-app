package it.aldinucci.todoapp.webcommons.config.security;

import static it.aldinucci.todoapp.webcommons.config.AppBaseUrls.BASE_REST_URL;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@Order(value = 1)
public class AppRestSecurityConfig extends WebSecurityConfigurerAdapter {
	

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			
			.antMatcher(BASE_REST_URL+"/**")
			// send csrf token back as a cookie for a REST GET 
			.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		.and()
//			.csrf().disable()
//			.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
//			.authorizeRequests().anyRequest().permitAll();
//			.authorizeRequests().regexMatchers(HttpMethod.GET, ".*").permitAll()
//		.and()
			.authorizeRequests().anyRequest().authenticated()
		.and()
			.httpBasic();
//		.and()
//			.authorizeHttpRequests((authorize) -> authorize
//					.antMatchers(HttpMethod.POST, BASE_REST_URL+"/task/create")
//						.access(new NewTaskAuthorization2(loadUser))
//					);
	}
	
//	@Bean
//	public SecurityFilterChain web(HttpSecurity http) throws AuthenticationException{
//		http
//			.authorizeHttpRequests(null)
//	}
	
	
	
}
