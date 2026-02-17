package com.cardoffers.oms.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
class CardholderNotEligibleExceptionIntegrationTest {

    @Test
    void testExceptionMessage() {
        String message = "Cardholder is not eligible for this offer";
        
        CardholderNotEligibleException exception = assertThrows(
                CardholderNotEligibleException.class,
                () -> { throw new CardholderNotEligibleException(message); }
        );

        assertEquals(message, exception.getMessage());
    }

    @Test
    void testExceptionWithoutMessage() {
        CardholderNotEligibleException exception = assertThrows(
                CardholderNotEligibleException.class,
                () -> { throw new CardholderNotEligibleException(null); }
        );

        assertNull(exception.getMessage());
    }
}