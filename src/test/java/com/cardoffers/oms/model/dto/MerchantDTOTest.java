package com.cardoffers.oms.model.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MerchantDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldCreateMerchantDTO_whenAllFieldsAreValid() {
        MerchantDTO merchant = new MerchantDTO();
        merchant.setName("Test Merchant");
        merchant.setCategory("Retail");
        merchant.setDescription("A sample retail merchant.");
        merchant.setLogoUrl("http://example.com/logo.png");
        merchant.setWebsite("http://example.com");
        merchant.setActive(true);

        assertNotNull(merchant);
        assertEquals("Test Merchant", merchant.getName());
    }

    @Test
    void shouldNotCreateMerchantDTO_whenNameIsBlank() {
        MerchantDTO merchant = new MerchantDTO();
        merchant.setName("");
        merchant.setCategory("Retail");

        var violations = validator.validate(merchant);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldNotCreateMerchantDTO_whenCategoryIsBlank() {
        MerchantDTO merchant = new MerchantDTO();
        merchant.setName("Valid Merchant");
        merchant.setCategory("");

        var violations = validator.validate(merchant);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldAllowNullDescription_whenOtherFieldsAreValid() {
        MerchantDTO merchant = new MerchantDTO();
        merchant.setName("Test Merchant");
        merchant.setCategory("Retail");
        merchant.setDescription(null);

        var violations = validator.validate(merchant);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldSetActiveToFalse_whenMerchantIsInactive() {
        MerchantDTO merchant = new MerchantDTO();
        merchant.setName("Test Merchant");
        merchant.setCategory("Retail");
        merchant.setActive(false);

        assertFalse(merchant.getActive());
    }

    @Test
    void shouldReturnNullValuesForOptionalFields_whenNotSet() {
        MerchantDTO merchant = new MerchantDTO();
        merchant.setName("Test Merchant");
        merchant.setCategory("Retail");

        assertNull(merchant.getDescription());
        assertNull(merchant.getLogoUrl());
        assertNull(merchant.getWebsite());
    }
}