package com.cardoffers.oms.service;

import java.util.List;

import com.cardoffers.oms.model.dto.OfferDTO;
import com.cardoffers.oms.model.dto.OfferSummaryDTO;

public interface OfferService {

    /**
     * Get offer by id (cached).
     *
     * @param id offer id
     * @return offer
     */
    OfferDTO getOfferById(Long id);

    /**
     * Get all currently active offers (cached).
     *
     * @return offers active for current date
     */
    List<OfferDTO> getAllActiveOffers();

    /**
     * Get offer summaries by merchant.
     *
     * @param merchantId merchant id
     * @return offer summaries
     */
    List<OfferSummaryDTO> getOffersByMerchant(Long merchantId);

    /**
     * Get offer summaries by card network.
     *
     * @param networkId network id
     * @return offer summaries
     */
    List<OfferSummaryDTO> getOffersByCardNetwork(Long networkId);

    /**
     * Create a new offer and evict relevant caches.
     *
     * @param dto offer payload
     * @return created offer
     */
    OfferDTO createOffer(OfferDTO dto);

    /**
     * Update an offer and evict relevant caches.
     *
     * @param id  offer id
     * @param dto update payload
     * @return updated offer
     */
    OfferDTO updateOffer(Long id, OfferDTO dto);

    /**
     * Search offers by optional keyword, offerType and merchant category.
     *
     * @param keyword   matches title/description
     * @param offerType offer type
     * @param category  merchant category
     * @return matching offers
     */
    List<OfferDTO> searchOffers(String keyword, String offerType, String category);
}
