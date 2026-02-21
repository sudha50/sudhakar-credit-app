package com.cardoffers.oms;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class OfferManagementSystemApplicationTest {

    @Test
    void shouldRunApplicationWithoutExceptions_whenMainIsCalled() {
        // Assert that executing the main method doesn't throw any exceptions
        assertDoesNotThrow(() -> OfferManagementSystemApplication.main(new String[] {}));
    }
}