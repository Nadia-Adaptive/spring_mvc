package com.weareadaptive.auction.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weareadaptive.auction.ResponseStatus;
import com.weareadaptive.auction.Response;
import com.weareadaptive.auction.authentication.AuthenticationController;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UnauthenticatedHandler implements AuthenticationFailureHandler {
    Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Override
    public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response,
                                        final AuthenticationException exception) throws IOException, ServletException {
        System.out.println(exception);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper map = new ObjectMapper();
        map.writeValue(response.getOutputStream(), new Response(ResponseStatus.UNAUTHORIZED.getMessage()));

        logger.warn("Unauthenticated user attempted to access a protected route");
    }
}
