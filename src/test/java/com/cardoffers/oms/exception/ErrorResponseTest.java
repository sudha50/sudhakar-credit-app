package com.cardoffers.oms.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void shouldCreateErrorResponse_whenAllFieldsProvided() {
        var details = new HashMap<String, Object>();
        details.put("key", "value");
        
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(404);
        errorResponse.setError("Not Found");
        errorResponse.setMessage("The requested resource was not found.");
        errorResponse.setPath("/api/resource");
        errorResponse.setDetails(details);

        assertNotNull(errorResponse);
        assertEquals(404, errorResponse.getStatus());
        assertEquals("Not Found", errorResponse.getError());
        assertEquals("The requested resource was not found.", errorResponse.getMessage());
        assertEquals("/api/resource", errorResponse.getPath());
        assertEquals(details, errorResponse.getDetails());
    }

    @Test
    void shouldSetDefaultValues_whenNoArgsConstructorUsed() {
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
    void shouldUpdateErrorResponseFields_whenSettersCalled() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(500);
        errorResponse.setError("Internal Server Error");

        assertEquals(500, errorResponse.getStatus());
        assertEquals("Internal Server Error", errorResponse.getError());
    }

    @Test
    void shouldNotThrowException_whenSettingNullFields() {
        ErrorResponse errorResponse = new ErrorResponse();
        
        assertDoesNotThrow(() -> {
            errorResponse.setMessage(null);
            errorResponse.setPath(null);
        });
        
        assertNull(errorResponse.getMessage());
        assertNull(errorResponse.getPath());
    }

    @Test
    void shouldCreateErrorResponseWithBuilder_whenAllFieldsProvided() {
        var details = new HashMap<String, Object>();
        details.put("key", "value");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(400)
                .error("Bad Request")
                .message("Invalid input provided.")
                .path("/api/resource")
                .details(details)
                .build();

        assertNotNull(errorResponse);
        assertEquals(400, errorResponse.getStatus());
        assertEquals("Bad Request", errorResponse.getError());
        assertEquals("Invalid input provided.", errorResponse.getMessage());
        assertEquals("/api/resource", errorResponse.getPath());
        assertEquals(details, errorResponse.getDetails());
    }
}