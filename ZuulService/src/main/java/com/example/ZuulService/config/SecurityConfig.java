package com.example.ZuulService.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.ZuulService.security.JwtTokenAuthenticationFilter;
import com.netflix.ribbon.proxy.annotation.Http.HttpMethod;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfiguration {

    @Autowired
    private JwtConfig jwtConfig;

    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().exceptionHandling().authenticationEntryPoint((req, rsp, e)-> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and().addFilterAfter(new JwtTokenAuthenticationFilter(jwtConfig), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                	.antMatchers(HttpMethod.POST, jwtConfig.getUri()).permitAll()
                	.antMatchers("/gallery"+"/admin/**").hasRole("ADMIN")
                	.anyRequest().authenticated();
    }
    
    @Bean
    public JwtConfig jwtConfig() {
    	return new JwtConfig();
    }
}
