package com.cardoffers.oms.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private HttpServletRequest request;

    @Test
    void shouldReturnNotFoundResponse_whenResourceNotFoundExceptionThrown() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource not found");
        when(request.getRequestURI()).thenReturn("/test-uri");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleResourceNotFound(exception, request);

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
        assertEquals("Resource not found", response.getBody().getMessage());
        assertEquals("/test-uri", response.getBody().getPath());
    }

    @Test
    void shouldReturnBadRequestResponse_whenValidationErrorsOccur() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        FieldError fieldError = new FieldError("testObject", "testField", "Test field error");

        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(fieldError);
        when(exception.getBindingResult().getFieldErrors()).thenReturn(fieldErrors);
        when(request.getRequestURI()).thenReturn("/test-uri");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleValidationErrors(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        assertEquals("Validation failed", response.getBody().getMessage());
        assertEquals("/test-uri", response.getBody().getPath());
        assertEquals(1, ((Map<String, String>) response.getBody().getDetails().get("fieldErrors")).size());
        assertEquals("Test field error", ((Map<String, String>) response.getBody().getDetails().get("fieldErrors")).get("testField"));
    }

    @Test
    void shouldReturnBadRequestResponse_whenBusinessExceptionThrown() {
        InvalidOfferException exception = new InvalidOfferException("Invalid offer");
        when(request.getRequestURI()).thenReturn("/test-uri");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleBusinessExceptions(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        assertEquals("Invalid offer", response.getBody().getMessage());
        assertEquals("/test-uri", response.getBody().getPath());
    }

    @Test
    void shouldReturnInternalServerErrorResponse_whenGenericExceptionThrown() {
        Exception exception = new Exception("Unexpected error");
        when(request.getRequestURI()).thenReturn("/test-uri");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGenericException(exception, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());
        assertEquals("Unexpected error occurred", response.getBody().getMessage());
        assertEquals("/test-uri", response.getBody().getPath());
        assertEquals("Exception", response.getBody().getDetails().get("exception"));
    }
}