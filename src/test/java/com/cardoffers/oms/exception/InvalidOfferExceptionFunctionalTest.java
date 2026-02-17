package com.cardoffers.oms.exception;

import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

@SpringBootTest
@ActiveProfiles("test")
public class InvalidOfferExceptionFunctionalTest {

    @Test
    public void shouldCreateInvalidOfferExceptionWithCorrectMessage() {
        String expectedMessage = "Invalid Offer";
        InvalidOfferException exception = new InvalidOfferException(expectedMessage);
        assertEquals(expectedMessage, exception.getMessage());
    }
}