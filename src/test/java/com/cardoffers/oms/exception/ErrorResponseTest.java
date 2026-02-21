package com.cardoffers.oms.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class ErrorResponseTest {

    @Test
    void shouldCreateErrorResponse_whenAllFieldsProvided() {
        Map<String, Object> details = new HashMap<>();
        details.put("key", "value");
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(404)
                .error("Not Found")
                .message("The requested resource was not found")
                .path("/api/resource")
                .details(details)
                .build();
        
        assertNotNull(errorResponse);
        assertEquals(404, errorResponse.getStatus());
        assertEquals("Not Found", errorResponse.getError());
        assertEquals("The requested resource was not found", errorResponse.getMessage());
        assertEquals("/api/resource", errorResponse.getPath());
        assertEquals(details, errorResponse.getDetails());
    }

    @Test
    void shouldSetErrorResponseTimestamp() {
        ErrorResponse errorResponse = new ErrorResponse();
        LocalDateTime now = LocalDateTime.now();
        errorResponse.setTimestamp(now);
        
        assertEquals(now, errorResponse.getTimestamp());
    }

    @Test
    void shouldSetErrorResponseStatus() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(400);
        
        assertEquals(400, errorResponse.getStatus());
    }

    @Test
    void shouldSetErrorResponseError() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError("Bad Request");
        
        assertEquals("Bad Request", errorResponse.getError());
    }

    @Test
    void shouldSetErrorResponseMessage() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Invalid request parameters");
        
        assertEquals("Invalid request parameters", errorResponse.getMessage());
    }

    @Test
    void shouldSetErrorResponsePath() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setPath("/api/resource/notfound");
        
        assertEquals("/api/resource/notfound", errorResponse.getPath());
    }

    @Test
    void shouldSetErrorResponseDetails() {
        ErrorResponse errorResponse = new ErrorResponse();
        Map<String, Object> details = new HashMap<>();
        details.put("field", "error message");
        errorResponse.setDetails(details);
        
        assertEquals(details, errorResponse.getDetails());
    }
}