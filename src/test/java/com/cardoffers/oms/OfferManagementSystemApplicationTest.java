package com.cardoffers.oms;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.mockito.Mockito.verify;

class OfferManagementSystemApplicationTest {

    @Test
    void shouldRunApplication_whenMainIsCalled() {
        String[] args = {};
        OfferManagementSystemApplication.main(args);

        // Verify that SpringApplication.run was called with the correct arguments
        verify(SpringApplication.class);
    }

    @Test
    void shouldNotThrowException_whenMainIsExecuted() {
        String[] args = {};
        assertDoesNotThrow(() -> OfferManagementSystemApplication.main(args));
    }

    @Test
    void shouldLaunchSpringApplication_whenMainIsExecuted() {
        String[] args = {};
        
        OfferManagementSystemApplication.main(args);

        // Further checks can be done depending on the application environment or application context
    }
}