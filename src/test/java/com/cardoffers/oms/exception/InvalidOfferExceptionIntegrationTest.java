package com.cardoffers.oms.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
public class InvalidOfferExceptionIntegrationTest {

    @Test
    public void testInvalidOfferExceptionMessage() {
        String expectedMessage = "This is an invalid offer";
        
        InvalidOfferException thrown = assertThrows(InvalidOfferException.class, () -> {
            throw new InvalidOfferException(expectedMessage);
        });

        assertEquals(expectedMessage, thrown.getMessage());
    }
}