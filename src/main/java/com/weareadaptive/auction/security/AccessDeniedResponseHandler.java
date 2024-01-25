package com.weareadaptive.auction.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weareadaptive.auction.ErrorMessage;
import com.weareadaptive.auction.ResponseError;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jdk.jfr.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.header.Header;
import org.springframework.security.web.server.header.XXssProtectionServerHttpHeadersWriter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component("AccessDeniedResponseHandler")
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
