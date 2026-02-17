package com.cardoffers.oms.exception;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CardholderNotEligibleExceptionIntegrationTest {

    @Test
    void testExceptionMessage() {
        String message = "Cardholder is not eligible for this offer.";
        CardholderNotEligibleException exception = new CardholderNotEligibleException(message);
        
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testExceptionIsRuntimeException() {
        CardholderNotEligibleException exception = new CardholderNotEligibleException("Test message");
        
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void testExceptionWithNullMessage() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new CardholderNotEligibleException(null);
        });

        assertEquals("message must not be null", exception.getMessage());
    }
}