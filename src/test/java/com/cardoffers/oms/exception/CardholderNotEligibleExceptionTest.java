package com.cardoffers.oms.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardholderNotEligibleExceptionTest {

    @Test
    void shouldReturnCorrectMessage_whenExceptionIsThrown() {
        String expectedMessage = "Cardholder is not eligible";
        CardholderNotEligibleException exception = new CardholderNotEligibleException(expectedMessage);
        
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void shouldReturnNullMessage_whenNoMessageProvided() {
        CardholderNotEligibleException exception = new CardholderNotEligibleException(null);
        
        assertNull(exception.getMessage());
    }

    @Test
    void shouldReturnEmptyMessage_whenEmptyMessageProvided() {
        CardholderNotEligibleException exception = new CardholderNotEligibleException("");
        
        assertEquals("", exception.getMessage());
    }
}