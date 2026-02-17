package com.cardoffers.oms.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class InvalidOfferExceptionIntegrationTest {

    @Test
    void testInvalidOfferExceptionMessage() {
        String expectedMessage = "This is an invalid offer.";
        InvalidOfferException exception = new InvalidOfferException(expectedMessage);
        
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testInvalidOfferExceptionWithNullMessage() {
        InvalidOfferException exception = new InvalidOfferException(null);
        
        assertNull(exception.getMessage());
    }
}