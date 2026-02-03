package com.cardoffers.oms.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cardoffers.oms.model.dto.EligibleOfferResponseDTO;
import com.cardoffers.oms.model.dto.OfferDTO;
import com.cardoffers.oms.service.OfferEligibilityService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/v1/eligible-offers")
@Validated
public class OfferEligibilityController {

    private final OfferEligibilityService offerEligibilityService;

    public OfferEligibilityController(OfferEligibilityService offerEligibilityService) {
        this.offerEligibilityService = offerEligibilityService;
    }

    @GetMapping("/cardholder/{cardholderId}")
    @Operation(summary = "Get eligible offers for a cardholder",
            description = "Returns all active offers that a cardholder is eligible for based on their card network")
    public ResponseEntity<EligibleOfferResponseDTO> getEligibleOffers(@PathVariable @NotNull Long cardholderId) {
        EligibleOfferResponseDTO response = offerEligibilityService.getEligibleOffersForCardholder(cardholderId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cardholder/{cardholderId}/category/{category}")
    public ResponseEntity<List<OfferDTO>> getEligibleOffersByCategory(@PathVariable Long cardholderId,
            @PathVariable String category) {
        return ResponseEntity.ok(offerEligibilityService.filterOffersByCategory(cardholderId, category));
    }

    @GetMapping("/cardholder/{cardholderId}/type/{offerType}")
    public ResponseEntity<List<OfferDTO>> getEligibleOffersByType(@PathVariable Long cardholderId,
            @PathVariable String offerType) {
        return ResponseEntity.ok(offerEligibilityService.filterOffersByOfferType(cardholderId, offerType));
    }
}
