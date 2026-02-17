package com.cardoffers.oms.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidOfferExceptionTest {

    @Test
    void shouldInstantiateWithMessage_whenValidMessageProvided() {
        String message = "Invalid offer provided";
        InvalidOfferException exception = new InvalidOfferException(message);
        
        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldInstantiateWithNullMessage_whenNullProvided() {
        InvalidOfferException exception = new InvalidOfferException(null);
        
        assertNull(exception.getMessage());
    }

    @Test
    void shouldInstantiateWithEmptyMessage_whenEmptyStringProvided() {
        InvalidOfferException exception = new InvalidOfferException("");
        
        assertEquals("", exception.getMessage());
    }
}