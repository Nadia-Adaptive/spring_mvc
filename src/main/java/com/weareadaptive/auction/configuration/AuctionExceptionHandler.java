package com.weareadaptive.auction.configuration;

import com.weareadaptive.auction.Response;
import com.weareadaptive.auction.ResponseStatus;
import com.weareadaptive.auction.model.BusinessException;
import com.weareadaptive.auction.model.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger logger = LoggerFactory.getLogger(AuctionExceptionHandler.class);

    public AuctionExceptionHandler() {
        headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
    }

    @ExceptionHandler(value = {BusinessException.class, IllegalArgumentException.class})
    protected ResponseEntity<Object> handleBadRequestException(final RuntimeException ex, final WebRequest request) {
        logException(ResponseStatus.BAD_REQUEST, ex);
        return handleExceptionInternal(ex, createJSONMessage(ResponseStatus.BAD_REQUEST), headers,
                HttpStatus.BAD_REQUEST, request);
    }


    @ExceptionHandler(value = {NotFoundException.class})
    protected ResponseEntity<Object> handleNotFoundException(final RuntimeException ex, final WebRequest request) {
        logException(ResponseStatus.NOT_FOUND, ex);
        return handleExceptionInternal(ex, createJSONMessage(ResponseStatus.NOT_FOUND), headers,
                HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {UsernameNotFoundException.class, BadCredentialsException.class})
    protected ResponseEntity<Object> handleBadCredentialsException(final RuntimeException ex,
                                                                   final WebRequest request) {
        logException(ResponseStatus.BAD_CREDENTIALS, ex);
        return handleExceptionInternal(ex, createJSONMessage(ResponseStatus.BAD_CREDENTIALS), headers,
                HttpStatus.BAD_REQUEST, request);
    }

    private Response createJSONMessage(final ResponseStatus errorMessage) {
        return new Response(errorMessage.getMessage());
    }

    private void logException(final ResponseStatus status, final Exception ex) {
        logger.error("Exception thrown: " + status.name() + " - " + ex.getMessage());
    }
}
