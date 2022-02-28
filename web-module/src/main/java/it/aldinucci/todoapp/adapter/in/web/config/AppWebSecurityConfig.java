package it.aldinucci.todoapp.adapter.in.web.config;

import static it.aldinucci.todoapp.webcommons.config.AppBaseURIs.BASE_WEB_URI;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class AppWebSecurityConfig extends WebSecurityConfigurerAdapter{

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers(BASE_WEB_URI+"/register").permitAll()
		.and()
			.authorizeRequests().anyRequest().authenticated()
		.and()
			.formLogin().loginPage("/login").permitAll()
		.and()
			.logout().permitAll();
//			.formLogin(form -> form.loginPage("/login").permitAll())
//			.logout();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
			.antMatchers("/css/**","/webjars/**");
	}
	
}
