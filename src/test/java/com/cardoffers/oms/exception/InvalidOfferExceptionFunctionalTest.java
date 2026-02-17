package com.cardoffers.oms.exception;

import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class InvalidOfferExceptionFunctionalTest {

    @Test
    void shouldCreateInvalidOfferExceptionWithMessage() {
        String expectedMessage = "This is an invalid offer";
        InvalidOfferException exception = new InvalidOfferException(expectedMessage);
        
        assertEquals(expectedMessage, exception.getMessage());
    }
}