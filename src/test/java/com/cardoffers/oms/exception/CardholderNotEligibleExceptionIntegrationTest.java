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
public class CardholderNotEligibleExceptionIntegrationTest {

    @Test
    public void testExceptionMessage() {
        String message = "Cardholder is not eligible for this offer.";
        CardholderNotEligibleException exception = new CardholderNotEligibleException(message);
        assertEquals(message, exception.getMessage());
    }

    @Test
    public void testExceptionSubclass() {
        String message = "Eligibility criteria not met.";
        CardholderNotEligibleException exception = new CardholderNotEligibleException(message);
        assertTrue(exception instanceof RuntimeException);
    }
}