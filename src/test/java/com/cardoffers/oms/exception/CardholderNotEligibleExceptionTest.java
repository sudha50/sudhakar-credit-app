package com.cardoffers.oms.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardholderNotEligibleExceptionTest {

    @Test
    void shouldSetMessage_whenExceptionIsCreated() {
        String message = "Cardholder is not eligible";
        CardholderNotEligibleException exception = new CardholderNotEligibleException(message);
        
        assertEquals(message, exception.getMessage());
    }
}