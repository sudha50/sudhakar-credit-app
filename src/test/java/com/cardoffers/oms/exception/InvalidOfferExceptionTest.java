package com.cardoffers.oms.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InvalidOfferExceptionTest {

    @Test
    void shouldReturnMessage_whenExceptionIsThrown() {
        String expectedMessage = "Invalid offer data";
        InvalidOfferException exception = new InvalidOfferException(expectedMessage);
        
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void shouldReturnNullMessage_whenExceptionIsThrownWithNull() {
        InvalidOfferException exception = new InvalidOfferException(null);
        
        assertEquals(null, exception.getMessage());
    }

    @Test
    void shouldReturnEmptyMessage_whenExceptionIsThrownWithEmptyString() {
        InvalidOfferException exception = new InvalidOfferException("");
        
        assertEquals("", exception.getMessage());
    }
}