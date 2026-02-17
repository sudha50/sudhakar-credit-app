package com.cardoffers.oms.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

public class ErrorResponseTest {

    @Test
    public void shouldCreateErrorResponse_whenAllFieldsProvided() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(404);
        errorResponse.setError("Not Found");
        errorResponse.setMessage("The requested resource was not found");
        errorResponse.setPath("/api/resource");
        errorResponse.setDetails(new HashMap<>());

        assertNotNull(errorResponse.getTimestamp());
        assertEquals(404, errorResponse.getStatus());
        assertEquals("Not Found", errorResponse.getError());
        assertEquals("The requested resource was not found", errorResponse.getMessage());
        assertEquals("/api/resource", errorResponse.getPath());
    }

    @Test
    public void shouldAllowEmptyDetails_whenCreatingErrorResponse() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setDetails(null);

        assertNull(errorResponse.getDetails());
    }

    @Test
    public void shouldSetTimestamp_whenErrorResponseUpdated() {
        ErrorResponse errorResponse = new ErrorResponse();
        LocalDateTime now = LocalDateTime.now();
        errorResponse.setTimestamp(now);

        assertEquals(now, errorResponse.getTimestamp());
    }

    @Test
    public void shouldSetStatus_whenErrorResponseUpdated() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(500);

        assertEquals(500, errorResponse.getStatus());
    }

    @Test
    public void shouldSetError_whenErrorResponseUpdated() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError("Internal Server Error");

        assertEquals("Internal Server Error", errorResponse.getError());
    }

    @Test
    public void shouldSetMessage_whenErrorResponseUpdated() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("An unexpected error occurred");

        assertEquals("An unexpected error occurred", errorResponse.getMessage());
    }

    @Test
    public void shouldSetPath_whenErrorResponseUpdated() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setPath("/api/error");

        assertEquals("/api/error", errorResponse.getPath());
    }
}