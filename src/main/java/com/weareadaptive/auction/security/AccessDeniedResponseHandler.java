package com.weareadaptive.auction.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weareadaptive.auction.ErrorMessage;
import com.weareadaptive.auction.ResponseError;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AccessDeniedResponseHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper map = new ObjectMapper();
        map.writeValue(response.getOutputStream(), new ResponseError(ErrorMessage.FORBIDDEN.getMessage()));
       response.flushBuffer();
    }
}
