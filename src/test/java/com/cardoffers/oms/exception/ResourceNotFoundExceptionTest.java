package com.cardoffers.oms.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResourceNotFoundExceptionTest {

    @Test
    void shouldReturnMessage_whenExceptionIsThrown() {
        // Arrange
        String message = "Resource not found";
        
        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(message);
        
        // Assert
        assertEquals(message, exception.getMessage());
    }
}