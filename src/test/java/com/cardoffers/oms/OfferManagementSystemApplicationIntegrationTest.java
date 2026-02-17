package com.cardoffers.oms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class OfferManagementSystemApplicationIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
        // Ensure the application context loads successfully
        assertNotNull(restTemplate);
    }

    @Test
    void shouldReturnOkStatusForDefaultEndpoint() {
        ResponseEntity<String> response = restTemplate.getForEntity("/", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldReturnNotFoundForInvalidEndpoint() {
        ResponseEntity<String> response = restTemplate.getForEntity("/invalid-endpoint", String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}