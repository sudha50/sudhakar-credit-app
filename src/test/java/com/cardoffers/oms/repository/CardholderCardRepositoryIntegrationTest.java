package com.cardoffers.oms.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.assertThat;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.cardoffers.oms.model.entity.CardholderCard;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
class CardholderCardRepositoryIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Autowired
    private CardholderCardRepository cardholderCardRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        cardholderCardRepository.deleteAll(); // Clear repository before each test
    }

    @Test
    void whenFindByCardholderId_thenCardholderCardsShouldBeFound() {
        CardholderCard card = new CardholderCard();
        card.setCardholderId(1L);
        card.setActive(true);
        entityManager.persist(card);
        entityManager.flush();

        List<CardholderCard> foundCards = cardholderCardRepository.findByCardholderId(1L);
        assertThat(foundCards).isNotEmpty();
        assertThat(foundCards.get(0).getCardholderId()).isEqualTo(1L);
    }

    @Test
    void whenFindByCardholderIdAndActiveTrue_thenOnlyActiveCardholderCardsShouldBeFound() {
        CardholderCard activeCard = new CardholderCard();
        activeCard.setCardholderId(2L);
        activeCard.setActive(true);
        entityManager.persist(activeCard);
        
        CardholderCard inactiveCard = new CardholderCard();
        inactiveCard.setCardholderId(2L);
        inactiveCard.setActive(false);
        entityManager.persist(inactiveCard);
        entityManager.flush();

        List<CardholderCard> foundCards = cardholderCardRepository.findByCardholderIdAndActiveTrue(2L);
        assertThat(foundCards).containsExactly(activeCard);
        assertThat(foundCards).doesNotContain(inactiveCard);
    }

    @Test
    void whenNoCardholderCardsExist_thenFindByCardholderIdReturnsEmptyList() {
        List<CardholderCard> foundCards = cardholderCardRepository.findByCardholderId(999L);
        assertThat(foundCards).isEmpty();
    }
}