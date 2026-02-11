package com.cardoffers.oms.service;

import com.cardoffers.oms.exception.InvalidOfferException;
import com.cardoffers.oms.exception.ResourceNotFoundException;
import com.cardoffers.oms.mapper.OfferMapper;
import com.cardoffers.oms.model.dto.OfferDTO;
import com.cardoffers.oms.model.entity.CardNetwork;
import com.cardoffers.oms.model.entity.Merchant;
import com.cardoffers.oms.model.entity.Offer;
import com.cardoffers.oms.repository.CardNetworkRepository;
import com.cardoffers.oms.repository.MerchantRepository;
import com.cardoffers.oms.repository.OfferRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OfferServiceImplTest {

    @Mock
    private OfferRepository offerRepository;

    @Mock
    private MerchantRepository merchantRepository;

    @Mock
    private CardNetworkRepository cardNetworkRepository;

    @Mock
    private OfferMapper offerMapper;

    @InjectMocks
    private OfferServiceImpl offerService;

    @Test
    void shouldReturnOfferDTO_whenOfferExists() {
        Offer offer = new Offer();
        OfferDTO offerDTO = new OfferDTO();
        offer.setId(1L);
        offerDTO.setId(1L);
        
        when(offerRepository.findById(1L)).thenReturn(Optional.of(offer));
        when(offerMapper.toDTO(offer)).thenReturn(offerDTO);

        OfferDTO result = offerService.getOfferById(1L);

        assertNotNull(result);
        assertEquals(offerDTO.getId(), result.getId());
        verify(offerRepository).findById(1L);
    }

    @Test
    void shouldThrowResourceNotFoundException_whenOfferDoesNotExist() {
        when(offerRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> offerService.getOfferById(1L));
    }

    @Test
    void shouldCreateOffer_whenValidInput() {
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setMerchant(new Merchant());
        offerDTO.setCardNetwork(new CardNetwork());

        Merchant merchant = new Merchant();
        CardNetwork cardNetwork = new CardNetwork();
        Offer offer = new Offer();
        offer.setId(1L);

        when(merchantRepository.findById(any())).thenReturn(Optional.of(merchant));
        when(cardNetworkRepository.findById(any())).thenReturn(Optional.of(cardNetwork));
        when(offerMapper.toEntity(offerDTO)).thenReturn(offer);
        when(offerRepository.save(offer)).thenReturn(offer);
        when(offerMapper.toDTO(offer)).thenReturn(offerDTO);

        OfferDTO result = offerService.createOffer(offerDTO);

        assertNotNull(result);
        verify(offerRepository).save(offer);
    }

    @Test
    void shouldThrowResourceNotFoundException_whenMerchantDoesNotExistOnCreate() {
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setMerchant(new Merchant());

        when(merchantRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> offerService.createOffer(offerDTO));
    }

    @Test
    void shouldThrowInvalidOfferException_whenEndDateBeforeStartDate() {
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setStartDate(LocalDate.now().plusDays(1));
        offerDTO.setEndDate(LocalDate.now());

        assertThrows(InvalidOfferException.class, () -> offerService.createOffer(offerDTO));
    }

    @Test
    void shouldUpdateOffer_whenOfferExists() {
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setMerchant(new Merchant());
        offerDTO.setCardNetwork(new CardNetwork());

        Offer existingOffer = new Offer();
        existingOffer.setId(1L);

        when(offerRepository.findById(1L)).thenReturn(Optional.of(existingOffer));
        when(merchantRepository.findById(any())).thenReturn(Optional.of(new Merchant()));
        when(cardNetworkRepository.findById(any())).thenReturn(Optional.of(new CardNetwork()));
        when(offerMapper.toDTO(existingOffer)).thenReturn(offerDTO);

        OfferDTO result = offerService.updateOffer(1L, offerDTO);

        assertNotNull(result);
        verify(offerRepository).save(existingOffer);
    }

    @Test
    void shouldThrowResourceNotFoundException_whenUpdatingNonexistentOffer() {
        when(offerRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> offerService.updateOffer(1L, new OfferDTO()));
    }
}