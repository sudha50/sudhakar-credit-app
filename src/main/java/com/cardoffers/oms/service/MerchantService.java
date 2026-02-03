package com.cardoffers.oms.service;

import java.util.List;

import com.cardoffers.oms.model.dto.MerchantDTO;

public interface MerchantService {

    /**
     * Get a merchant by id (cached).
     *
     * @param id merchant identifier
     * @return merchant
     */
    MerchantDTO getMerchantById(Long id);

    /**
     * Get all active merchants.
     *
     * @return list of merchants
     */
    List<MerchantDTO> getAllActiveMerchants();

    /**
     * Get active merchants by category.
     *
     * @param category merchant category
     * @return list of merchants
     */
    List<MerchantDTO> getMerchantsByCategory(String category);

    /**
     * Create a new merchant.
     *
     * @param dto merchant payload
     * @return created merchant
     */
    MerchantDTO createMerchant(MerchantDTO dto);

    /**
     * Update an existing merchant.
     *
     * @param id  merchant id
     * @param dto update payload
     * @return updated merchant
     */
    MerchantDTO updateMerchant(Long id, MerchantDTO dto);
}
