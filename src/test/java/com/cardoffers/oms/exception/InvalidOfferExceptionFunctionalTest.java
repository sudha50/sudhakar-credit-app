package com.cardoffers.oms.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class InvalidOfferExceptionFunctionalTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        String errorMessage = "This is an invalid offer.";
        InvalidOfferException exception = new InvalidOfferException(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithDifferentMessage() {
        String errorMessage = "Offer cannot be processed.";
        InvalidOfferException exception = new InvalidOfferException(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithNullMessage() {
        InvalidOfferException exception = new InvalidOfferException(null);
        assertNull(exception.getMessage());
    }
}