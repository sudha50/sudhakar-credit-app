package com.cardoffers.oms.exception;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

class GlobalExceptionHandlerTest {
    private GlobalExceptionHandler globalExceptionHandler;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void shouldReturnNotFoundResponse_whenResourceNotFoundExceptionIsThrown() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource not found");
        Mockito.when(request.getRequestURI()).thenReturn("/test-uri");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleResourceNotFound(exception, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Resource not found", response.getBody().getMessage());
        assertEquals("/test-uri", response.getBody().getPath());
    }

    @Test
    void shouldReturnBadRequestResponse_whenValidationErrorsOccur() {
        FieldError fieldError = new FieldError("obj", "field", "must not be empty");
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "obj");
        bindingResult.addError(fieldError);
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);
        Mockito.when(request.getRequestURI()).thenReturn("/test-uri");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleValidationErrors(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation failed", response.getBody().getMessage());
        assertEquals("/test-uri", response.getBody().getPath());
        assertEquals(Map.of("fieldErrors", Map.of("field", "must not be empty")), response.getBody().getDetails());
    }

    @Test
    void shouldReturnBadRequestResponse_whenBusinessExceptionIsThrown() {
        InvalidOfferException exception = new InvalidOfferException("Invalid offer");
        Mockito.when(request.getRequestURI()).thenReturn("/test-uri");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleBusinessExceptions(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid offer", response.getBody().getMessage());
        assertEquals("/test-uri", response.getBody().getPath());
    }

    @Test
    void shouldReturnInternalServerErrorResponse_whenGenericExceptionIsThrown() {
        Exception exception = new Exception("Unexpected error");
        Mockito.when(request.getRequestURI()).thenReturn("/test-uri");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGenericException(exception, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Unexpected error occurred", response.getBody().getMessage());
        assertEquals("/test-uri", response.getBody().getPath());
        assertEquals(Map.of("exception", "Exception"), response.getBody().getDetails());
    }
}