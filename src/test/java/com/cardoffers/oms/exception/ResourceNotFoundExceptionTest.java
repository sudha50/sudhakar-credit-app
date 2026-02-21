package com.cardoffers.oms.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceNotFoundExceptionTest {

    @Test
    void shouldReturnMessage_whenInitializedWithMessage() {
        String errorMessage = "Resource not found";
        ResourceNotFoundException exception = new ResourceNotFoundException(errorMessage);
        
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void shouldReturnNonNullMessage_whenInitializedWithNull() {
        ResourceNotFoundException exception = new ResourceNotFoundException(null);
        
        assertNull(exception.getMessage());
    }

    @Test
    void shouldReturnCorrectClass_whenInstanceOfResourceNotFoundException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Test");
        
        assertTrue(exception instanceof ResourceNotFoundException);
    }
}