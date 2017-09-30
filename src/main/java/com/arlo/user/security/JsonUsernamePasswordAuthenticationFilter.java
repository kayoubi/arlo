package com.arlo.user.security;

import com.arlo.user.domain.Account;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.Supplier;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

// custom filter to extract username/password form JSON request opposed to spring's default form requests parameters
public class JsonUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final String USERNAME_KEY = "username";
    private final String PASSWORD_KEY = "password";

    JsonUsernamePasswordAuthenticationFilter(final AuthenticationManager authenticationManager,
                                             final AuthenticationSuccessHandler authenticationSuccessHandler,
                                             final AuthenticationFailureHandler authenticationFailureHandler) {
        this.setAuthenticationManager(authenticationManager);
        this.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        this.setAuthenticationFailureHandler(authenticationFailureHandler);
    }

    @Override
    protected String obtainUsername(final HttpServletRequest request) {
        return getRequestData(request, USERNAME_KEY, () -> super.obtainUsername(request));
    }

    @Override
    protected String obtainPassword(final HttpServletRequest request) {
        return getRequestData(request, PASSWORD_KEY, () -> super.obtainPassword(request));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (isJsonRequest(request)) {
            try {
                final Account account = new ObjectMapper().readValue(request.getInputStream(), Account.class);
                request.setAttribute(USERNAME_KEY, account.username);
                request.setAttribute(PASSWORD_KEY, account.password);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return super.attemptAuthentication(request, response);
    }

    private boolean isJsonRequest(final HttpServletRequest request) {
        return APPLICATION_JSON_VALUE.equals(request.getHeader(CONTENT_TYPE));
    }

    private String getRequestData(final HttpServletRequest request, final String key, final Supplier<String> defaultData) {
        return isJsonRequest(request) ? (String)request.getAttribute(key) : defaultData.get();
    }
}
