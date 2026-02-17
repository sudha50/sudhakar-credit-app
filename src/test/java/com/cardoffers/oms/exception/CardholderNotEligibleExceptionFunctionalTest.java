package com.cardoffers.oms.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CardholderNotEligibleExceptionFunctionalTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        String message = "Cardholder is not eligible for this offer.";
        CardholderNotEligibleException exception = new CardholderNotEligibleException(message);
        
        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithEmptyMessage() {
        String message = "";
        CardholderNotEligibleException exception = new CardholderNotEligibleException(message);
        
        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithNullMessage() {
        String message = null;
        CardholderNotEligibleException exception = new CardholderNotEligibleException(message);
        
        assertNull(exception.getMessage());
    }
}