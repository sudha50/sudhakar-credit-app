package com.cardoffers.oms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.BeforeAllCallback;
import org.springframework.boot.testcontainers.DynamicPropertyRegistry;
import org.springframework.boot.testcontainers.DynamicPropertySource;
import org.springframework.boot.testcontainers.Testcontainers;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

import com.cardoffers.oms.model.dto.CardholderDTO;
import com.cardoffers.oms.model.entity.Cardholder;
import com.cardoffers.oms.repository.CardholderRepository;

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CardholderServiceImplIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CardholderRepository cardholderRepository;

    @Test
    public void testCreateCardholder() {
        CardholderDTO dto = new CardholderDTO();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("john.doe@example.com");
        dto.setPhoneNumber("1234567890");
        dto.setActive(true);

        ResponseEntity<CardholderDTO> response = restTemplate.postForEntity("/cardholders", dto, CardholderDTO.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John", response.getBody().getFirstName());

        Optional<Cardholder> cardholder = cardholderRepository.findByEmail("john.doe@example.com");
        assertTrue(cardholder.isPresent());
        assertEquals("Doe", cardholder.get().getLastName());
    }

    @Test
    public void testGetCardholderById_Success() {
        CardholderDTO dto = new CardholderDTO();
        dto.setFirstName("Alice");
        dto.setLastName("Smith");
        dto.setEmail("alice.smith@example.com");
        dto.setPhoneNumber("0987654321");
        dto.setActive(true);

        Cardholder createdCardholder = cardholderRepository.save(new Cardholder(dto));
        Long id = createdCardholder.getId();

        ResponseEntity<CardholderDTO> response = restTemplate.getForEntity("/cardholders/" + id, CardholderDTO.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Alice", response.getBody().getFirstName());
    }

    @Test
    public void testGetCardholderByEmail_NotFound() {
        ResponseEntity<CardholderDTO> response = restTemplate.getForEntity("/cardholders/email/notfound@example.com", CardholderDTO.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateCardholder() {
        CardholderDTO dto = new CardholderDTO();
        dto.setFirstName("Bob");
        dto.setLastName("Brown");
        dto.setEmail("bob.brown@example.com");
        dto.setPhoneNumber("1122334455");
        dto.setActive(true);

        Cardholder savedCardholder = cardholderRepository.save(new Cardholder(dto));
        Long id = savedCardholder.getId();

        dto.setFirstName("Robert");
        HttpEntity<CardholderDTO> requestEntity = new HttpEntity<>(dto);
        ResponseEntity<CardholderDTO> response = restTemplate.exchange("/cardholders/" + id, HttpMethod.PUT, requestEntity, CardholderDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Robert", response.getBody().getFirstName());
    }
}