package com.cardoffers.oms.model.dto;

import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

@SpringBootTest
@ActiveProfiles("test")
public class MerchantDTOFunctionalTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldPassValidationWithAllFields() {
        MerchantDTO dto = new MerchantDTO();
        dto.setName("Test Merchant");
        dto.setCategory("Retail");
        dto.setDescription("Test Description");
        dto.setLogoUrl("http://example.com/logo.png");
        dto.setWebsite("http://example.com");
        dto.setActive(true);

        Set<ConstraintViolation<MerchantDTO>> violations = validator.validate(dto);
        assertEquals(0, violations.size());
    }

    @Test
    public void shouldFailValidationWhenNameIsBlank() {
        MerchantDTO dto = new MerchantDTO();
        dto.setName("");
        dto.setCategory("Retail");

        Set<ConstraintViolation<MerchantDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("must not be blank", violations.iterator().next().getMessage());
    }

    @Test
    public void shouldFailValidationWhenCategoryIsBlank() {
        MerchantDTO dto = new MerchantDTO();
        dto.setName("Test Merchant");
        dto.setCategory("");

        Set<ConstraintViolation<MerchantDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("must not be blank", violations.iterator().next().getMessage());
    }

    @Test
    public void shouldPassValidationWithAdditionalFieldsMissing() {
        MerchantDTO dto = new MerchantDTO();
        dto.setName("Test Merchant");
        dto.setCategory("Retail");

        Set<ConstraintViolation<MerchantDTO>> violations = validator.validate(dto);
        assertEquals(0, violations.size());
    }

    @Test
    public void shouldCreateMerchantDTOSuccessfully() {
        MerchantDTO dto = new MerchantDTO();
        dto.setName("Merchant Name");
        dto.setCategory("Food");

        assertNotNull(dto);
        assertEquals("Merchant Name", dto.getName());
        assertEquals("Food", dto.getCategory());
    }
}