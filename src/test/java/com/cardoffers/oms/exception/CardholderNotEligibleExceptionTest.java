package com.cardoffers.oms.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardholderNotEligibleExceptionTest {

    @Test
    void shouldInstantiateExceptionWithMessage_whenMessageProvided() {
        String message = "Cardholder is not eligible";
        CardholderNotEligibleException exception = new CardholderNotEligibleException(message);
        
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldInstantiateExceptionWithNullMessage_whenNullProvided() {
        CardholderNotEligibleException exception = new CardholderNotEligibleException(null);

        assertNotNull(exception);
        assertNull(exception.getMessage());
    }
}