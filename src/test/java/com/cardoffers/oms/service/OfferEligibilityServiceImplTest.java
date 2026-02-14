package com.cardoffers.oms.service;

import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cardoffers.oms.exception.CardholderNotEligibleException;
import com.cardoffers.oms.exception.ResourceNotFoundException;
import com.cardoffers.oms.mapper.OfferMapper;
import com.cardoffers.oms.model.dto.EligibleOfferResponseDTO;
import com.cardoffers.oms.model.dto.OfferDTO;
import com.cardoffers.oms.model.entity.Cardholder;
import com.cardoffers.oms.model.entity.CardholderCard;
import com.cardoffers.oms.model.entity.Offer;
import com.cardoffers.oms.repository.CardholderCardRepository;
import com.cardoffers.oms.repository.CardholderRepository;
import com.cardoffers.oms.repository.OfferRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
class OfferEligibilityServiceImplTest {

    @Mock
    CardholderRepository cardholderRepository;
    
    @Mock
    CardholderCardRepository cardholderCardRepository;

    @Mock
    OfferRepository offerRepository;

    @Mock
    OfferMapper offerMapper;

    @Mock
    OfferEligibilityService offerEligibilityService;

    @InjectMocks
    OfferEligibilityServiceImpl offerEligibilityServiceImpl;

    @Test
    void shouldReturnEligibleOffers_whenCardholderHasActiveCards() {
        Long cardholderId = 1L;
        Cardholder cardholder = aCardholder();
        when(cardholderRepository.findById(cardholderId)).thenReturn(Optional.of(cardholder));

        CardholderCard card = aCardholderCard();
        card.setCardholder(cardholder);
        card.setActive(true);
        when(cardholderCardRepository.findByCardholderIdAndActiveTrue(cardholderId)).thenReturn(List.of(card));

        Offer offer = new Offer();
        offer.setCardNetwork(card.getCardNetwork());
        offer.setMaxRedemptions(10);
        offer.setCurrentRedemptions(5);
        when(offerRepository.findActiveOffers(LocalDate.now())).thenReturn(List.of(offer));

        OfferDTO offerDTO = new OfferDTO();
        when(offerMapper.toDTO(offer)).thenReturn(offerDTO);

        EligibleOfferResponseDTO response = offerEligibilityServiceImpl.getEligibleOffersForCardholder(cardholderId);

        assertNotNull(response);
        assertEquals(1, response.getTotalOffers());
        assertEquals("John Doe", response.getCardholderName());
    }

    @Test
    void shouldThrowCardholderNotEligibleException_whenCardholderHasNoActiveCards() {
        Long cardholderId = 1L;
        Cardholder cardholder = aCardholder();
        when(cardholderRepository.findById(cardholderId)).thenReturn(Optional.of(cardholder));
        when(cardholderCardRepository.findByCardholderIdAndActiveTrue(cardholderId)).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(CardholderNotEligibleException.class, () -> {
            offerEligibilityServiceImpl.getEligibleOffersForCardholder(cardholderId);
        });

        assertEquals("Cardholder has no active cards", exception.getMessage());
    }

    @Test
    void shouldThrowResourceNotFoundException_whenCardholderDoesNotExist() {
        Long cardholderId = 1L;
        when(cardholderRepository.findById(cardholderId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            offerEligibilityServiceImpl.getEligibleOffersForCardholder(cardholderId);
        });

        assertEquals("Cardholder not found", exception.getMessage());
    }

    @Test
    void shouldFilterOffersByCategory_whenCategoryIsValid() {
        Long cardholderId = 1L;
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setMerchant(new MerchantDTO());
        offerDTO.getMerchant().setCategory("Grocery");
        when(offerEligibilityService.getEligibleOffersForCardholder(cardholderId)).thenReturn(anEligibleOfferResponseDTO(List.of(offerDTO)));

        List<OfferDTO> filteredOffers = offerEligibilityServiceImpl.filterOffersByCategory(cardholderId, "Grocery");

        assertEquals(1, filteredOffers.size());
        assertEquals("Grocery", filteredOffers.get(0).getMerchant().getCategory());
    }

    @Test
    void shouldReturnEmptyList_whenNoOffersMatchCategory() {
        Long cardholderId = 1L;
        when(offerEligibilityService.getEligibleOffersForCardholder(cardholderId)).thenReturn(anEligibleOfferResponseDTO(Collections.emptyList()));

        List<OfferDTO> filteredOffers = offerEligibilityServiceImpl.filterOffersByCategory(cardholderId, "Grocery");

        assertTrue(filteredOffers.isEmpty());
    }

    @Test
    void shouldFilterOffersByOfferType_whenOfferTypeIsValid() {
        Long cardholderId = 1L;
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setOfferType("Cashback");
        when(offerEligibilityService.getEligibleOffersForCardholder(cardholderId)).thenReturn(anEligibleOfferResponseDTO(List.of(offerDTO)));

        List<OfferDTO> filteredOffers = offerEligibilityServiceImpl.filterOffersByOfferType(cardholderId, "Cashback");

        assertEquals(1, filteredOffers.size());
        assertEquals("Cashback", filteredOffers.get(0).getOfferType());
    }

    private static Cardholder aCardholder() {
        Cardholder entity = new Cardholder();
        entity.setId(1L);
        entity.setFirstName("John");
        entity.setLastName("Doe");
        entity.setEmail("test@example.com");
        entity.setPhoneNumber("+1234567890");
        entity.setCreatedAt(LocalDateTime.of(2025, 1, 15, 10, 30));
        entity.setUpdatedAt(LocalDateTime.of(2025, 1, 15, 10, 30));
        entity.setActive(true);
        return entity;
    }

    private static CardholderCard aCardholderCard() {
        CardholderCard entity = new CardholderCard();
        entity.setId(1L);
        entity.setCardholder(aCardholder());
        entity.setCardNetwork(new CardNetwork());
        entity.setCardNumberLastFour("1234");
        entity.setCardType("Visa");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setActive(true);
        return entity;
    }

    private static EligibleOfferResponseDTO anEligibleOfferResponseDTO(List<OfferDTO> offers) {
        EligibleOfferResponseDTO entity = new EligibleOfferResponseDTO();
        entity.setEligibleOffers(offers);
        entity.setTotalOffers(offers.size());
        return entity;
    }
}