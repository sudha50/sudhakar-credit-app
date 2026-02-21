package com.cardoffers.oms.model.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class CardholderDTOIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    private final Validator validator;

    public CardholderDTOIntegrationTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Test
    void testValidCardholderDTO() {
        CardholderDTO cardholder = new CardholderDTO();
        cardholder.setFirstName("John");
        cardholder.setLastName("Doe");
        cardholder.setEmail("john.doe@example.com");
        cardholder.setPhoneNumber("1234567890");
        cardholder.setActive(true);

        var violations = validator.validate(cardholder);
        assertTrue(violations.isEmpty(), "CardholderDTO should be valid");
    }

    @Test
    void testInvalidCardholderDTOWithoutEmail() {
        CardholderDTO cardholder = new CardholderDTO();
        cardholder.setFirstName("John");
        cardholder.setLastName("Doe");
        cardholder.setPhoneNumber("1234567890");
        cardholder.setActive(true);

        var violations = validator.validate(cardholder);
        assertFalse(violations.isEmpty(), "CardholderDTO should be invalid due to missing email");
    }
    
    @Test
    void testPersistCardholderDTO() {
        CardholderDTO cardholder = new CardholderDTO();
        cardholder.setFirstName("Jane");
        cardholder.setLastName("Smith");
        cardholder.setEmail("jane.smith@example.com");
        cardholder.setPhoneNumber("0987654321");
        cardholder.setActive(true);

        // Assuming a method to persist exists
        entityManager.persist(cardholder);
        entityManager.flush();

        // Verification
        CardholderDTO persistedCardholder = entityManager.find(CardholderDTO.class, cardholder.getId());
        assertNotNull(persistedCardholder);
        assertEquals("Jane", persistedCardholder.getFirstName());
        assertEquals("Smith", persistedCardholder.getLastName());
    }

    @Test
    void testInvalidCardholderDTOWithoutFirstName() {
        CardholderDTO cardholder = new CardholderDTO();
        cardholder.setLastName("Doe");
        cardholder.setEmail("john.doe@example.com");
        cardholder.setPhoneNumber("1234567890");
        cardholder.setActive(true);

        var violations = validator.validate(cardholder);
        assertFalse(violations.isEmpty(), "CardholderDTO should be invalid due to missing first name");
    }
}