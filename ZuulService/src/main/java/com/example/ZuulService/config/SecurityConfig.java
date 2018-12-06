package com.example.ZuulService.config;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfiguration {

    @Autowired
    private JwtConfig jwtConfig;

    protected void configure(HttpStatus http) throws Exception {
        http
                .csrf().disable()
                sessionManagement().sessionCreationPolicy()
    }
}
