package com.cardoffers.oms.exception;

import org.mockito.InjectMocks;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
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
        ResourceNotFoundException exception = new ResourceNotFoundException("Not Found");
        when(request.getRequestURI()).thenReturn("/test-uri");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleResourceNotFound(exception, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not Found", response.getBody().getMessage());
        assertEquals("/test-uri", response.getBody().getPath());
    }

    @Test
    void shouldReturn400_whenValidationErrors() {
        Map<String, String> fieldErrorsMap = new LinkedHashMap<>();
        fieldErrorsMap.put("field1", "must not be blank");
        fieldErrorsMap.put("field2", "must be a valid email");

        @Mock

        private BindingResult bindingResult;
        FieldError fieldError1 = new FieldError("objectName", "field1", "must not be blank");
        FieldError fieldError2 = new FieldError("objectName", "field2", "must be a valid email");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);
        when(request.getRequestURI()).thenReturn("/test-uri");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleValidationErrors(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().getDetails().containsKey("fieldErrors"));
        assertEquals(fieldErrorsMap, response.getBody().getDetails().get("fieldErrors"));
    }

    @Test
    void shouldReturn400_whenBusinessExceptionOccurs() {
        RuntimeException exception = new InvalidOfferException("Invalid offer");
        when(request.getRequestURI()).thenReturn("/test-uri");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleBusinessExceptions(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid offer", response.getBody().getMessage());
        assertEquals("/test-uri", response.getBody().getPath());
    }

    @Test
    void shouldReturn500_whenGenericExceptionOccurs() {
        Exception exception = new Exception("Unexpected error");
        when(request.getRequestURI()).thenReturn("/test-uri");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGenericException(exception, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Unexpected error occurred", response.getBody().getMessage());
        assertEquals("/test-uri", response.getBody().getPath());
        assertEquals("Exception", response.getBody().getDetails().get("exception"));
    }
}