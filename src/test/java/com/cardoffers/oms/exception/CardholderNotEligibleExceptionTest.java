package com.cardoffers.oms.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class CardholderNotEligibleExceptionTest {

    @Test
    public void shouldReturnMessage_whenExceptionIsThrown() {
        String expectedMessage = "Cardholder not eligible";
        
        CardholderNotEligibleException exception = new CardholderNotEligibleException(expectedMessage);
        
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void shouldThrowException_whenNullMessageIsProvided() {
        assertThrows(NullPointerException.class, () -> {
            new CardholderNotEligibleException(null);
        });
    }

    @Test
    public void shouldReturnMessage_whenDifferentMessageIsProvided() {
        String expectedMessage = "Eligibility check failed";
        
        CardholderNotEligibleException exception = new CardholderNotEligibleException(expectedMessage);
        
        assertEquals(expectedMessage, exception.getMessage());
    }
}