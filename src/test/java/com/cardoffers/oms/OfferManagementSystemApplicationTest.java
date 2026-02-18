package com.cardoffers.oms;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class OfferManagementSystemApplicationTest {

    @Test
    void shouldStartApplication_whenMainIsCalled() {
        String[] args = {};
        assertThrows(Exception.class, () -> {
            OfferManagementSystemApplication.main(args);
        });
    }
}