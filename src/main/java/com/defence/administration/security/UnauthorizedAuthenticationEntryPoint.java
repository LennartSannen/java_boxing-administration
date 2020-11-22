package com.defence.administration.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This is invoked when user tries to access a secured REST resource without supplying any credentials
 * Application sends a 401 Unauthorized response because there is no 'login page' to redirect to.
 * @author snsam
 * Created on Mar 28, 2018
 */
@Component
public class UnauthorizedAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }
}