package com.cardoffers.oms.service;

import java.util.List;

import com.cardoffers.oms.model.dto.CardholderDTO;

public interface CardholderService {

    /**
     * Fetch a cardholder by database identifier.
     *
     * @param id cardholder identifier
     * @return cardholder details
     */
    CardholderDTO getCardholderById(Long id);

    /**
     * Fetch a cardholder by email.
     *
     * @param email email address
     * @return cardholder details
     */
    CardholderDTO getCardholderByEmail(String email);

    /**
     * Get all active cardholders.
     *
     * @return list of active cardholders
     */
    List<CardholderDTO> getAllActiveCardholders();

    /**
     * Create a new cardholder.
     *
     * @param dto cardholder details
     * @return persisted cardholder
     */
    CardholderDTO createCardholder(CardholderDTO dto);

    /**
     * Update an existing cardholder.
     *
     * @param id  cardholder identifier
     * @param dto updated fields
     * @return updated cardholder
     */
    CardholderDTO updateCardholder(Long id, CardholderDTO dto);
}
