package com.cardoffers.oms.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class ErrorResponseTest {

    @Test
    void shouldCreateErrorResponse_withAllFields() {
        Map<String, Object> details = new HashMap<>();
        details.put("key", "value");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(404)
                .error("Not Found")
                .message("The requested resource was not found.")
                .path("/api/resource")
                .details(details)
                .build();

        assertNotNull(errorResponse);
        assertEquals(404, errorResponse.getStatus());
        assertEquals("Not Found", errorResponse.getError());
        assertEquals("The requested resource was not found.", errorResponse.getMessage());
        assertEquals("/api/resource", errorResponse.getPath());
        assertEquals(details, errorResponse.getDetails());
    }

    @Test
    void shouldSetTimestamp_whenUsingSetter() {
        ErrorResponse errorResponse = new ErrorResponse();
        LocalDateTime now = LocalDateTime.now();
        errorResponse.setTimestamp(now);
        
        assertEquals(now, errorResponse.getTimestamp());
    }

    @Test
    void shouldReturnDefaultValues_whenNoArgsConstructorUsed() {
        ErrorResponse errorResponse = new ErrorResponse();

        assertNull(errorResponse.getTimestamp());
        assertEquals(0, errorResponse.getStatus());
        assertNull(errorResponse.getError());
        assertNull(errorResponse.getMessage());
        assertNull(errorResponse.getPath());
        assertNull(errorResponse.getDetails());
    }

    @Test
    void shouldBuildErrorResponseWithBuilderPattern() {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(500)
                .error("Internal Server Error")
                .message("An unexpected error occurred.")
                .path("/api/unknown")
                .build();

        assertEquals(500, errorResponse.getStatus());
        assertEquals("Internal Server Error", errorResponse.getError());
        assertEquals("An unexpected error occurred.", errorResponse.getMessage());
        assertEquals("/api/unknown", errorResponse.getPath());
        assertNull(errorResponse.getTimestamp());
        assertNull(errorResponse.getDetails());
    }

    @Test
    void shouldCreateErrorResponseWithDetails_whenProvided() {
        Map<String, Object> details = new HashMap<>();
        details.put("errorCode", "E123");

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setDetails(details);

        assertNotNull(errorResponse.getDetails());
        assertEquals(details, errorResponse.getDetails());
    }
}