package com.cardoffers.oms.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidOfferExceptionTest {

    @Test
    void shouldReturnMessage_whenExceptionIsThrown() {
        String message = "Invalid offer provided";
        InvalidOfferException exception = new InvalidOfferException(message);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldReturnNullMessage_whenExceptionIsThrownWithNull() {
        InvalidOfferException exception = new InvalidOfferException(null);
        assertNull(exception.getMessage());
    }

    @Test
    void shouldReturnEmptyMessage_whenExceptionIsThrownWithEmptyString() {
        InvalidOfferException exception = new InvalidOfferException("");
        assertEquals("", exception.getMessage());
    }

    @Test
    void shouldNotBeNull_whenCreatingExceptionWithMessage() {
        String message = "Sample message";
        InvalidOfferException exception = new InvalidOfferException(message);
        assertNotNull(exception);
    }
}