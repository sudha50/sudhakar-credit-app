package com.cardoffers.oms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cardoffers.oms.model.entity.CardNetwork;

public interface CardNetworkRepository extends JpaRepository<CardNetwork, Long> {

    Optional<CardNetwork> findByCode(String code);

    List<CardNetwork> findByActiveTrue();
}
