package com.cardoffers.oms.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardholderNotEligibleExceptionTest {

    @Test
    void shouldInstantiateWithMessage_whenProvided() {
        String message = "Cardholder is not eligible";
        CardholderNotEligibleException exception = new CardholderNotEligibleException(message);
        
        assertEquals(message, exception.getMessage());
    }
    
    @Test
    void shouldInstantiateWithNullMessage_whenProvided() {
        CardholderNotEligibleException exception = new CardholderNotEligibleException(null);
        
        assertNull(exception.getMessage());
    }
    
    @Test
    void shouldInstantiateWithEmptyMessage_whenProvided() {
        CardholderNotEligibleException exception = new CardholderNotEligibleException("");
        
        assertEquals("", exception.getMessage());
    }
}