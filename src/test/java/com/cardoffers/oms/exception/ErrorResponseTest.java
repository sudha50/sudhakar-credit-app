package com.cardoffers.oms.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.HashMap;

class ErrorResponseTest {

    @Test
    void shouldCreateErrorResponse_whenAllFieldsAreProvided() {
        var details = new HashMap<String, Object>();
        details.put("field", "value");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(404)
                .error("Not Found")
                .message("The requested resource was not found.")
                .path("/path/to/resource")
                .details(details)
                .build();

        assertNotNull(errorResponse);
        assertEquals(404, errorResponse.getStatus());
        assertEquals("Not Found", errorResponse.getError());
        assertEquals("The requested resource was not found.", errorResponse.getMessage());
        assertEquals("/path/to/resource", errorResponse.getPath());
        assertEquals(details, errorResponse.getDetails());
    }

    @Test
    void shouldSetDefaultValues_whenNoFieldsAreProvided() {
        ErrorResponse errorResponse = new ErrorResponse();

        assertNotNull(errorResponse);
        assertNull(errorResponse.getTimestamp());
        assertEquals(0, errorResponse.getStatus());
        assertNull(errorResponse.getError());
        assertNull(errorResponse.getMessage());
        assertNull(errorResponse.getPath());
        assertNull(errorResponse.getDetails());
    }

    @Test
    void shouldOverrideFields_whenUsingBuilderPattern() {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(500)
                .error("Internal Server Error")
                .message("An unexpected error occurred.")
                .build();

        assertEquals(500, errorResponse.getStatus());
        assertEquals("Internal Server Error", errorResponse.getError());
        assertEquals("An unexpected error occurred.", errorResponse.getMessage());
        assertNull(errorResponse.getPath());
        assertNull(errorResponse.getDetails());
    }

    @Test
    void shouldReturnCorrectTimestamp_whenProvided() {
        LocalDateTime timestamp = LocalDateTime.now();
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(timestamp);

        assertEquals(timestamp, errorResponse.getTimestamp());
    }

    @Test
    void shouldReturnCorrectDetails_whenProvided() {
        var details = new HashMap<String, Object>();
        details.put("key", "value");
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setDetails(details);

        assertEquals(details, errorResponse.getDetails());
    }
}