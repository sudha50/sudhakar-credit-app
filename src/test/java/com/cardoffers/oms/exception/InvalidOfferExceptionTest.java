package com.cardoffers.oms.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InvalidOfferExceptionTest {

    @Test
    public void shouldSetMessage_whenInvalidOfferExceptionIsCreated() {
        // Arrange
        String expectedMessage = "Invalid offer details";

        // Act
        InvalidOfferException exception = new InvalidOfferException(expectedMessage);

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void shouldSetNullMessage_whenNullIsPassed() {
        // Arrange
        String expectedMessage = null;

        // Act
        InvalidOfferException exception = new InvalidOfferException(expectedMessage);

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void shouldSetEmptyMessage_whenEmptyStringIsPassed() {
        // Arrange
        String expectedMessage = "";

        // Act
        InvalidOfferException exception = new InvalidOfferException(expectedMessage);

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
    }
}