package com.cardoffers.oms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cardoffers.oms.model.entity.Merchant;

public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    List<Merchant> findByActiveTrue();

    List<Merchant> findByCategory(String category);

    Optional<Merchant> findByNameIgnoreCase(String name);
}
