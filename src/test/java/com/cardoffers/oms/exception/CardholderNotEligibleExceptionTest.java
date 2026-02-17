package com.cardoffers.oms.exception;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CardholderNotEligibleExceptionTest {

    @Test
    void shouldInstantiateExceptionWithMessage_whenMessageProvided() {
        String expectedMessage = "Cardholder is not eligible for the offer";
        CardholderNotEligibleException exception = new CardholderNotEligibleException(expectedMessage);
        
        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWithCorrectMessage_whenConstructedWithNullMessage() {
        CardholderNotEligibleException exception = assertThrows(CardholderNotEligibleException.class, () -> {
            throw new CardholderNotEligibleException(null);
        });

        assertNull(exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWithEmptyMessage_whenConstructedWithEmptyString() {
        String expectedMessage = "";
        CardholderNotEligibleException exception = new CardholderNotEligibleException(expectedMessage);
        
        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWithSpecificMessage_whenDifferentMessageProvided() {
        String expectedMessage = "Specific eligibility reason";
        CardholderNotEligibleException exception = new CardholderNotEligibleException(expectedMessage);
        
        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage());
    }
}