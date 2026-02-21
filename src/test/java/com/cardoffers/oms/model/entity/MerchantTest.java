package com.cardoffers.oms.model.entity;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.PrePersist;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MerchantTest {

    @Test
    void shouldSetCreatedAtAndActive_whenPrePersistIsCalled() {
        // Arrange
        Merchant merchant = new Merchant();
        merchant.setName("Test Merchant");
        merchant.setCategory("Retail");
        merchant.setDescription("Test description");

        // Act
        merchant.prePersist();

        // Assert
        assertNotNull(merchant.getCreatedAt());
        assertTrue(merchant.getActive());
    }

    @Test
    void shouldSetActiveToTrue_whenActiveIsNullOnPrePersist() {
        // Arrange
        Merchant merchant = new Merchant();
        merchant.setName("Test Merchant");
        merchant.setCategory("Retail");
        merchant.setDescription("Test description");
        merchant.setActive(null);

        // Act
        merchant.prePersist();

        // Assert
        assertTrue(merchant.getActive());
    }

    @Test
    void shouldNotChangeActive_whenActiveIsTrueOnPrePersist() {
        // Arrange
        Merchant merchant = new Merchant();
        merchant.setName("Test Merchant");
        merchant.setCategory("Retail");
        merchant.setDescription("Test description");
        merchant.setActive(true);

        // Act
        merchant.prePersist();

        // Assert
        assertTrue(merchant.getActive());
    }

    @Test
    void shouldNotChangeActive_whenActiveIsFalseOnPrePersist() {
        // Arrange
        Merchant merchant = new Merchant();
        merchant.setName("Test Merchant");
        merchant.setCategory("Retail");
        merchant.setDescription("Test description");
        merchant.setActive(false);

        // Act
        merchant.prePersist();

        // Assert
        assertFalse(merchant.getActive());
    }
}