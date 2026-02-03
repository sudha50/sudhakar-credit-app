package com.cardoffers.oms.service;

import java.util.List;

import com.cardoffers.oms.model.dto.CardNetworkDTO;

public interface CardNetworkService {

    /**
     * Get a card network by id (cached).
     *
     * @param id network id
     * @return network
     */
    CardNetworkDTO getCardNetworkById(Long id);

    /**
     * Get all active card networks.
     *
     * @return list of networks
     */
    List<CardNetworkDTO> getAllActiveCardNetworks();

    /**
     * Get a network by code.
     *
     * @param code network code
     * @return network
     */
    CardNetworkDTO getCardNetworkByCode(String code);
}
