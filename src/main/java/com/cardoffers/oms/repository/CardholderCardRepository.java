package com.cardoffers.oms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cardoffers.oms.model.entity.CardholderCard;

public interface CardholderCardRepository extends JpaRepository<CardholderCard, Long> {

    List<CardholderCard> findByCardholderId(Long cardholderId);

    List<CardholderCard> findByCardholderIdAndActiveTrue(Long cardholderId);
}
