package com.cardoffers.oms.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class InvalidOfferExceptionIntegrationTest {

    @Test
    void testInvalidOfferExceptionMessage() {
        String expectedMessage = "Invalid offer provided";
        InvalidOfferException exception = new InvalidOfferException(expectedMessage);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testInvalidOfferExceptionIsARuntimeException() {
        InvalidOfferException exception = new InvalidOfferException("Some message");
        assertTrue(exception instanceof RuntimeException);
    }
}