package com.cardoffers.oms.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResourceNotFoundExceptionTest {

    @Test
    void shouldSetMessage_whenExceptionIsCreated() {
        String expectedMessage = "Resource not found";
        ResourceNotFoundException exception = new ResourceNotFoundException(expectedMessage);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void shouldSetAnotherMessage_whenDifferentExceptionIsCreated() {
        String expectedMessage = "User not found";
        ResourceNotFoundException exception = new ResourceNotFoundException(expectedMessage);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void shouldSetNullMessage_whenNullIsPassed() {
        ResourceNotFoundException exception = new ResourceNotFoundException(null);
        assertEquals(null, exception.getMessage());
    }

    @Test
    void shouldSetEmptyMessage_whenEmptyStringIsPassed() {
        ResourceNotFoundException exception = new ResourceNotFoundException("");
        assertEquals("", exception.getMessage());
    }
}