package com.cardoffers.oms.model.entity;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
        cardholder.setPhoneNumber("123-456-7890");
        cardholder.setActive(null);
    }

    @Test
    void shouldSetCreatedAndUpdatedTime_whenPrePersist() {
        cardholder.prePersist();
        assertNotNull(cardholder.getCreatedAt());
        assertNotNull(cardholder.getUpdatedAt());
        assertTrue(cardholder.getActive());
    }

    @Test
    void shouldUpdateUpdatedTime_whenPreUpdate() {
        LocalDateTime initialUpdatedAt = cardholder.getUpdatedAt();
        cardholder.preUpdate();
        assertNotEquals(initialUpdatedAt, cardholder.getUpdatedAt());
    }

    @Test
    void shouldSetActiveTrue_whenActiveIsNull() {
        cardholder.setActive(null);
        cardholder.prePersist();
        assertTrue(cardholder.getActive());
    }

    @Test
    void shouldNotChangeActive_whenActiveIsAlreadySet() {
        cardholder.setActive(false);
        cardholder.prePersist();
        assertFalse(cardholder.getActive());
    }
}