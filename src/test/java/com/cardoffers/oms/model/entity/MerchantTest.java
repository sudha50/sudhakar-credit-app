package com.cardoffers.oms.model.entity;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MerchantTest {

    private Merchant merchant;

    @BeforeEach
    void setUp() {
        merchant = new Merchant();
        merchant.setName("Test Merchant");
        merchant.setCategory("Retail");
        merchant.setDescription("A merchant for testing purposes");
        merchant.setLogoUrl("http://example.com/logo.png");
        merchant.setWebsite("http://example.com");
        merchant.setActive(null);
    }

    @Test
    void shouldSetCreatedAtAndActive_whenPrePersist() {
        merchant.prePersist();
        assertNotNull(merchant.getCreatedAt(), "Created at should not be null after prePersist");
        assertTrue(merchant.getActive(), "Active should default to true if null");
    }

    @Test
    void shouldRetainExistingActiveValue_whenPrePersistCalled() {
        merchant.setActive(false);
        merchant.prePersist();
        assertFalse(merchant.getActive(), "Active should retain its existing value");
    }

    @Test
    void shouldNotThrowException_whenAllRequiredFieldsAreSet() {
        merchant.setActive(true);
        assertDoesNotThrow(merchant::prePersist, "prePersist should not throw exception");
    }

    @Test
    void shouldThrowException_whenNameIsNull() {
        merchant.setName(null);
        Exception exception = assertThrows(IllegalArgumentException.class, merchant::prePersist);
        assertEquals("Name cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowException_whenCategoryIsNull() {
        merchant.setCategory(null);
        Exception exception = assertThrows(IllegalArgumentException.class, merchant::prePersist);
        assertEquals("Category cannot be null", exception.getMessage());
    }

    @Test
    void shouldNotThrowException_whenDescriptionIsNull() {
        merchant.setDescription(null);
        assertDoesNotThrow(merchant::prePersist, "prePersist should handle null description gracefully");
    }

    @Test
    void shouldNotThrowException_whenLogoUrlIsNull() {
        merchant.setLogoUrl(null);
        assertDoesNotThrow(merchant::prePersist, "prePersist should handle null logo URL gracefully");
    }
}