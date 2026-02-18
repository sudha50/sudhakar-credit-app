package com.cardoffers.oms.exception;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class ErrorResponseTest {

    @Test
    void shouldCreateErrorResponse_whenAllFieldsAreProvided() {
        // Arrange
        LocalDateTime timestamp = LocalDateTime.now();
        int status = 404;
        String error = "Not Found";
        String message = "The requested resource was not found";
        String path = "/api/resource";
        Map<String, Object> details = new HashMap<>();
        details.put("key", "value");

        // Act
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(timestamp)
                .status(status)
                .error(error)
                .message(message)
                .path(path)
                .details(details)
                .build();

        // Assert
        assertNotNull(errorResponse);
        assertEquals(timestamp, errorResponse.getTimestamp());
        assertEquals(status, errorResponse.getStatus());
        assertEquals(error, errorResponse.getError());
        assertEquals(message, errorResponse.getMessage());
        assertEquals(path, errorResponse.getPath());
        assertEquals(details, errorResponse.getDetails());
    }

    @Test
    void shouldCreateErrorResponseWithNoArgsConstructor_andSetFields() {
        // Arrange
        ErrorResponse errorResponse = new ErrorResponse();
        LocalDateTime timestamp = LocalDateTime.now();
        int status = 500;
        String error = "Internal Server Error";
        String message = "An unexpected error occurred";
        String path = "/api/unexpected";
        Map<String, Object> details = new HashMap<>();
        details.put("debug", "stack trace");

        // Act
        errorResponse.setTimestamp(timestamp);
        errorResponse.setStatus(status);
        errorResponse.setError(error);
        errorResponse.setMessage(message);
        errorResponse.setPath(path);
        errorResponse.setDetails(details);

        // Assert
        assertNotNull(errorResponse);
        assertEquals(timestamp, errorResponse.getTimestamp());
        assertEquals(status, errorResponse.getStatus());
        assertEquals(error, errorResponse.getError());
        assertEquals(message, errorResponse.getMessage());
        assertEquals(path, errorResponse.getPath());
        assertEquals(details, errorResponse.getDetails());
    }

    @Test
    void shouldNotAllowNullTimestamp_whenCreatingErrorResponse() {
        // Arrange
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(400);
        errorResponse.setError("Bad Request");
        errorResponse.setMessage("Request cannot be processed");
        errorResponse.setPath("/api/request");
        errorResponse.setDetails(new HashMap<>());

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            errorResponse.setTimestamp(null);
        });
    }

    @Test
    void shouldHandleEmptyDetails_whenCreatingErrorResponse() {
        // Arrange
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(400);
        errorResponse.setError("Bad Request");
        errorResponse.setMessage("Request cannot be processed");
        errorResponse.setPath("/api/request");
        errorResponse.setDetails(new HashMap<>()); // Empty details

        // Act & Assert
        assertEquals(new HashMap<>(), errorResponse.getDetails());
    }
}