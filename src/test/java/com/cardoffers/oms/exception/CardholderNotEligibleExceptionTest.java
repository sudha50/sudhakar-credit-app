package com.cardoffers.oms.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CardholderNotEligibleExceptionTest {

    @Test
    void shouldReturnMessage_whenExceptionIsThrown() {
        String message = "Cardholder is not eligible";
        CardholderNotEligibleException exception = new CardholderNotEligibleException(message);
        
        assertEquals(message, exception.getMessage());
    }
}