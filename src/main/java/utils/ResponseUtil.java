package utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {
    public static ResponseEntity createResponse(final HttpStatus status, final String message){
        return ResponseEntity.status(status).body("{\"message\": \"%s\"}".formatted(message));
    }
}
