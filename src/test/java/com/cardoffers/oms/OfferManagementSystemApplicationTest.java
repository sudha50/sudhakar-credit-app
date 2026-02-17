package com.cardoffers.oms;

import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@ActiveProfiles("test")
class OfferManagementSystemApplicationTest {

    @Test
    void shouldStartApplication_whenMainIsCalled() {
        String[] args = {};
        assertDoesNotThrow(() -> OfferManagementSystemApplication.main(args));
    }
}