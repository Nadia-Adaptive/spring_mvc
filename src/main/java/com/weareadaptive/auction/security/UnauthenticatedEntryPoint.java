package com.weareadaptive.auction.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weareadaptive.auction.ErrorMessage;
import com.weareadaptive.auction.ResponseError;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UnauthenticatedEntryPoint implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        System.out.println("siiiigh. please work");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper map = new ObjectMapper();
        map.writeValue(response.getOutputStream(), new ResponseError(ErrorMessage.UNAUTHORIZED.getMessage()));
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}
