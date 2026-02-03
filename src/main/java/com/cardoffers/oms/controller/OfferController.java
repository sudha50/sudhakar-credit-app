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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cardoffers.oms.model.dto.OfferDTO;
import com.cardoffers.oms.model.dto.OfferSummaryDTO;
import com.cardoffers.oms.service.OfferService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/offers")
@Validated
public class OfferController {

    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @GetMapping
    public ResponseEntity<List<OfferDTO>> getAllActiveOffers() {
        return ResponseEntity.ok(offerService.getAllActiveOffers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfferDTO> getOfferById(@PathVariable Long id) {
        return ResponseEntity.ok(offerService.getOfferById(id));
    }

    @GetMapping("/merchant/{merchantId}")
    public ResponseEntity<List<OfferSummaryDTO>> getOffersByMerchant(@PathVariable Long merchantId) {
        return ResponseEntity.ok(offerService.getOffersByMerchant(merchantId));
    }

    @GetMapping("/network/{networkId}")
    public ResponseEntity<List<OfferSummaryDTO>> getOffersByNetwork(@PathVariable Long networkId) {
        return ResponseEntity.ok(offerService.getOffersByCardNetwork(networkId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<OfferDTO>> searchOffers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String offerType,
            @RequestParam(required = false) String category) {
        return ResponseEntity.ok(offerService.searchOffers(keyword, offerType, category));
    }

    @PostMapping
    public ResponseEntity<OfferDTO> createOffer(@Valid @RequestBody OfferDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(offerService.createOffer(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OfferDTO> updateOffer(@PathVariable Long id, @Valid @RequestBody OfferDTO dto) {
        return ResponseEntity.ok(offerService.updateOffer(id, dto));
    }
}
