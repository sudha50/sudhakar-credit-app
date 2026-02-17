package com.cardoffers.oms.repository;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.cardoffers.oms.model.entity.CardholderCard;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class CardholderCardRepositoryIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    private CardholderCardRepository cardholderCardRepository;

    @Test
    public void testFindByCardholderId() {
        CardholderCard card1 = new CardholderCard();
        card1.setCardholderId(1L);
        card1.setActive(true);
        cardholderCardRepository.save(card1);

        CardholderCard card2 = new CardholderCard();
        card2.setCardholderId(1L);
        card2.setActive(false);
        cardholderCardRepository.save(card2);

        List<CardholderCard> result = cardholderCardRepository.findByCardholderId(1L);
        assertThat(result).hasSize(2);
    }

    @Test
    public void testFindByCardholderIdAndActiveTrue() {
        CardholderCard card1 = new CardholderCard();
        card1.setCardholderId(2L);
        card1.setActive(true);
        cardholderCardRepository.save(card1);

        CardholderCard card2 = new CardholderCard();
        card2.setCardholderId(2L);
        card2.setActive(false);
        cardholderCardRepository.save(card2);

        List<CardholderCard> result = cardholderCardRepository.findByCardholderIdAndActiveTrue(2L);
        assertEquals(1, result.size());
        assertEquals(true, result.get(0).isActive());
    }
}