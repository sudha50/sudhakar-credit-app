package com.cardoffers.oms;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class OfferManagementSystemApplicationTest {

    @Test
    void shouldStartApplication_whenMainIsCalled() {
        String[] args = {};
        assertNotNull(SpringApplication.run(OfferManagementSystemApplication.class, args));
    }
}