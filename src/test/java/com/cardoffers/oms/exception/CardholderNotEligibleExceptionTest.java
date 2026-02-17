package com.cardoffers.oms.exception;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardholderNotEligibleExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage_whenMessageProvided() {
        String message = "Cardholder is not eligible for this offer.";
        CardholderNotEligibleException exception = new CardholderNotEligibleException(message);
        
        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWithNullMessage_whenNullMessageProvided() {
        Exception exception = assertThrows(CardholderNotEligibleException.class, () -> {
            throw new CardholderNotEligibleException(null);
        });
        
        assertNull(exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWithEmptyMessage_whenEmptyMessageProvided() {
        String message = "";
        CardholderNotEligibleException exception = new CardholderNotEligibleException(message);
        
        assertEquals(message, exception.getMessage());
    }
}