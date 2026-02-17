package com.cardoffers.oms.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CardholderNotEligibleExceptionTest {

    @Test
    void shouldReturnMessage_whenExceptionIsThrown() {
        String message = "Cardholder is not eligible for this offer";
        CardholderNotEligibleException exception = new CardholderNotEligibleException(message);
        
        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldReturnDifferentMessage_whenAnotherExceptionIsThrown() {
        String message = "Another eligibility check failed";
        CardholderNotEligibleException exception = new CardholderNotEligibleException(message);
        
        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldReturnNullMessage_whenNullIsPassed() {
        CardholderNotEligibleException exception = new CardholderNotEligibleException(null);
        
        assertEquals(null, exception.getMessage());
    }
}