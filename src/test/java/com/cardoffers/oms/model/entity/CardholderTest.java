package com.cardoffers.oms.model.entity;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CardholderTest {

    private Cardholder cardholder;

    @BeforeEach
    void setUp() {
        cardholder = new Cardholder();
        cardholder.setFirstName("John");
        cardholder.setLastName("Doe");
        cardholder.setEmail("john.doe@example.com");
        cardholder.setPhoneNumber("1234567890");
        cardholder.setActive(null);
    }

    @Test
    void shouldSetTimestampsAndActiveTrue_whenPrePersistCalled() {
        cardholder.prePersist();

        assertNotNull(cardholder.getCreatedAt());
        assertNotNull(cardholder.getUpdatedAt());
        assertTrue(cardholder.getActive());
    }

    @Test
    void shouldKeepActiveTrue_whenPrePersistCalledWithActiveTrue() {
        cardholder.setActive(true);
        cardholder.prePersist();

        assertTrue(cardholder.getActive());
    }

    @Test
    void shouldUpdateTimestamp_whenPreUpdateCalled() {
        LocalDateTime initialUpdatedAt = LocalDateTime.now().minusDays(1);
        cardholder.setUpdatedAt(initialUpdatedAt);

        cardholder.preUpdate();

        assertNotEquals(initialUpdatedAt, cardholder.getUpdatedAt());
    }

    @Test
    void shouldSetUpdatedAtOnly_whenPreUpdateCalled() {
        LocalDateTime beforeUpdate = cardholder.getUpdatedAt();
        cardholder.preUpdate();

        assertNotEquals(beforeUpdate, cardholder.getUpdatedAt());
    }
}