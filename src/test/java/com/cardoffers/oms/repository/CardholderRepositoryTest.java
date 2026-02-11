package com.cardoffers.oms.repository;

import com.cardoffers.oms.model.entity.Cardholder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@Transactional
class CardholderRepositoryTest {

    @Autowired
    private CardholderRepository cardholderRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @DisplayName("shouldReturnCardholder_whenEmailExists")
    void shouldReturnCardholder_whenEmailExists() {
        Cardholder cardholder = new Cardholder();
        cardholder.setEmail("test@example.com");
        cardholder.setActive(true);
        testEntityManager.persist(cardholder);
        testEntityManager.flush();

        Optional<Cardholder> found = cardholderRepository.findByEmail("test@example.com");
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("shouldReturnEmpty_whenEmailDoesNotExist")
    void shouldReturnEmpty_whenEmailDoesNotExist() {
        Optional<Cardholder> found = cardholderRepository.findByEmail("nonexistent@example.com");
        assertThat(found).isNotPresent();
    }

    @Test
    @DisplayName("shouldReturnActiveCardholders_whenThereAreActiveOnes")
    void shouldReturnActiveCardholders_whenThereAreActiveOnes() {
        Cardholder activeCardholder = new Cardholder();
        activeCardholder.setEmail("active@example.com");
        activeCardholder.setActive(true);
        testEntityManager.persist(activeCardholder);

        Cardholder inactiveCardholder = new Cardholder();
        inactiveCardholder.setEmail("inactive@example.com");
        inactiveCardholder.setActive(false);
        testEntityManager.persist(inactiveCardholder);

        List<Cardholder> activeCardholders = cardholderRepository.findByActiveTrue();
        assertThat(activeCardholders).hasSize(1);
        assertThat(activeCardholders.get(0).getEmail()).isEqualTo("active@example.com");
    }

    @Test
    @DisplayName("shouldReturnEmptyList_whenNoActiveCardholders")
    void shouldReturnEmptyList_whenNoActiveCardholders() {
        Cardholder inactiveCardholder = new Cardholder();
        inactiveCardholder.setEmail("inactive@example.com");
        inactiveCardholder.setActive(false);
        testEntityManager.persist(inactiveCardholder);

        List<Cardholder> activeCardholders = cardholderRepository.findByActiveTrue();
        assertThat(activeCardholders).isEmpty();
    }
}