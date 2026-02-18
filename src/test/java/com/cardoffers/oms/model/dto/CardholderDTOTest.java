package com.cardoffers.oms.model.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static java.util.Collections.singletonList;

class CardholderDTOTest {
    private final Validator validator;

    public CardholderDTOTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

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
        cardholder.setFirstName(""); // blank first name
        cardholder.setLastName("Doe");
        cardholder.setEmail("john.doe@example.com");

        assertThrows(javax.validation.ConstraintViolationException.class, () -> validate(cardholder));
    }

    @Test
    void shouldThrowConstraintViolationException_whenLastNameIsBlank() {
        CardholderDTO cardholder = new CardholderDTO();
        cardholder.setFirstName("John");
        cardholder.setLastName(""); // blank last name
        cardholder.setEmail("john.doe@example.com");

        assertThrows(javax.validation.ConstraintViolationException.class, () -> validate(cardholder));
    }

    @Test
    void shouldThrowConstraintViolationException_whenEmailIsBlank() {
        CardholderDTO cardholder = new CardholderDTO();
        cardholder.setFirstName("John");
        cardholder.setLastName("Doe");
        cardholder.setEmail(""); // blank email

        assertThrows(javax.validation.ConstraintViolationException.class, () -> validate(cardholder));
    }

    @Test
    void shouldThrowConstraintViolationException_whenEmailIsInvalid() {
        CardholderDTO cardholder = new CardholderDTO();
        cardholder.setFirstName("John");
        cardholder.setLastName("Doe");
        cardholder.setEmail("invalid-email"); // invalid email

        assertThrows(javax.validation.ConstraintViolationException.class, () -> validate(cardholder));
    }

    @Test
    void shouldSetCardNetworks_whenProvided() {
        CardholderDTO cardholder = new CardholderDTO();
        CardNetworkDTO network = new CardNetworkDTO();
        network.setId(1L);  // Assuming there's a setId method for CardNetworkDTO
        cardholder.setCardNetworks(singletonList(network));

        assertNotNull(cardholder.getCardNetworks());
        assertEquals(1, cardholder.getCardNetworks().size());
        assertEquals(1L, cardholder.getCardNetworks().get(0).getId());
    }

    private void validate(CardholderDTO cardholder) {
        var violations = validator.validate(cardholder);
        if (!violations.isEmpty()) {
            throw new javax.validation.ConstraintViolationException(violations);
        }
    }
}