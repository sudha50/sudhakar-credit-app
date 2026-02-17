package com.cardoffers.oms.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ErrorResponseTest {

    @Test
    void shouldCreateErrorResponseWithAllFields_whenUsingBuilder() {
        Map<String, Object> details = new HashMap<>();
        details.put("key", "value");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(404)
                .error("Not Found")
                .message("Resource not found")
                .path("/some/path")
                .details(details)
                .build();

        assertNotNull(errorResponse);
        assertEquals(404, errorResponse.getStatus());
        assertEquals("Not Found", errorResponse.getError());
        assertEquals("Resource not found", errorResponse.getMessage());
        assertEquals("/some/path", errorResponse.getPath());
        assertEquals(details, errorResponse.getDetails());
    }

    @Test
    void shouldDefaultFieldsToNull_whenUsingNoArgConstructor() {
        ErrorResponse errorResponse = new ErrorResponse();

        assertNull(errorResponse.getTimestamp());
        assertEquals(0, errorResponse.getStatus());
        assertNull(errorResponse.getError());
        assertNull(errorResponse.getMessage());
        assertNull(errorResponse.getPath());
        assertNull(errorResponse.getDetails());
    }

    @Test
    void shouldSetAndRetrieveFieldsCorrectly() {
        ErrorResponse errorResponse = new ErrorResponse();
        LocalDateTime timestamp = LocalDateTime.now();
        errorResponse.setTimestamp(timestamp);
        errorResponse.setStatus(500);
        errorResponse.setError("Internal Server Error");
        errorResponse.setMessage("An error occurred");
        errorResponse.setPath("/error");
        
        Map<String, Object> details = new HashMap<>();
        details.put("errorDetail", "Detail about the error");
        errorResponse.setDetails(details);

        assertEquals(timestamp, errorResponse.getTimestamp());
        assertEquals(500, errorResponse.getStatus());
        assertEquals("Internal Server Error", errorResponse.getError());
        assertEquals("An error occurred", errorResponse.getMessage());
        assertEquals("/error", errorResponse.getPath());
        assertEquals(details, errorResponse.getDetails());
    }

    @Test
    void shouldCreateErrorResponseWithoutDetails_whenUsingBuilder() {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(400)
                .error("Bad Request")
                .message("Invalid parameters")
                .path("/some/path")
                .build();

        assertNotNull(errorResponse);
        assertNull(errorResponse.getDetails());
    }
}