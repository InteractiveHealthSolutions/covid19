/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus;

import java.time.LocalDate;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@Configuration
@EnableWebSecurity
@EnableWebMvc
@EnableAsync
public class WebConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AuthenticationEntryPoint authEntryPoint;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
	registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH").allowedOrigins("*")
		.allowedHeaders("*");// .maxAge(-1);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
	WebMvcConfigurer.super.addInterceptors(registry);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	return encoder;
    }
    
    @Bean("threadPoolTaskExecutor")
    public TaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(1000);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("Async-");
        return executor;
    }

    /**
     * Provides In-memory authentication to test Swagger API. There is only one user
     * 'admin' and the password is calculed from date (day * month * year)
     * 
     * @param auth
     * @return
     * @throws Exception
     */
    public AuthenticationManagerBuilder getInMemoryAuthenticationService(AuthenticationManagerBuilder auth)
	    throws Exception {
	InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
	LocalDate date = LocalDate.now();
	String todaysPassword = "{noop}" + (date.getYear() * date.getMonthValue() * date.getDayOfMonth());
	manager.createUser(org.springframework.security.core.userdetails.User.withUsername("admin")
		.password(todaysPassword).roles("ADMIN").build());
	auth.userDetailsService(manager);
	return auth;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
	http.csrf().disable();
	http.authorizeRequests().anyRequest().authenticated();
	http.httpBasic().realmName(AuthenticationEntryPoint.AAHUNG_CORONAVIRUS_AUTH_REALM)
		.authenticationEntryPoint(authEntryPoint);
	http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	http.cors().and();
    }

    /**
     * Provides JDBC authentication to test Swagger API. User authentication and
     * roles are read from the database
     * 
     * @param auth
     * @return
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	// auth = getInMemoryAuthenticationService(auth);
	auth.jdbcAuthentication().usersByUsernameQuery(
		"SELECT username, password_hash as password, 'true' as enabled FROM users WHERE username = ? and voided = 0")
		.authoritiesByUsernameQuery(
			"SELECT u.username, 'ADMIN' as role FROM users u, user_role r WHERE u.username = ? and r.user_id = u.user_id")
		.dataSource(dataSource).passwordEncoder(bCryptPasswordEncoder);
    }
    
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
      configurer.setUseSuffixPatternMatch(false);
    }

}
