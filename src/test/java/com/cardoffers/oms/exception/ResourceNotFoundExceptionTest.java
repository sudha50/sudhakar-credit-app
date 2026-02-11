package com.cardoffers.oms.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResourceNotFoundExceptionTest {

    @Test
    void shouldReturnMessage_whenExceptionIsThrown() {
        String message = "Resource not found";
        ResourceNotFoundException exception = new ResourceNotFoundException(message);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldReturnNullMessage_whenExceptionIsThrownWithEmptyString() {
        String message = "";
        ResourceNotFoundException exception = new ResourceNotFoundException(message);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldReturnNullMessage_whenExceptionIsThrownWithNull() {
        ResourceNotFoundException exception = new ResourceNotFoundException(null);
        assertEquals(null, exception.getMessage());
    }
}