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
    void shouldCreateInvalidOfferExceptionWithMessage() {
        String expectedMessage = "Invalid offer provided";
        InvalidOfferException exception = new InvalidOfferException(expectedMessage);
        
        assertEquals(expectedMessage, exception.getMessage());
    }
}