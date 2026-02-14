package com.cardoffers.oms.service;

import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cardoffers.oms.exception.ResourceNotFoundException;
import com.cardoffers.oms.mapper.MerchantMapper;
import com.cardoffers.oms.model.dto.MerchantDTO;
import com.cardoffers.oms.model.entity.Merchant;
import com.cardoffers.oms.repository.MerchantRepository;

@ExtendWith(MockitoExtension.class)
class MerchantServiceImplFunctionalTest {

    @Mock
    private MerchantRepository merchantRepository;

    @Mock
    private MerchantMapper merchantMapper;

    @InjectMocks
    private MerchantServiceImpl merchantService;

    private Merchant merchant;
    private MerchantDTO merchantDTO;

    @BeforeEach
    void setUp() {
        merchant = new Merchant();
        merchant.setName("Test Merchant");
        merchant.setDescription("Test Description");
        merchant.setCategory("Test Category");
        merchant.setLogoUrl("http://example.com/logo.png");
        merchant.setWebsite("http://example.com");
        merchant.setActive(true);

        merchantDTO = new MerchantDTO();
        merchantDTO.setName("Test Merchant");
        merchantDTO.setDescription("Test Description");
        merchantDTO.setCategory("Test Category");
        merchantDTO.setLogoUrl("http://example.com/logo.png");
        merchantDTO.setWebsite("http://example.com");
        merchantDTO.setActive(true);
    }

    @Test
    void testGetMerchantById_HappyPath() {
        when(merchantRepository.findById(1L)).thenReturn(Optional.of(merchant));
        when(merchantMapper.toDTO(merchant)).thenReturn(merchantDTO);

        MerchantDTO result = merchantService.getMerchantById(1L);

        assertNotNull(result);
        assertEquals("Test Merchant", result.getName());
    }

    @Test
    void testGetMerchantById_NotFound() {
        when(merchantRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            merchantService.getMerchantById(2L);
        });

        assertEquals("Merchant not found", exception.getMessage());
    }

    @Test
    void testCreateMerchant() {
        when(merchantMapper.toEntity(merchantDTO)).thenReturn(merchant);
        when(merchantRepository.save(merchant)).thenReturn(merchant);
        when(merchantMapper.toDTO(merchant)).thenReturn(merchantDTO);

        MerchantDTO result = merchantService.createMerchant(merchantDTO);

        assertNotNull(result);
        assertEquals("Test Merchant", result.getName());
    }

    @Test
    void testUpdateMerchant_HappyPath() {
        when(merchantRepository.findById(1L)).thenReturn(Optional.of(merchant));
        when(merchantMapper.toDTO(merchant)).thenReturn(merchantDTO);
        when(merchantRepository.save(any(Merchant.class))).thenReturn(merchant);

        MerchantDTO result = merchantService.updateMerchant(1L, merchantDTO);

        assertNotNull(result);
        assertEquals("Test Merchant", result.getName());
    }

    @Test
    void testUpdateMerchant_NotFound() {
        when(merchantRepository.findById(3L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            merchantService.updateMerchant(3L, merchantDTO);
        });

        assertEquals("Merchant not found", exception.getMessage());
    }

    @Test
    void testGetMerchantsByCategory_HappyPath() {
        when(merchantRepository.findByCategory("Test Category")).thenReturn(Collections.singletonList(merchant));
        when(merchantMapper.toDTO(merchant)).thenReturn(merchantDTO);

        assertEquals(1, merchantService.getMerchantsByCategory("Test Category").size());
    }
}