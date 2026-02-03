package com.cardoffers.oms.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cardoffers.oms.model.dto.CardNetworkDTO;
import com.cardoffers.oms.service.CardNetworkService;

@RestController
@RequestMapping("/api/v1/card-networks")
@Validated
public class CardNetworkController {

    private final CardNetworkService cardNetworkService;

    public CardNetworkController(CardNetworkService cardNetworkService) {
        this.cardNetworkService = cardNetworkService;
    }

    @GetMapping
    public ResponseEntity<List<CardNetworkDTO>> getAllCardNetworks() {
        return ResponseEntity.ok(cardNetworkService.getAllActiveCardNetworks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardNetworkDTO> getCardNetworkById(@PathVariable Long id) {
        return ResponseEntity.ok(cardNetworkService.getCardNetworkById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<CardNetworkDTO> getCardNetworkByCode(@PathVariable String code) {
        return ResponseEntity.ok(cardNetworkService.getCardNetworkByCode(code));
    }
}
