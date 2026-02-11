package com.cardoffers.oms.repository;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.cardoffers.oms.model.entity.Offer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
class OfferRepositoryTest {

    @Autowired
    private OfferRepository offerRepository;

    @Mock
    private OfferRepository mockOfferRepository;

    @Test
    void shouldReturnActiveOffers_whenCurrentDateWithinRange() {
        LocalDate currentDate = LocalDate.now();
        Offer offer = new Offer(); // Add proper initialization here

        when(mockOfferRepository.findActiveOffers(currentDate)).thenReturn(List.of(offer));

        List<Offer> activeOffers = offerRepository.findActiveOffers(currentDate);
        Assertions.assertNotNull(activeOffers);
        Assertions.assertEquals(1, activeOffers.size());
    }

    @Test
    void shouldReturnEmptyList_whenNoActiveOffers() {
        LocalDate currentDate = LocalDate.now();

        when(mockOfferRepository.findActiveOffers(currentDate)).thenReturn(List.of());

        List<Offer> activeOffers = offerRepository.findActiveOffers(currentDate);
        Assertions.assertTrue(activeOffers.isEmpty());
    }

    @Test
    void shouldReturnActiveOffersByMerchant_whenMerchantIdExists() {
        Long merchantId = 1L;
        Offer offer = new Offer(); // Add proper initialization here

        when(mockOfferRepository.findActiveOffersByMerchant(merchantId)).thenReturn(List.of(offer));

        List<Offer> activeOffers = offerRepository.findActiveOffersByMerchant(merchantId);
        Assertions.assertNotNull(activeOffers);
        Assertions.assertEquals(1, activeOffers.size());
    }

    @Test
    void shouldReturnEmptyList_whenMerchantIdHasNoActiveOffers() {
        Long merchantId = 1L;

        when(mockOfferRepository.findActiveOffersByMerchant(merchantId)).thenReturn(List.of());

        List<Offer> activeOffers = offerRepository.findActiveOffersByMerchant(merchantId);
        Assertions.assertTrue(activeOffers.isEmpty());
    }

    @Test
    void shouldReturnActiveOffersByCardNetwork_whenNetworkIdExists() {
        Long networkId = 1L;
        Offer offer = new Offer(); // Add proper initialization here

        when(mockOfferRepository.findActiveOffersByCardNetwork(networkId)).thenReturn(List.of(offer));

        List<Offer> activeOffers = offerRepository.findActiveOffersByCardNetwork(networkId);
        Assertions.assertNotNull(activeOffers);
        Assertions.assertEquals(1, activeOffers.size());
    }

    @Test
    void shouldReturnEmptyList_whenNetworkIdHasNoActiveOffers() {
        Long networkId = 1L;

        when(mockOfferRepository.findActiveOffersByCardNetwork(networkId)).thenReturn(List.of());

        List<Offer> activeOffers = offerRepository.findActiveOffersByCardNetwork(networkId);
        Assertions.assertTrue(activeOffers.isEmpty());
    }
}