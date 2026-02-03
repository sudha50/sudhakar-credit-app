package com.cardoffers.oms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cardoffers.oms.model.entity.OfferEligibility;

public interface OfferEligibilityRepository extends JpaRepository<OfferEligibility, Long> {

    List<OfferEligibility> findByCardholderIdAndIsEligibleTrue(Long cardholderId);
}
