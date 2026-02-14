package com.cardoffers.oms.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.cardoffers.oms.model.entity.Cardholder;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CardholderRepositoryTest {

    @Autowired 
    TestEntityManager entityManager;

    @Autowired 
    CardholderRepository cardholderRepository;

    private static Cardholder aCardholder() {
        Cardholder entity = new Cardholder();
        entity.setId(1L);
        entity.setFirstName("John");
        entity.setLastName("Doe");
        entity.setEmail("test@example.com");
        entity.setPhoneNumber("+1234567890");
        entity.setCreatedAt(LocalDateTime.of(2025, 1, 15, 10, 30));
        entity.setUpdatedAt(LocalDateTime.of(2025, 1, 15, 10, 30));
        entity.setActive(true);
        entity.setNow(LocalDateTime.of(2025, 1, 15, 10, 30));
        entity.setCards(null);
        return entity;
    }

    @Test
    void shouldReturnCardholder_whenEmailExists() {
        Cardholder cardholder = aCardholder();
        entityManager.persist(cardholder);
        entityManager.flush();

        var found = cardholderRepository.findByEmail("test@example.com");

        assertTrue(found.isPresent());
        assertEquals("John", found.get().getFirstName());
    }

    @Test
    void shouldReturnEmpty_whenEmailDoesNotExist() {
        var found = cardholderRepository.findByEmail("nonexistent@example.com");
        
        assertFalse(found.isPresent());
    }

    @Test
    void shouldReturnActiveCardholders_whenCalled() {
        Cardholder activeCardholder = aCardholder();
        entityManager.persist(activeCardholder);
        
        Cardholder inactiveCardholder = aCardholder();
        inactiveCardholder.setActive(false);
        entityManager.persist(inactiveCardholder);
        
        entityManager.flush();

        List<Cardholder> activeCardholders = cardholderRepository.findByActiveTrue();
        
        assertEquals(1, activeCardholders.size());
        assertEquals("John", activeCardholders.get(0).getFirstName());
    }

    @Test
    void shouldReturnEmptyList_whenNoActiveCardholders() {
        Cardholder inactiveCardholder = aCardholder();
        inactiveCardholder.setActive(false);
        entityManager.persist(inactiveCardholder);
        
        entityManager.flush();

        List<Cardholder> activeCardholders = cardholderRepository.findByActiveTrue();
        
        assertTrue(activeCardholders.isEmpty());
    }
}