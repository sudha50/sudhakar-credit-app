package com.cardoffers.oms.service;

import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
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
@SpringBootTest
@ActiveProfiles("test")
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
        merchant.setId(1L);
        merchant.setName("Test Merchant");
        merchant.setDescription("Description");
        merchant.setCategory("Category");
        merchant.setLogoUrl("http://logo.url");
        merchant.setWebsite("http://website.url");
        merchant.setActive(true);

        merchantDTO = new MerchantDTO();
        merchantDTO.setName("Test Merchant");
        merchantDTO.setDescription("Description");
        merchantDTO.setCategory("Category");
        merchantDTO.setLogoUrl("http://logo.url");
        merchantDTO.setWebsite("http://website.url");
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
        when(merchantRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            merchantService.getMerchantById(1L);
        });
        assertEquals("Merchant not found", exception.getMessage());
    }

    @Test
    void testCreateMerchant() {
        when(merchantMapper.toEntity(merchantDTO)).thenReturn(merchant);
        when(merchantRepository.save(any(Merchant.class))).thenReturn(merchant);
        when(merchantMapper.toDTO(merchant)).thenReturn(merchantDTO);

        MerchantDTO result = merchantService.createMerchant(merchantDTO);
        assertNotNull(result);
        assertEquals("Test Merchant", result.getName());
    }

    @Test
    void testUpdateMerchant_HappyPath() {
        when(merchantRepository.findById(1L)).thenReturn(Optional.of(merchant));
        when(merchantRepository.save(any(Merchant.class))).thenReturn(merchant);
        when(merchantMapper.toDTO(merchant)).thenReturn(merchantDTO);

        MerchantDTO updatedMerchantDTO = new MerchantDTO();
        updatedMerchantDTO.setName("Updated Merchant");
        updatedMerchantDTO.setDescription("Updated Description");
        updatedMerchantDTO.setCategory("Updated Category");
        updatedMerchantDTO.setLogoUrl("http://updated.logo.url");
        updatedMerchantDTO.setWebsite("http://updated.website.url");
        updatedMerchantDTO.setActive(false);

        MerchantDTO result = merchantService.updateMerchant(1L, updatedMerchantDTO);
        assertEquals("Updated Merchant", result.getName());
        assertFalse(result.getActive());
    }

    @Test
    void testUpdateMerchant_NotFound() {
        when(merchantRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            merchantService.updateMerchant(1L, merchantDTO);
        });
        assertEquals("Merchant not found", exception.getMessage());
    }
}