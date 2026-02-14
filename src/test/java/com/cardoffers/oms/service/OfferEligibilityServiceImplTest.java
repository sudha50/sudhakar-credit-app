package com.cardoffers.oms.service;

import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
import java.util.HashSet;

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
    void shouldReturnEligibleOffersForCardholder_whenValidCardholderId() {
        Long cardholderId = 1L;
        Cardholder cardholder = aCardholder();
        List<CardholderCard> activeCards = Arrays.asList(aCardholderCard());
        List<Offer> offers = Arrays.asList(new Offer());

        when(cardholderRepository.findById(cardholderId)).thenReturn(java.util.Optional.of(cardholder));
        when(cardholderCardRepository.findByCardholderIdAndActiveTrue(cardholderId)).thenReturn(activeCards);
        when(offerRepository.findActiveOffers(LocalDate.now())).thenReturn(offers);
        when(offerMapper.toDTO(any())).thenReturn(new OfferDTO());

        EligibleOfferResponseDTO response = offerEligibilityServiceImpl.getEligibleOffersForCardholder(cardholderId);

        assertNotNull(response);
        assertEquals(cardholderId, response.getCardholderId());
        assertEquals("John Doe", response.getCardholderName());
        assertEquals(1, response.getTotalOffers());
    }

    @Test
    void shouldThrowResourceNotFoundException_whenCardholderNotFound() {
        Long cardholderId = 1L;

        when(cardholderRepository.findById(cardholderId)).thenReturn(java.util.Optional.empty());

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () ->
                offerEligibilityServiceImpl.getEligibleOffersForCardholder(cardholderId));
        assertEquals("Cardholder not found", thrown.getMessage());
    }

    @Test
    void shouldThrowCardholderNotEligibleException_whenNoActiveCards() {
        Long cardholderId = 1L;
        Cardholder cardholder = aCardholder();

        when(cardholderRepository.findById(cardholderId)).thenReturn(java.util.Optional.of(cardholder));
        when(cardholderCardRepository.findByCardholderIdAndActiveTrue(cardholderId)).thenReturn(Collections.emptyList());

        CardholderNotEligibleException thrown = assertThrows(CardholderNotEligibleException.class, () ->
                offerEligibilityServiceImpl.getEligibleOffersForCardholder(cardholderId));
        assertEquals("Cardholder has no active cards", thrown.getMessage());
    }

    @Test
    void shouldFilterOffersByCategory_whenValidCategory() {
        Long cardholderId = 1L;
        String category = "RETAIL";
        List<OfferDTO> offers = Arrays.asList(new OfferDTO());

        when(offerEligibilityService.getEligibleOffersForCardholder(cardholderId)).thenReturn(anEligibleOfferResponseDTO().setEligibleOffers(offers));

        List<OfferDTO> filteredOffers = offerEligibilityServiceImpl.filterOffersByCategory(cardholderId, category);

        assertNotNull(filteredOffers);
        assertEquals(offers.size(), filteredOffers.size());
    }

    @Test
    void shouldReturnAllOffers_whenCategoryIsNull() {
        Long cardholderId = 1L;
        List<OfferDTO> offers = Arrays.asList(new OfferDTO());

        when(offerEligibilityService.getEligibleOffersForCardholder(cardholderId)).thenReturn(anEligibleOfferResponseDTO().setEligibleOffers(offers));

        List<OfferDTO> filteredOffers = offerEligibilityServiceImpl.filterOffersByCategory(cardholderId, null);

        assertNotNull(filteredOffers);
        assertEquals(offers.size(), filteredOffers.size());
    }

    @Test
    void shouldFilterOffersByOfferType_whenValidOfferType() {
        Long cardholderId = 1L;
        String offerType = "DISCOUNT";
        List<OfferDTO> offers = Arrays.asList(new OfferDTO());

        when(offerEligibilityService.getEligibleOffersForCardholder(cardholderId)).thenReturn(anEligibleOfferResponseDTO().setEligibleOffers(offers));

        List<OfferDTO> filteredOffers = offerEligibilityServiceImpl.filterOffersByOfferType(cardholderId, offerType);

        assertNotNull(filteredOffers);
        assertEquals(offers.size(), filteredOffers.size());
    }

    @Test
    void shouldReturnAllOffers_whenOfferTypeIsNull() {
        Long cardholderId = 1L;
        List<OfferDTO> offers = Arrays.asList(new OfferDTO());

        when(offerEligibilityService.getEligibleOffersForCardholder(cardholderId)).thenReturn(anEligibleOfferResponseDTO().setEligibleOffers(offers));

        List<OfferDTO> filteredOffers = offerEligibilityServiceImpl.filterOffersByOfferType(cardholderId, null);

        assertNotNull(filteredOffers);
        assertEquals(offers.size(), filteredOffers.size());
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
        entity.setCardType("VISA");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setActive(true);
        return entity;
    }

    private static EligibleOfferResponseDTO anEligibleOfferResponseDTO() {
        EligibleOfferResponseDTO entity = new EligibleOfferResponseDTO();
        return entity;
    }
}