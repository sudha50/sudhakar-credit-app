package com.cardoffers.oms.service;

import java.util.List;

import com.cardoffers.oms.model.dto.EligibleOfferResponseDTO;
import com.cardoffers.oms.model.dto.OfferDTO;

public interface OfferEligibilityService {

    /**
     * Main use case: get all eligible offers for a cardholder.
     *
     * Eligibility rules:
     * - cardholder must exist
     * - cardholder's active cards determine eligible card networks
     * - offers must be active and within date range
     * - offers must have remaining redemptions (if a max is set)
     *
     * Result is cached per cardholder id.
     *
     * @param cardholderId cardholder id
     * @return eligible offer response
     */
    EligibleOfferResponseDTO getEligibleOffersForCardholder(Long cardholderId);

    /**
     * Filter eligible offers by merchant category.
     *
     * @param cardholderId cardholder id
     * @param category     merchant category
     * @return offers
     */
    List<OfferDTO> filterOffersByCategory(Long cardholderId, String category);

    /**
     * Filter eligible offers by offer type.
     *
     * @param cardholderId cardholder id
     * @param offerType    offer type
     * @return offers
     */
    List<OfferDTO> filterOffersByOfferType(Long cardholderId, String offerType);
}
