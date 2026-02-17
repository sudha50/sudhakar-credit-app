package com.cardoffers.oms.exception;

import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Mock
    private HttpServletRequest request;

    @Test
    void shouldReturn404_whenResourceNotFound() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource not found");
        when(request.getRequestURI()).thenReturn("/test/resource");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleResourceNotFound(exception, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Resource not found", response.getBody().getMessage());
        assertEquals("/test/resource", response.getBody().getPath());
    }

    @Test
    void shouldReturn400_whenValidationErrors() {
        FieldError fieldError = new FieldError("testObject", "fieldName", "Field error message");
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "testObject");
        bindingResult.addError(fieldError);
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);
        when(request.getRequestURI()).thenReturn("/test/validate");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleValidationErrors(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().getDetails().containsKey("fieldErrors"));
        assertEquals("Field error message", ((Map<String, String>) response.getBody().getDetails().get("fieldErrors")).get("fieldName"));
    }

    @Test
    void shouldReturn400_whenBusinessExceptions() {
        RuntimeException exception = new InvalidOfferException("Invalid offer");
        when(request.getRequestURI()).thenReturn("/test/business");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleBusinessExceptions(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid offer", response.getBody().getMessage());
        assertEquals("/test/business", response.getBody().getPath());
    }

    @Test
    void shouldReturn500_whenGenericException() {
        Exception exception = new Exception("Unexpected error");
        when(request.getRequestURI()).thenReturn("/test/generic");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGenericException(exception, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Unexpected error occurred", response.getBody().getMessage());
        assertEquals("/test/generic", response.getBody().getPath());
        assertEquals("Exception", response.getBody().getDetails().get("exception"));
    }
}