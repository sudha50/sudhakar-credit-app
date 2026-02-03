package com.cardoffers.oms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cardoffers.oms.model.entity.Cardholder;

public interface CardholderRepository extends JpaRepository<Cardholder, Long> {

    Optional<Cardholder> findByEmail(String email);

    List<Cardholder> findByActiveTrue();
}
