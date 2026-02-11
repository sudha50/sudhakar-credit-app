package com.cardoffers.oms.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;

import com.cardoffers.oms.model.entity.OfferEligibility;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
class OfferEligibilityRepositoryTest {

    @Autowired
    private OfferEligibilityRepository offerEligibilityRepository;

    @Test
    @DisplayName("shouldReturnEligibleOffers_whenValidCardholderIdProvided")
    void shouldReturnEligibleOffers_whenValidCardholderIdProvided() {
        // Given
        Long cardholderId = 1L;
        OfferEligibility offer1 = new OfferEligibility(cardholderId, true);
        OfferEligibility offer2 = new OfferEligibility(cardholderId, true);
        offerEligibilityRepository.save(offer1);
        offerEligibilityRepository.save(offer2);

        // When
        List<OfferEligibility> results = offerEligibilityRepository.findByCardholderIdAndIsEligibleTrue(cardholderId);

        // Then
        assertNotNull(results);
        assertEquals(2, results.size());
    }

    @Test
    @DisplayName("shouldReturnEmptyList_whenNoEligibleOffersForCardholder")
    void shouldReturnEmptyList_whenNoEligibleOffersForCardholder() {
        // Given
        Long cardholderId = 2L;

        // When
        List<OfferEligibility> results = offerEligibilityRepository.findByCardholderIdAndIsEligibleTrue(cardholderId);

        // Then
        assertNotNull(results);
        assertEquals(0, results.size());
    }

    @Test
    @DisplayName("shouldReturnEmptyList_whenCardholderIdIsZero")
    void shouldReturnEmptyList_whenCardholderIdIsZero() {
        // When
        List<OfferEligibility> results = offerEligibilityRepository.findByCardholderIdAndIsEligibleTrue(0L);

        // Then
        assertNotNull(results);
        assertEquals(0, results.size());
    }

    @Test
    @DisplayName("shouldReturnEligibleOffers_whenCardholderHasInactiveOffers")
    void shouldReturnEligibleOffers_whenCardholderHasInactiveOffers() {
        // Given
        Long cardholderId = 3L;
        OfferEligibility offer1 = new OfferEligibility(cardholderId, true);
        OfferEligibility offer2 = new OfferEligibility(cardholderId, false);
        offerEligibilityRepository.save(offer1);
        offerEligibilityRepository.save(offer2);

        // When
        List<OfferEligibility> results = offerEligibilityRepository.findByCardholderIdAndIsEligibleTrue(cardholderId);

        // Then
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(offer1, results.get(0));
    }
}