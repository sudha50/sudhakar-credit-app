package com.cardoffers.oms.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cardoffers.oms.model.dto.CardholderDTO;
import com.cardoffers.oms.service.CardholderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/cardholders")
@Validated
public class CardholderController {

    private final CardholderService cardholderService;

    public CardholderController(CardholderService cardholderService) {
        this.cardholderService = cardholderService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardholderDTO> getCardholder(@PathVariable Long id) {
        return ResponseEntity.ok(cardholderService.getCardholderById(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<CardholderDTO> getCardholderByEmail(@PathVariable String email) {
        return ResponseEntity.ok(cardholderService.getCardholderByEmail(email));
    }

    @GetMapping
    public ResponseEntity<List<CardholderDTO>> getAllCardholders() {
        return ResponseEntity.ok(cardholderService.getAllActiveCardholders());
    }

    @PostMapping
    public ResponseEntity<CardholderDTO> createCardholder(@Valid @RequestBody CardholderDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cardholderService.createCardholder(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardholderDTO> updateCardholder(@PathVariable Long id, @Valid @RequestBody CardholderDTO dto) {
        return ResponseEntity.ok(cardholderService.updateCardholder(id, dto));
    }
}
