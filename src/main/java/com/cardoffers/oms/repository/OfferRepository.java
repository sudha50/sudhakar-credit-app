package com.cardoffers.oms.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cardoffers.oms.model.entity.Offer;

public interface OfferRepository extends JpaRepository<Offer, Long> {

    @Query("SELECT o FROM Offer o WHERE o.active = true AND o.startDate <= :currentDate AND o.endDate >= :currentDate")
    List<Offer> findActiveOffers(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT o FROM Offer o WHERE o.merchant.id = :merchantId AND o.active = true")
    List<Offer> findActiveOffersByMerchant(@Param("merchantId") Long merchantId);

    @Query("SELECT o FROM Offer o WHERE o.cardNetwork.id = :networkId AND o.active = true")
    List<Offer> findActiveOffersByCardNetwork(@Param("networkId") Long networkId);
}
