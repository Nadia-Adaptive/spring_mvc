package com.weareadaptive.auction.response;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseBuilder {
    public static ResponseEntity<Response> respond(final HttpStatus httpStatus, final ResponseStatus responseStatus) {
        return ResponseEntity.status(httpStatus).body(new Response<>(responseStatus.getMessage()));
    }

    public static <T> ResponseEntity<ResponseBody> respondWithBody(final HttpStatus httpStatus,
                                                                   final ResponseStatus responseStatus, final T body) {
        return ResponseEntity.status(httpStatus).body(new ResponseBody<>(responseStatus.getMessage(), body));
    }

    public static Response badCredentials() {
        return new Response(ResponseStatus.BAD_CREDENTIALS.getMessage());
    }

    public static Response notFound() {
        return new Response(ResponseStatus.NOT_FOUND.getMessage());
    }

    public static Response badRequest() {
        return new Response(ResponseStatus.BAD_REQUEST.getMessage());
    }

    public static Response forbidden() {
        return new Response(ResponseStatus.FORBIDDEN.getMessage());
    }

    public static Response unauthorized() {
        return new Response(ResponseStatus.UNAUTHORIZED.getMessage());
    }

    public static ResponseEntity<Response> ok() {
        return respond(HttpStatus.OK, ResponseStatus.OK);
    }

    public static ResponseEntity<Response> created() {
        return respond(HttpStatus.CREATED, ResponseStatus.CREATED);
    }
    public static <T> ResponseEntity<ResponseBody> ok(final T body) {
        return respondWithBody(HttpStatus.OK, ResponseStatus.OK, body);
    }
}
