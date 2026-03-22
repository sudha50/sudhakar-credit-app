package com.cardoffers.oms.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

@Service
@Transactional
public class OfferEligibilityServiceImpl implements OfferEligibilityService {

    private final CardholderRepository cardholderRepository;
    private final CardholderCardRepository cardholderCardRepository;
    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;
    private final OfferEligibilityService offerEligibilityServiceProxy;

    public OfferEligibilityServiceImpl(CardholderRepository cardholderRepository,
            CardholderCardRepository cardholderCardRepository,
            OfferRepository offerRepository,
            OfferMapper offerMapper,
            @Lazy OfferEligibilityService offerEligibilityServiceProxy) {
        this.cardholderRepository = cardholderRepository;
        this.cardholderCardRepository = cardholderCardRepository;
        this.offerRepository = offerRepository;
        this.offerMapper = offerMapper;
        this.offerEligibilityServiceProxy = offerEligibilityServiceProxy;
    }

    @Override
    @Cacheable(value = "eligibleOffers", key = "#cardholderId")
    @Transactional(readOnly = true)
    public EligibleOfferResponseDTO getEligibleOffersForCardholder(Long cardholderId) {
        Cardholder cardholder = cardholderRepository.findById(cardholderId)
                .orElseThrow(() -> new ResourceNotFoundException("Cardholder not found"));

        List<CardholderCard> activeCards = cardholderCardRepository
                .findByCardholderIdAndActiveTrue(cardholderId);

        if (activeCards.isEmpty()) {
            throw new CardholderNotEligibleException("Cardholder has no active cards.");
        }

        Set<Long> cardNetworkIds = activeCards.stream()
                .map(card -> card.getCardNetwork().getId())
                .collect(Collectors.toSet());

        List<Offer> eligibleOffers = offerRepository.findActiveOffers(LocalDate.now()).stream()
                .filter(offer -> offer.getCardNetwork() != null && offer.getCardNetwork().getId() != null)
                .filter(offer -> cardNetworkIds.contains(offer.getCardNetwork().getId()))
                .filter(this::hasRedemptionsAvailable)
                .collect(Collectors.toList());

        List<OfferDTO> offerDTOs = eligibleOffers.stream()
                .map(offerMapper::toDTO)
                .collect(Collectors.toList());

        return EligibleOfferResponseDTO.builder()
                .cardholderId(cardholderId)
                .cardholderName(cardholder.getFirstName() + " " + cardholder.getLastName())
                .eligibleOffers(offerDTOs)
                .totalOffers(offerDTOs.size())
                .build();
    }

    private boolean hasRedemptionsAvailable(Offer offer) {
        return offer.getMaxRedemptions() == null ||
                offer.getCurrentRedemptions() == null ||
                offer.getCurrentRedemptions() < offer.getMaxRedemptions();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OfferDTO> filterOffersByCategory(Long cardholderId, String category) {
        String upper = category == null ? null : category.trim().toUpperCase();
        List<OfferDTO> offers = offerEligibilityServiceProxy.getEligibleOffersForCardholder(cardholderId)
                .getEligibleOffers();
        if (upper == null || upper.isBlank()) {
            return offers;
        }
        return offers.stream()
                .filter(o -> o.getMerchant() != null && o.getMerchant().getCategory() != null)
                .filter(o -> o.getMerchant().getCategory().trim().toUpperCase().equals(upper))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OfferDTO> filterOffersByOfferType(Long cardholderId, String offerType) {
        String upper = offerType == null ? null : offerType.trim().toUpperCase();
        List<OfferDTO> offers = offerEligibilityServiceProxy.getEligibleOffersForCardholder(cardholderId)
                .getEligibleOffers();
        if (upper == null || upper.isBlank()) {
            return offers;
        }
        return offers.stream()
                .filter(o -> o.getOfferType() != null)
                .filter(o -> o.getOfferType().trim().toUpperCase().equals(upper))
                .collect(Collectors.toList());
    }
}
