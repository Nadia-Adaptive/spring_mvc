package com.weareadaptive.auction.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weareadaptive.auction.authentication.AuthenticationController;
import com.weareadaptive.auction.response.ResponseBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AccessDeniedResponseHandler implements AccessDeniedHandler {
    Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Override
    public void handle(final HttpServletRequest request, final HttpServletResponse response,
                       final AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper map = new ObjectMapper();
        map.writeValue(response.getOutputStream(), ResponseBuilder.forbidden());
       response.flushBuffer();

       logger.warn("Unauthorised user attempted to access admin route.");
    }
}
