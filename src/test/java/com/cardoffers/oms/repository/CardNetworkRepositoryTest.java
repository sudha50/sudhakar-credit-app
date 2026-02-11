package com.cardoffers.oms.repository;

import com.cardoffers.oms.model.entity.CardNetwork;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@DataJpaTest
class CardNetworkRepositoryTest {

    @Autowired
    private CardNetworkRepository cardNetworkRepository;

    @Test
    void shouldReturnCardNetwork_whenCodeExists() {
        // Given
        CardNetwork cardNetwork = new CardNetwork();
        cardNetwork.setCode("VISA");
        cardNetwork.setActive(true);
        cardNetworkRepository.save(cardNetwork);

        // When
        Optional<CardNetwork> result = cardNetworkRepository.findByCode("VISA");

        // Then
        assertTrue(result.isPresent());
        assertEquals("VISA", result.get().getCode());
    }

    @Test
    void shouldReturnEmpty_whenCodeDoesNotExist() {
        // When
        Optional<CardNetwork> result = cardNetworkRepository.findByCode("NON_EXISTENT_CODE");

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void shouldReturnActiveCardNetworks_whenThereAreActiveNetworks() {
        // Given
        CardNetwork activeNetwork = new CardNetwork();
        activeNetwork.setCode("MASTERCARD");
        activeNetwork.setActive(true);
        cardNetworkRepository.save(activeNetwork);
        
        CardNetwork inactiveNetwork = new CardNetwork();
        inactiveNetwork.setCode("AMEX");
        inactiveNetwork.setActive(false);
        cardNetworkRepository.save(inactiveNetwork);

        // When
        List<CardNetwork> result = cardNetworkRepository.findByActiveTrue();

        // Then
        assertEquals(1, result.size());
        assertEquals("MASTERCARD", result.get(0).getCode());
    }

    @Test
    void shouldReturnEmptyList_whenThereAreNoActiveCardNetworks() {
        // Given
        CardNetwork inactiveNetwork = new CardNetwork();
        inactiveNetwork.setCode("AMEX");
        inactiveNetwork.setActive(false);
        cardNetworkRepository.save(inactiveNetwork);

        // When
        List<CardNetwork> result = cardNetworkRepository.findByActiveTrue();

        // Then
        assertTrue(result.isEmpty());
    }
}