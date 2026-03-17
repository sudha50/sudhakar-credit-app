package com.cardoffers.oms.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import com.cardoffers.oms.model.entity.OfferEligibility;
import com.cardoffers.oms.repository.OfferEligibilityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.junit.jupiter.api.Assertions.*;
import com.cardoffers.oms.model.entity.Cardholder;
import com.cardoffers.oms.model.entity.Offer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(properties = "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect")
@ActiveProfiles("test")
class OfferEligibilityRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OfferEligibilityRepository offerEligibilityRepository;

    @Test
void shouldFindByCardholderIdAndIsEligibleTrue_returnsResult() {
    // Given
    Offer offer = new Offer();
// Assuming id is set after persisting
    // Set other required fields for Offer
    entityManager.persist(offer);

    Cardholder cardholder = new Cardholder();
// Assuming id is set after persisting
    // Set other required fields for Cardholder
    entityManager.persist(cardholder);

    OfferEligibility offerEligibility = new OfferEligibility();
    offerEligibility.setOffer(offer);
    offerEligibility.setCardholder(cardholder);
    offerEligibility.setIsEligible(true);
    // Set other fields as needed
    entityManager.persistAndFlush(offerEligibility);

    // When
    List<OfferEligibility> result = offerEligibilityRepository.findByCardholderIdAndIsEligibleTrue(cardholder.getId());

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(offerEligibility.getId(), result.get(0).getId());
}

    @Test
void shouldFindByCardholderIdAndIsEligibleTrue_returnsEmpty() {
    // Given
    Long cardholderId = 1L; // example cardholderId

    // When
    List<OfferEligibility> result = offerEligibilityRepository.findByCardholderIdAndIsEligibleTrue(cardholderId);

    // Then
    assertTrue(result.isEmpty());
}

}