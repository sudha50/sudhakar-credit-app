package com.cardoffers.oms.exception;

import org.mockito.InjectMocks;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        globalExceptionHandler;
    }

    @Test
    void shouldReturn404_whenResourceNotFound() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Resource not found");
        when(request.getRequestURI()).thenReturn("/test");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleResourceNotFound(ex, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Resource not found", response.getBody().getMessage());
        assertEquals("/test", response.getBody().getPath());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void shouldReturn400_whenValidationErrors() {
        FieldError fieldError = new FieldError("objectName", "fieldName", "Field error message");
        @Mock
        private BindingResult bindingResult;
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));
        
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);
        when(request.getRequestURI()).thenReturn("/test");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleValidationErrors(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation failed", response.getBody().getMessage());
        assertEquals("/test", response.getBody().getPath());
        Map<String, String> fieldErrors = (Map<String, String>) response.getBody().getDetails().get("fieldErrors");
        assertEquals("Field error message", fieldErrors.get("fieldName"));
    }

    @Test
    void shouldReturn400_whenBusinessExceptionOccurs() {
        RuntimeException ex = new InvalidOfferException("Invalid offer");
        when(request.getRequestURI()).thenReturn("/test");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleBusinessExceptions(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid offer", response.getBody().getMessage());
        assertEquals("/test", response.getBody().getPath());
    }

    @Test
    void shouldReturn500_whenGenericExceptionOccurs() {
        Exception ex = new Exception("Unexpected error");
        when(request.getRequestURI()).thenReturn("/test");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGenericException(ex, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Unexpected error occurred", response.getBody().getMessage());
        assertEquals("/test", response.getBody().getPath());
        assertEquals("Exception", ((Map<String, Object>) response.getBody().getDetails()).get("exception"));
    }
}