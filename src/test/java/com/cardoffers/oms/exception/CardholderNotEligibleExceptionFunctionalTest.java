package com.cardoffers.oms.exception;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CardholderNotEligibleExceptionFunctionalTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        String expectedMessage = "Cardholder is not eligible for this offer";
        CardholderNotEligibleException exception = new CardholderNotEligibleException(expectedMessage);
        
        assertEquals(expectedMessage, exception.getMessage());
    }
    
    @Test
    void shouldThrowExceptionWithNullMessage() {
        CardholderNotEligibleException exception = assertThrows(CardholderNotEligibleException.class, () -> {
            throw new CardholderNotEligibleException(null);
        });
        
        assertNull(exception.getMessage());
    }
    
    @Test
    void shouldThrowExceptionWithEmptyMessage() {
        CardholderNotEligibleException exception = assertThrows(CardholderNotEligibleException.class, () -> {
            throw new CardholderNotEligibleException("");
        });
        
        assertEquals("", exception.getMessage());
    }
}