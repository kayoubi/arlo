package com.arlo.user.security;

import com.arlo.user.model.LoginResult;
import com.arlo.user.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConf extends WebSecurityConfigurerAdapter {
    private final AccountService accountService;

    public WebSecurityConf(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf()
                .disable()
            .formLogin()
                .loginProcessingUrl("/login")
            .and()
            .addFilterAt(
                new JsonUsernamePasswordAuthenticationFilter(
                    authenticationManagerBean(),
                    (request, response, auth) -> new ObjectMapper().writeValue(response.getWriter(), new LoginResult(true)),
                    (request, response, auth) -> new ObjectMapper().writeValue(response.getWriter(), new LoginResult(false))
                ), UsernamePasswordAuthenticationFilter.class)
            .authorizeRequests()
                .anyRequest()
                .permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(this.accountService)
            .passwordEncoder(new BCryptPasswordEncoder());
    }
}
