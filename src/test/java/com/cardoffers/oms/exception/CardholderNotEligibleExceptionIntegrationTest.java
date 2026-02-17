package com.cardoffers.oms.exception;

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
        String expectedMessage = "Cardholder is not eligible";
        CardholderNotEligibleException exception = new CardholderNotEligibleException(expectedMessage);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testExceptionInstance() {
        CardholderNotEligibleException exception = new CardholderNotEligibleException("Test message");
        assertTrue(exception instanceof RuntimeException);
    }
}