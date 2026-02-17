package com.cardoffers.oms.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class ErrorResponseTest {

    @Test
    void shouldCreateErrorResponseWithAllFields_whenUsingBuilder() {
        Map<String, Object> details = new HashMap<>();
        details.put("key", "value");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(400)
                .error("Bad Request")
                .message("Invalid request parameters")
                .path("/api/test")
                .details(details)
                .build();

        assertNotNull(errorResponse);
        assertEquals(400, errorResponse.getStatus());
        assertEquals("Bad Request", errorResponse.getError());
        assertEquals("Invalid request parameters", errorResponse.getMessage());
        assertEquals("/api/test", errorResponse.getPath());
        assertEquals(details, errorResponse.getDetails());
    }

    @Test
    void shouldCreateErrorResponseWithDefaultConstructor_whenSettingFields() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(404);
        errorResponse.setError("Not Found");
        errorResponse.setMessage("Resource not found");
        errorResponse.setPath("/api/resource");
        errorResponse.setDetails(null);

        assertNotNull(errorResponse);
        assertEquals(404, errorResponse.getStatus());
        assertEquals("Not Found", errorResponse.getError());
        assertEquals("Resource not found", errorResponse.getMessage());
        assertEquals("/api/resource", errorResponse.getPath());
        assertNull(errorResponse.getDetails());
    }

    @Test
    void shouldSetDetails_whenProvided() {
        Map<String, Object> details = new HashMap<>();
        details.put("errorCode", "ERR123");

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setDetails(details);

        assertNotNull(errorResponse.getDetails());
        assertEquals(1, errorResponse.getDetails().size());
        assertEquals("ERR123", errorResponse.getDetails().get("errorCode"));
    }

    @Test
    void shouldNotThrowException_whenUsingValidFields() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(200);
        errorResponse.setError("OK");
        errorResponse.setMessage("Request was successful");
        errorResponse.setPath("/api/success");
        errorResponse.setDetails(new HashMap<>());

        assertDoesNotThrow(() -> {
            assertEquals(200, errorResponse.getStatus());
        });
    }
    
    @Test
    void shouldHandleNullTimestampGracefully() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(500);
        errorResponse.setError("Internal Server Error");
        errorResponse.setMessage("An unexpected error occurred");
        errorResponse.setPath("/api/error");
        
        assertNull(errorResponse.getTimestamp());
        assertEquals(500, errorResponse.getStatus());
        assertEquals("Internal Server Error", errorResponse.getError());
    }
    
    @Test
    void shouldCreateErrorResponseWithEmptyDetails_whenNoDetailsProvided() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(403);
        errorResponse.setError("Forbidden");
        errorResponse.setMessage("Access denied");
        errorResponse.setPath("/api/forbidden");
        errorResponse.setDetails(new HashMap<>());

        assertNotNull(errorResponse.getDetails());
        assertTrue(errorResponse.getDetails().isEmpty());
    }
}