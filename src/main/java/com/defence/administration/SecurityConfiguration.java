package com.defence.administration;

import com.defence.administration.security.JwtAuthenticationFilter;
import com.defence.administration.security.JwtTokenUtil;
import com.defence.administration.security.UnauthorizedAuthenticationEntryPoint;
import com.defence.administration.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    UserService userService;

    @Autowired
    UnauthorizedAuthenticationEntryPoint unauthorizedAuthenticationEntryPoint;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().exceptionHandling()
                .authenticationEntryPoint(unauthorizedAuthenticationEntryPoint).and().authorizeRequests()
                .antMatchers("/login").permitAll().anyRequest().authenticated().and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenUtil, userService),
                        BasicAuthenticationFilter.class);
        http.csrf().disable();
        http.cors();
    }

}