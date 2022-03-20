package it.aldinucci.todoapp.adapter.in.web.security.config;

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
			.authorizeRequests().antMatchers("/user/register/**").permitAll()
		.and()
			.authorizeRequests().antMatchers("/web/**").authenticated()
		.and()
			.formLogin().loginPage("/login").permitAll()
			.successHandler((request, response, authentication) -> response.sendRedirect("/web"))
		.and()
			.logout().permitAll();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
			.antMatchers("/css/**","/webjars/**");
	}
	
}
