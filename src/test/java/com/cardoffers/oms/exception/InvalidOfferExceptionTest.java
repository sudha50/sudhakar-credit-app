package com.cardoffers.oms.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidOfferExceptionTest {

    @Test
    void shouldInstantiateExceptionWithMessage_whenMessageIsProvided() {
        String message = "Invalid offer details";
        InvalidOfferException exception = new InvalidOfferException(message);
        
        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldInstantiateExceptionWithNullMessage_whenNullIsProvided() {
        InvalidOfferException exception = new InvalidOfferException(null);
        
        assertNull(exception.getMessage());
    }

    @Test
    void shouldInstantiateExceptionWithEmptyMessage_whenEmptyStringIsProvided() {
        InvalidOfferException exception = new InvalidOfferException("");
        
        assertEquals("", exception.getMessage());
    }
}