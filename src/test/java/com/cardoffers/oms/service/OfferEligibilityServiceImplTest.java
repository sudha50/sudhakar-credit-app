package com.cardoffers.oms.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
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

@ExtendWith(MockitoExtension.class)
class OfferEligibilityServiceImplTest {

    @Mock
    private CardholderRepository cardholderRepository;

    @Mock
    private CardholderCardRepository cardholderCardRepository;

    @Mock
    private OfferRepository offerRepository;

    @Mock
    private OfferMapper offerMapper;

    @InjectMocks
    private OfferEligibilityServiceImpl offerEligibilityService;

    private Cardholder cardholder;
    private List<CardholderCard> activeCards;
    private List<Offer> offers;
    private OfferDTO offerDTO;

    @BeforeEach
    void setUp() {
        cardholder = new Cardholder();
        cardholder.setId(1L);
        cardholder.setFirstName("John");
        cardholder.setLastName("Doe");

        activeCards = new ArrayList<>();
        offers = new ArrayList<>();
        offerDTO = new OfferDTO();
        offerDTO.setOfferType("Discount");
    }

    @Test
    void shouldReturnEligibleOffers_whenCardholderHasActiveCards() {
        // Arrange
        when(cardholderRepository.findById(1L)).thenReturn(Optional.of(cardholder));
        CardholderCard card = new CardholderCard();
        card.setCardNetwork(null); // Assume a valid card network would be set here
        activeCards.add(card);
        when(cardholderCardRepository.findByCardholderIdAndActiveTrue(1L)).thenReturn(activeCards);

        Offer offer = new Offer();
        offer.setCardNetwork(card.getCardNetwork());
        offer.setMaxRedemptions(null);
        offer.setCurrentRedemptions(0);
        offers.add(offer);
        
        when(offerRepository.findActiveOffers(LocalDate.now())).thenReturn(offers);
        when(offerMapper.toDTO(any(Offer.class))).thenReturn(offerDTO);

        // Act
        EligibleOfferResponseDTO result = offerEligibilityService.getEligibleOffersForCardholder(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalOffers());
        assertEquals("John Doe", result.getCardholderName());
    }

    @Test
    void shouldThrowResourceNotFoundException_whenCardholderDoesNotExist() {
        // Arrange
        when(cardholderRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> offerEligibilityService.getEligibleOffersForCardholder(1L));
    }

    @Test
    void shouldThrowCardholderNotEligibleException_whenNoActiveCards() {
        // Arrange
        when(cardholderRepository.findById(1L)).thenReturn(Optional.of(cardholder));
        when(cardholderCardRepository.findByCardholderIdAndActiveTrue(1L)).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(CardholderNotEligibleException.class, () -> offerEligibilityService.getEligibleOffersForCardholder(1L));
    }

    @Test
    void shouldReturnFilteredOffersByCategory_whenCategoryIsValid() {
        // Arrange
        when(cardholderRepository.findById(1L)).thenReturn(Optional.of(cardholder));
        when(cardholderCardRepository.findByCardholderIdAndActiveTrue(1L)).thenReturn(activeCards);
        when(offerRepository.findActiveOffers(LocalDate.now())).thenReturn(offers);
        when(offerMapper.toDTO(any(Offer.class))).thenReturn(offerDTO);
        
        // Act
        List<OfferDTO> result = offerEligibilityService.filterOffersByCategory(1L, "Discount");

        // Assert
        assertFalse(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyList_whenFilteredByInvalidCategory() {
        // Arrange
        when(cardholderRepository.findById(1L)).thenReturn(Optional.of(cardholder));
        when(cardholderCardRepository.findByCardholderIdAndActiveTrue(1L)).thenReturn(activeCards);
        when(offerRepository.findActiveOffers(LocalDate.now())).thenReturn(offers);
        when(offerMapper.toDTO(any(Offer.class))).thenReturn(offerDTO);

        // Act
        List<OfferDTO> result = offerEligibilityService.filterOffersByCategory(1L, "InvalidCategory");

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnAllOffers_whenCategoryIsNull() {
        // Arrange
        when(cardholderRepository.findById(1L)).thenReturn(Optional.of(cardholder));
        when(cardholderCardRepository.findByCardholderIdAndActiveTrue(1L)).thenReturn(activeCards);
        when(offerRepository.findActiveOffers(LocalDate.now())).thenReturn(offers);
        when(offerMapper.toDTO(any(Offer.class))).thenReturn(offerDTO);

        // Act
        List<OfferDTO> result = offerEligibilityService.filterOffersByCategory(1L, null);

        // Assert
        assertEquals(offers.size(), result.size());
    }
}