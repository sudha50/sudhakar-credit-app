package com.cardoffers.oms.service;

import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

    private static Cardholder aCardholder() {
        Cardholder entity = new Cardholder();
        entity.setId(1L);
        entity.setFirstName("John");
        entity.setLastName("Doe");
        entity.setEmail("test@example.com");
        entity.setPhoneNumber("+1234567890");
        return entity;
    }

    private static CardholderCard aCardholderCard() {
        CardholderCard entity = new CardholderCard();
        entity.setId(1L);
        entity.setCardholder(aCardholder());
        entity.setCardNetwork(new CardNetwork());
        entity.setCardNumberLastFour("1234");
        entity.setCardType("Visa");
        entity.setCreatedAt(LocalDate.now());
        entity.setActive(true);
        return entity;
    }

    private static EligibleOfferResponseDTO anEligibleOfferResponseDTO() {
        EligibleOfferResponseDTO entity = new EligibleOfferResponseDTO();
        return entity;
    }

    @Test
    void shouldReturnEligibleOffers_whenCardholderHasActiveCards() {
        Cardholder cardholder = aCardholder();
        when(cardholderRepository.findById(1L)).thenReturn(Optional.of(cardholder));
        CardholderCard card = aCardholderCard();
        List<CardholderCard> activeCards = List.of(card);
        when(cardholderCardRepository.findByCardholderIdAndActiveTrue(1L)).thenReturn(activeCards);
        Offer offer = new Offer();
        offer.setCardNetwork(new CardNetwork());
        offer.getCardNetwork().setId(1L);
        offer.setMaxRedemptions(10);
        offer.setCurrentRedemptions(0);
        when(offerRepository.findActiveOffers(LocalDate.now())).thenReturn(List.of(offer));
        when(offerMapper.toDTO(any())).thenReturn(new OfferDTO());

        EligibleOfferResponseDTO response = offerEligibilityServiceImpl.getEligibleOffersForCardholder(1L);

        assertNotNull(response);
        assertEquals(1, response.getTotalOffers());
    }

    @Test
    void shouldThrowCardholderNotEligibleException_whenCardholderHasNoActiveCards() {
        Cardholder cardholder = aCardholder();
        when(cardholderRepository.findById(1L)).thenReturn(Optional.of(cardholder));
        when(cardholderCardRepository.findByCardholderIdAndActiveTrue(1L)).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(CardholderNotEligibleException.class, () ->
                offerEligibilityServiceImpl.getEligibleOffersForCardholder(1L));

        assertEquals("Cardholder has no active cards", exception.getMessage());
    }

    @Test
    void shouldThrowResourceNotFoundException_whenCardholderDoesNotExist() {
        when(cardholderRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                offerEligibilityServiceImpl.getEligibleOffersForCardholder(1L));

        assertEquals("Cardholder not found", exception.getMessage());
    }

    @Test
    void shouldFilterOffersByCategory_whenCategoryIsProvided() {
        Cardholder cardholder = aCardholder();
        when(cardholderRepository.findById(1L)).thenReturn(Optional.of(cardholder));
        CardholderCard card = aCardholderCard();
        List<CardholderCard> activeCards = List.of(card);
        when(cardholderCardRepository.findByCardholderIdAndActiveTrue(1L)).thenReturn(activeCards);
        when(offerEligibilityService.getEligibleOffersForCardholder(1L)).thenReturn(anEligibleOfferResponseDTO());

        List<OfferDTO> filteredOffers = offerEligibilityServiceImpl.filterOffersByCategory(1L, "SomeCategory");

        assertNotNull(filteredOffers);
    }

    @Test
    void shouldFilterOffersByOfferType_whenOfferTypeIsProvided() {
        Cardholder cardholder = aCardholder();
        when(cardholderRepository.findById(1L)).thenReturn(Optional.of(cardholder));
        CardholderCard card = aCardholderCard();
        List<CardholderCard> activeCards = List.of(card);
        when(cardholderCardRepository.findByCardholderIdAndActiveTrue(1L)).thenReturn(activeCards);
        when(offerEligibilityService.getEligibleOffersForCardholder(1L)).thenReturn(anEligibleOfferResponseDTO());

        List<OfferDTO> filteredOffers = offerEligibilityServiceImpl.filterOffersByOfferType(1L, "Discount");

        assertNotNull(filteredOffers);
    }
}