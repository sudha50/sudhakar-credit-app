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

import com.cardoffers.oms.model.dto.MerchantDTO;
import com.cardoffers.oms.service.MerchantService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/merchants")
@Validated
public class MerchantController {

    private final MerchantService merchantService;

    public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<MerchantDTO> getMerchantById(@PathVariable Long id) {
        return ResponseEntity.ok(merchantService.getMerchantById(id));
    }

    @GetMapping
    public ResponseEntity<List<MerchantDTO>> getAllMerchants() {
        return ResponseEntity.ok(merchantService.getAllActiveMerchants());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<MerchantDTO>> getMerchantsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(merchantService.getMerchantsByCategory(category));
    }

    @PostMapping
    public ResponseEntity<MerchantDTO> createMerchant(@Valid @RequestBody MerchantDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(merchantService.createMerchant(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MerchantDTO> updateMerchant(@PathVariable Long id, @Valid @RequestBody MerchantDTO dto) {
        return ResponseEntity.ok(merchantService.updateMerchant(id, dto));
    }
}
