package com.cardoffers.oms.exception;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BeanPropertyBindingResult;

import java.util.List;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;
    private HttpServletRequest httpServletRequest;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void shouldReturn404_whenResourceNotFound() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource not found");
        when(httpServletRequest.getRequestURI()).thenReturn("/test/resource");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleResourceNotFound(exception, httpServletRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Resource not found", response.getBody().getMessage());
    }

    @Test
    void shouldReturn400_whenValidationErrors() {
        FieldError fieldError = new FieldError("objectName", "fieldName", "Error message");
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "objectName");
        bindingResult.addError(fieldError);
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);
        when(httpServletRequest.getRequestURI()).thenReturn("/test/validation");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleValidationErrors(exception, httpServletRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().getDetails().containsKey("fieldErrors"));
        assertEquals("Error message", ((List<String>) response.getBody().getDetails().get("fieldErrors")).get("fieldName"));
    }

    @Test
    void shouldReturn400_whenBusinessExceptionOccurred() {
        InvalidOfferException exception = new InvalidOfferException("Invalid offer");
        when(httpServletRequest.getRequestURI()).thenReturn("/test/business");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleBusinessExceptions(exception, httpServletRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid offer", response.getBody().getMessage());
    }

    @Test
    void shouldReturn500_whenGenericExceptionOccurred() {
        Exception exception = new RuntimeException("Unexpected error");
        when(httpServletRequest.getRequestURI()).thenReturn("/test/generic");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGenericException(exception, httpServletRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Unexpected error occurred", response.getBody().getMessage());
        assertEquals("RuntimeException", response.getBody().getDetails().get("exception"));
    }
}