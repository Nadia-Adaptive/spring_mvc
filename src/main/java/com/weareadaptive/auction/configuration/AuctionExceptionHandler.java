package com.weareadaptive.auction.configuration;

import com.weareadaptive.auction.ErrorMessage;
import com.weareadaptive.auction.ResponseError;
import com.weareadaptive.auction.model.BusinessException;
import com.weareadaptive.auction.model.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.rmi.AccessException;

@RestControllerAdvice
public class AuctionExceptionHandler extends ResponseEntityExceptionHandler {
    private final HttpHeaders headers;

    public AuctionExceptionHandler() {
        headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
    }

    @ExceptionHandler(value = {BusinessException.class})
    protected ResponseEntity<Object> handleBadRequestException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, createJSONMessage(ErrorMessage.BAD_REQUEST), headers,
                HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    protected ResponseEntity<Object> handleNotFoundException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, createJSONMessage(ErrorMessage.NOT_FOUND), headers,
                HttpStatus.NOT_FOUND, request);
    }

//    @ExceptionHandler(value = {AccessDeniedException.class})
//    protected ResponseEntity<Object> handleAuthorizationException(RuntimeException ex, WebRequest request) {
//        return handleExceptionInternal(ex, createJSONMessage(ErrorMessage.FORBIDDEN), headers,
//                HttpStatus.FORBIDDEN, request);
//    }

    private ResponseError createJSONMessage(final ErrorMessage errorMessage) {
        return new ResponseError(errorMessage.getMessage());
    }
}
