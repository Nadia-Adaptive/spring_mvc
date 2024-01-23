package com.weareadaptive.auction;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.ResponseUtil.createResponse;

public class ResponseUtilTest {
    @Test
    void createResponse_ValidParameters_ReturnResponse(){
        final var response = createResponse(HttpStatus.OK, "test");

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), "{\"message\": \"test\"}");
    }
}
