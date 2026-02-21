package com.cardoffers.oms.model.dto;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MerchantDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldCreateMerchantDTOSuccessfully_whenAllFieldsAreValid() {
        MerchantDTO merchantDTO = new MerchantDTO();
        merchantDTO.setName("Valid Merchant");
        merchantDTO.setDescription("A description");
        merchantDTO.setCategory("Retail");
        merchantDTO.setLogoUrl("http://logo.url");
        merchantDTO.setWebsite("http://merchant.website");
        merchantDTO.setActive(true);

        Set<ConstraintViolation<MerchantDTO>> violations = validator.validate(merchantDTO);
        assertTrue(violations.isEmpty(), "MerchantDTO should be valid when all fields are set correctly");
    }

    @Test
    void shouldFailValidation_whenNameIsBlank() {
        MerchantDTO merchantDTO = new MerchantDTO();
        merchantDTO.setName("");
        merchantDTO.setCategory("Retail");

        Set<ConstraintViolation<MerchantDTO>> violations = validator.validate(merchantDTO);
        assertFalse(violations.isEmpty(), "MerchantDTO should not be valid when name is blank");
    }

    @Test
    void shouldFailValidation_whenCategoryIsBlank() {
        MerchantDTO merchantDTO = new MerchantDTO();
        merchantDTO.setName("Valid Merchant");
        merchantDTO.setCategory("");

        Set<ConstraintViolation<MerchantDTO>> violations = validator.validate(merchantDTO);
        assertFalse(violations.isEmpty(), "MerchantDTO should not be valid when category is blank");
    }

    @Test
    void shouldPassValidation_whenOnlyOptionalFieldsAreNullOrEmpty() {
        MerchantDTO merchantDTO = new MerchantDTO();
        merchantDTO.setName("Merchant Name");
        merchantDTO.setCategory("Category Name");

        Set<ConstraintViolation<MerchantDTO>> violations = validator.validate(merchantDTO);
        assertTrue(violations.isEmpty(), "MerchantDTO should be valid with only required fields set");
    }
}