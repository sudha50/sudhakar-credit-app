package com.cardoffers.oms.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class InvalidOfferExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage_whenMessageProvided() {
        // Arrange
        String errorMessage = "Invalid offer details provided.";

        // Act
        InvalidOfferException exception = new InvalidOfferException(errorMessage);
        
        // Assert
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithNullMessage_whenNullMessageProvided() {
        // Act
        InvalidOfferException exception = new InvalidOfferException(null);
        
        // Assert
        assertNull(exception.getMessage());
    }
}