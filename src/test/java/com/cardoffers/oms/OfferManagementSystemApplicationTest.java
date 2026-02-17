package com.cardoffers.oms;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.junit.jupiter.api.Assertions.*;

class OfferManagementSystemApplicationTest {

    @Test
    void shouldStartApplication_whenMainMethodIsCalled() {
        String[] args = {};
        assertDoesNotThrow(() -> OfferManagementSystemApplication.main(args));
    }
}