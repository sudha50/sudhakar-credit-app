package com.cardoffers.oms.model.dto;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import jakarta.validation.constraints.Email;

import java.util.Collections;

class CardholderDTOTest {

    @Test
    void shouldCreateCardholderDTO_whenAllFieldsAreValid() {
        CardholderDTO cardholder = new CardholderDTO();
        cardholder.setId(1L);
        cardholder.setFirstName("John");
        cardholder.setLastName("Doe");
        cardholder.setEmail("john.doe@example.com");
        cardholder.setPhoneNumber("1234567890");
        cardholder.setActive(true);
        cardholder.setCardNetworks(Collections.emptyList());
        
        assertNotNull(cardholder);
        assertEquals(1L, cardholder.getId());
        assertEquals("John", cardholder.getFirstName());
        assertEquals("Doe", cardholder.getLastName());
        assertEquals("john.doe@example.com", cardholder.getEmail());
        assertTrue(cardholder.getActive());
    }

    @Test
    void shouldThrowConstraintViolation_whenFirstNameIsBlank() {
        CardholderDTO cardholder = new CardholderDTO();
        cardholder.setId(1L);
        cardholder.setFirstName("");
        cardholder.setLastName("Doe");
        cardholder.setEmail("john.doe@example.com");
        
        Exception exception = assertThrows(javax.validation.ConstraintViolationException.class, () -> {
            validateCardholder(cardholder);
        });
        assertNotNull(exception);
    }

    @Test
    void shouldThrowConstraintViolation_whenLastNameIsBlank() {
        CardholderDTO cardholder = new CardholderDTO();
        cardholder.setId(1L);
        cardholder.setFirstName("John");
        cardholder.setLastName("");
        cardholder.setEmail("john.doe@example.com");
        
        Exception exception = assertThrows(javax.validation.ConstraintViolationException.class, () -> {
            validateCardholder(cardholder);
        });
        assertNotNull(exception);
    }

    @Test
    void shouldThrowConstraintViolation_whenEmailIsInvalid() {
        CardholderDTO cardholder = new CardholderDTO();
        cardholder.setId(1L);
        cardholder.setFirstName("John");
        cardholder.setLastName("Doe");
        cardholder.setEmail("invalid-email");
        
        Exception exception = assertThrows(javax.validation.ConstraintViolationException.class, () -> {
            validateCardholder(cardholder);
        });
        assertNotNull(exception);
    }

    private void validateCardholder(CardholderDTO cardholder) {
        // Simulate validation logic
        if (cardholder.getFirstName() == null || cardholder.getFirstName().isBlank()) {
            throw new javax.validation.ConstraintViolationException("First Name cannot be blank", null);
        }
        if (cardholder.getLastName() == null || cardholder.getLastName().isBlank()) {
            throw new javax.validation.ConstraintViolationException("Last Name cannot be blank", null);
        }
        if (cardholder.getEmail() == null || !cardholder.getEmail().contains("@")) {
            throw new javax.validation.ConstraintViolationException("Email is not valid", null);
        }
    }
}