package com.cardoffers.oms.exception;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class InvalidOfferExceptionIntegrationTest {

    @Test
    void testInvalidOfferExceptionMessage() {
        String expectedMessage = "Invalid offer provided!";
        InvalidOfferException exception = new InvalidOfferException(expectedMessage);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testInvalidOfferExceptionWithNullMessage() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new InvalidOfferException(null);
        });
        assertNotNull(exception);
    }
}