package com.cardoffers.oms.repository;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.cardoffers.oms.model.entity.CardholderCard;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
public class CardholderCardRepositoryIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword("password");

    @DynamicPropertySource
    static void setDatabaseProperties(org.springframework.core.env.ConfigurableEnvironment environment) {
        environment.addPropertySource(new org.springframework.core.env.MapPropertySource("testcontainers",
                java.util.Map.of(
                    "spring.datasource.url", postgresContainer.getJdbcUrl(),
                    "spring.datasource.username", postgresContainer.getUsername(),
                    "spring.datasource.password", postgresContainer.getPassword()
                )));
    }

    @Autowired
    private CardholderCardRepository cardholderCardRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testFindByCardholderId() {
        CardholderCard card1 = new CardholderCard();
        card1.setCardholderId(1L);
        card1.setActive(true);
        card1.setCardNumber("1234-5678-9876-5432");
        entityManager.persist(card1);

        CardholderCard card2 = new CardholderCard();
        card2.setCardholderId(1L);
        card2.setActive(false);
        card2.setCardNumber("5678-1234-8765-4321");
        entityManager.persist(card2);

        List<CardholderCard> cards = cardholderCardRepository.findByCardholderId(1L);
        assertEquals(2, cards.size());
    }

    @Test
    public void testFindByCardholderIdAndActiveTrue() {
        CardholderCard activeCard = new CardholderCard();
        activeCard.setCardholderId(2L);
        activeCard.setActive(true);
        activeCard.setCardNumber("1234-5678-1234-5678");
        entityManager.persist(activeCard);

        CardholderCard inactiveCard = new CardholderCard();
        inactiveCard.setCardholderId(2L);
        inactiveCard.setActive(false);
        inactiveCard.setCardNumber("8765-4321-8765-4321");
        entityManager.persist(inactiveCard);

        List<CardholderCard> activeCards = cardholderCardRepository.findByCardholderIdAndActiveTrue(2L);
        assertThat(activeCards).hasSize(1);
        assertThat(activeCards.get(0).getCardNumber()).isEqualTo("1234-5678-1234-5678");
    }

    @Test
    public void testNoCardsForInvalidCardholderId() {
        List<CardholderCard> cards = cardholderCardRepository.findByCardholderId(99L);
        assertThat(cards).isEmpty();
    }
}