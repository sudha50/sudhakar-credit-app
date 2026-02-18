package com.cardoffers.oms.exception;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;

import jakarta.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        globalExceptionHandler;
    }

    @Test
    void shouldReturn404_whenResourceNotFoundExceptionThrown() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource not found");
        when(request.getRequestURI()).thenReturn("/api/resource");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleResourceNotFound(exception, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Resource not found", response.getBody().getMessage());
        assertEquals("/api/resource", response.getBody().getPath());
    }

    @Test
    void shouldReturn400_whenMethodArgumentNotValidExceptionThrown() {
        @Mock
        private MethodArgumentNotValidException exception;
        FieldError fieldError = new FieldError("objectName", "fieldName", "Field error message");
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "objectName");
        bindingResult.addError(fieldError);
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(request.getRequestURI()).thenReturn("/api/validate");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleValidationErrors(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation failed", response.getBody().getMessage());
        assertTrue(response.getBody().getDetails().containsKey("fieldErrors"));
        assertEquals("Field error message", ((List<FieldError>) response.getBody().getDetails().get("fieldErrors")).get(0).getDefaultMessage());
    }

    @Test
    void shouldReturn400_whenBusinessExceptionThrown() {
        InvalidOfferException exception = new InvalidOfferException("Invalid offer");
        when(request.getRequestURI()).thenReturn("/api/offer");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleBusinessExceptions(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid offer", response.getBody().getMessage());
        assertEquals("/api/offer", response.getBody().getPath());
    }

    @Test
    void shouldReturn500_whenGenericExceptionThrown() {
        Exception exception = new Exception("Unexpected error");
        when(request.getRequestURI()).thenReturn("/api/error");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGenericException(exception, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Unexpected error occurred", response.getBody().getMessage());
        assertEquals("/api/error", response.getBody().getPath());
        assertEquals("Exception", response.getBody().getDetails().get("exception"));
    }
}