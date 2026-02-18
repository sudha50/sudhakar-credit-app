package com.cardoffers.oms.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidOfferExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage_whenMessageProvided() {
        // Arrange
        String message = "Invalid offer";

        // Act
        InvalidOfferException exception = new InvalidOfferException(message);
        
        // Assert
        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithNullMessage_whenMessageIsNull() {
        // Act
        InvalidOfferException exception = new InvalidOfferException(null);
        
        // Assert
        assertNull(exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithEmptyMessage_whenMessageIsEmpty() {
        // Arrange
        String message = "";
        
        // Act
        InvalidOfferException exception = new InvalidOfferException(message);
        
        // Assert
        assertEquals(message, exception.getMessage());
    }
}