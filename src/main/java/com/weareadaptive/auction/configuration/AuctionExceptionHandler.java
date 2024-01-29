package com.weareadaptive.auction.configuration;

import com.weareadaptive.auction.ResponseStatus;
import com.weareadaptive.auction.Response;
import com.weareadaptive.auction.model.BusinessException;
import com.weareadaptive.auction.model.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class AuctionExceptionHandler extends ResponseEntityExceptionHandler {
    private final HttpHeaders headers;

    public AuctionExceptionHandler() {
        headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
    }

    @ExceptionHandler(value = {BusinessException.class, IllegalArgumentException.class})
    protected ResponseEntity<Object> handleBadRequestException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, createJSONMessage(ResponseStatus.BAD_REQUEST), headers,
                HttpStatus.BAD_REQUEST, request);
    }


    @ExceptionHandler(value = {NotFoundException.class})
    protected ResponseEntity<Object> handleNotFoundException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, createJSONMessage(ResponseStatus.NOT_FOUND), headers,
                HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {UsernameNotFoundException.class, BadCredentialsException.class})
    protected ResponseEntity<Object> handleBadCredentialsException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, createJSONMessage(ResponseStatus.BAD_CREDENTIALS), headers,
                HttpStatus.BAD_REQUEST, request);
    }
    private Response createJSONMessage(final ResponseStatus errorMessage) {
        return new Response(errorMessage.getMessage());
    }
}
