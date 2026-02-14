package com.cardoffers.oms.exception;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void shouldReturn404Response_whenResourceNotFoundExceptionIsThrown() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource not found");
        @Mock
        private HttpServletRequest request;
        when(request.getRequestURI()).thenReturn("/test/resource");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleResourceNotFound(exception, request);

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
        assertEquals("Resource not found", response.getBody().getMessage());
        assertEquals("/test/resource", response.getBody().getPath());
    }

    @Test
    void shouldReturn400Response_whenMethodArgumentNotValidExceptionIsThrown() {
        @Mock
        private MethodArgumentNotValidException exception;
        @Mock
        private HttpServletRequest request;
        when(request.getRequestURI()).thenReturn("/test/validate");

        FieldError fieldError = new FieldError("objectName", "field", "must not be null");
        when(exception.getBindingResult().getFieldErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleValidationErrors(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
        assertEquals("Validation failed", response.getBody().getMessage());
        assertEquals("/test/validate", response.getBody().getPath());
        
        Map<String, Object> details = response.getBody().getDetails();
        assertNotNull(details);
        assertTrue(details.containsKey("fieldErrors"));
        assertEquals("must not be null", ((Map<?, ?>) details.get("fieldErrors")).get("field"));
    }
    
    @Test
    void shouldReturn400Response_whenBusinessExceptionIsThrown() {
        RuntimeException exception = new InvalidOfferException("Invalid offer");
        @Mock
        private HttpServletRequest request;
        when(request.getRequestURI()).thenReturn("/test/offer");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleBusinessExceptions(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
        assertEquals("Invalid offer", response.getBody().getMessage());
        assertEquals("/test/offer", response.getBody().getPath());
    }

    @Test
    void shouldReturn500Response_whenGenericExceptionIsThrown() {
        Exception exception = new Exception("Unexpected error");
        @Mock
        private HttpServletRequest request;
        when(request.getRequestURI()).thenReturn("/test/generic");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGenericException(exception, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().getStatus());
        assertEquals("Unexpected error occurred", response.getBody().getMessage());
        assertEquals("/test/generic", response.getBody().getPath());
        assertEquals("Exception", ((Map<String, Object>) response.getBody().getDetails()).get("exception"));
    }

    private static ErrorResponse anErrorResponse() {
        ErrorResponse entity = new ErrorResponse();
        return entity;
    }
}