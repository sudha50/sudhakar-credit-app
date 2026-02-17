package com.cardoffers.oms.exception;

import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void shouldCreateExceptionWithDifferentMessage() {
        String message = "Reason for ineligibility: insufficient credit score.";
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
    void shouldThrowNullPointerExceptionWhenMessageIsNull() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new CardholderNotEligibleException(null);
        });

        assertEquals("message must not be null", exception.getMessage());
    }
}