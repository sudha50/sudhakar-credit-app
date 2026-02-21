package com.cardoffers.oms.model.entity;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CardNetworkTest {

    @Test
    void shouldSetCreatedAtAndActiveTrue_whenPrePersistIsCalled() {
        // Arrange
        CardNetwork cardNetwork = new CardNetwork();
        cardNetwork.setName("Visa");
        cardNetwork.setCode("VISA");

        // Act
        cardNetwork.prePersist();

        // Assert
        assertNotNull(cardNetwork.getCreatedAt(), "Created at should not be null after prePersist");
        assertTrue(cardNetwork.getActive(), "Active should be true after prePersist when it was null");
    }

    @Test
    void shouldSetCreatedAtAndKeepActiveTrue_whenPrePersistIsCalledWithActiveTrue() {
        // Arrange
        CardNetwork cardNetwork = new CardNetwork();
        cardNetwork.setName("MasterCard");
        cardNetwork.setCode("MC");
        cardNetwork.setActive(true);

        // Act
        cardNetwork.prePersist();

        // Assert
        assertNotNull(cardNetwork.getCreatedAt(), "Created at should not be null after prePersist");
        assertTrue(cardNetwork.getActive(), "Active should remain true after prePersist");
    }

    @Test
    void shouldSetCreatedAtAndSetActiveFalse_whenPrePersistIsCalledWithActiveFalse() {
        // Arrange
        CardNetwork cardNetwork = new CardNetwork();
        cardNetwork.setName("American Express");
        cardNetwork.setCode("AMEX");
        cardNetwork.setActive(false);

        // Act
        cardNetwork.prePersist();

        // Assert
        assertNotNull(cardNetwork.getCreatedAt(), "Created at should not be null after prePersist");
        assertFalse(cardNetwork.getActive(), "Active should remain false after prePersist");
    }

    @Test
    void shouldSetActiveTrue_whenPrePersistIsCalledAndActiveIsExplicitlyNull() {
        // Arrange
        CardNetwork cardNetwork = new CardNetwork();
        cardNetwork.setName("Discover");
        cardNetwork.setCode("DISC");
        cardNetwork.setActive(null);

        // Act
        cardNetwork.prePersist();

        // Assert
        assertNotNull(cardNetwork.getCreatedAt(), "Created at should not be null after prePersist");
        assertTrue(cardNetwork.getActive(), "Active should be set to true after prePersist when it was null");
    }
}