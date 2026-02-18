package com.cardoffers.oms.model.dto;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Collections;

class CardholderDTOTest {

    @Test
    void shouldCreateCardholderDTO_whenAllFieldsProvided() {
        CardholderDTO cardholder = new CardholderDTO();
        cardholder.setFirstName("John");
        cardholder.setLastName("Doe");
        cardholder.setEmail("john.doe@example.com");
        cardholder.setPhoneNumber("123-456-7890");
        cardholder.setActive(true);
        cardholder.setCardNetworks(Collections.emptyList());

        assertNotNull(cardholder);
        assertEquals("John", cardholder.getFirstName());
        assertEquals("Doe", cardholder.getLastName());
        assertEquals("john.doe@example.com", cardholder.getEmail());
        assertEquals("123-456-7890", cardholder.getPhoneNumber());
        assertTrue(cardholder.getActive());
        assertTrue(cardholder.getCardNetworks().isEmpty());
    }

    @Test
    void shouldThrowConstraintViolationException_whenFirstNameIsBlank() {
        CardholderDTO cardholder = new CardholderDTO();
        cardholder.setFirstName("");
        cardholder.setLastName("Doe");
        cardholder.setEmail("john.doe@example.com");

        assertThrows(javax.validation.ConstraintViolationException.class, () -> {
            validate(cardholder);
        });
    }

    @Test
    void shouldThrowConstraintViolationException_whenLastNameIsBlank() {
        CardholderDTO cardholder = new CardholderDTO();
        cardholder.setFirstName("John");
        cardholder.setLastName("");
        cardholder.setEmail("john.doe@example.com");

        assertThrows(javax.validation.ConstraintViolationException.class, () -> {
            validate(cardholder);
        });
    }

    @Test
    void shouldThrowConstraintViolationException_whenEmailIsBlank() {
        CardholderDTO cardholder = new CardholderDTO();
        cardholder.setFirstName("John");
        cardholder.setLastName("Doe");
        cardholder.setEmail("");

        assertThrows(javax.validation.ConstraintViolationException.class, () -> {
            validate(cardholder);
        });
    }

    @Test
    void shouldThrowConstraintViolationException_whenEmailIsInvalid() {
        CardholderDTO cardholder = new CardholderDTO();
        cardholder.setFirstName("John");
        cardholder.setLastName("Doe");
        cardholder.setEmail("invalid-email");

        assertThrows(javax.validation.ConstraintViolationException.class, () -> {
            validate(cardholder);
        });
    }

    @Test
    void shouldSetCardNetworks_whenProvided() {
        CardholderDTO cardholder = new CardholderDTO();
        CardNetworkDTO network = new CardNetworkDTO();
        network.setId(1L);  // Assuming there's a setId method for CardNetworkDTO
        cardholder.setCardNetworks(Collections.singletonList(network));

        assertNotNull(cardholder.getCardNetworks());
        assertEquals(1, cardholder.getCardNetworks().size());
        assertEquals(1L, cardholder.getCardNetworks().get(0).getId());
    }

    private void validate(CardholderDTO cardholder) {
        // Implementation of constraint validation logic goes here
        // You may use a Validator instance to perform validation
    }
}