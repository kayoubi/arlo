package com.arlo.user.security;

import com.arlo.user.model.LoginResult;
import com.arlo.user.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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
                    (request, response, auth) -> result(response, true),
                    (request, response, auth) -> result(response, false)
                ), UsernamePasswordAuthenticationFilter.class)
            .authorizeRequests()
                .anyRequest()
                .permitAll();
    }

    private void result(HttpServletResponse response, boolean success) {
        response.setStatus(success ? HttpStatus.OK.value() : HttpStatus.UNAUTHORIZED.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        try {
            new ObjectMapper().writeValue(response.getWriter(), new LoginResult(success));
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(this.accountService)
            .passwordEncoder(new BCryptPasswordEncoder());
    }
}
