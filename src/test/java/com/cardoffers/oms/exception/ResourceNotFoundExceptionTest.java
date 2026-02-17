package com.cardoffers.oms.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceNotFoundExceptionTest {

    @Test
    void shouldCreateResourceNotFoundException_whenGivenMessage() {
        String message = "Resource not found";
        ResourceNotFoundException exception = new ResourceNotFoundException(message);
        
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldCreateResourceNotFoundException_withEmptyMessage() {
        String message = "";
        ResourceNotFoundException exception = new ResourceNotFoundException(message);
        
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldCreateResourceNotFoundException_withNullMessage() {
        String message = null;

        ResourceNotFoundException exception = new ResourceNotFoundException(message);
        
        assertNotNull(exception);
        assertNull(exception.getMessage());
    }
}