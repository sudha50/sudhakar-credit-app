package com.cardoffers.oms.service;

import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
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

    private MerchantDTO merchantDTO;
    private Merchant merchant;

    @BeforeEach
    void setUp() {
        merchantDTO = new MerchantDTO();
        merchantDTO.setName("Test Merchant");
        merchantDTO.setDescription("Test Description");
        merchantDTO.setCategory("Test Category");
        merchantDTO.setLogoUrl("http://testlogo.com/logo.png");
        merchantDTO.setWebsite("http://testmerchant.com");
        merchantDTO.setActive(true);

        merchant = new Merchant();
        merchant.setName("Test Merchant");
        merchant.setDescription("Test Description");
        merchant.setCategory("Test Category");
        merchant.setLogoUrl("http://testlogo.com/logo.png");
        merchant.setWebsite("http://testmerchant.com");
        merchant.setActive(true);
    }

    @Test
    void testGetMerchantById_Success() {
        when(merchantRepository.findById(1L)).thenReturn(Optional.of(merchant));
        when(merchantMapper.toDTO(merchant)).thenReturn(merchantDTO);

        MerchantDTO result = merchantService.getMerchantById(1L);

        assertNotNull(result);
        assertEquals("Test Merchant", result.getName());
    }

    @Test
    void testGetMerchantById_NotFound() {
        when(merchantRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            merchantService.getMerchantById(1L);
        });

        assertEquals("Merchant not found", exception.getMessage());
    }

    @Test
    void testCreateMerchant_Success() {
        when(merchantMapper.toEntity(any(MerchantDTO.class))).thenReturn(merchant);
        when(merchantRepository.save(any(Merchant.class))).thenReturn(merchant);
        when(merchantMapper.toDTO(merchant)).thenReturn(merchantDTO);

        MerchantDTO result = merchantService.createMerchant(merchantDTO);

        assertNotNull(result);
        assertEquals("Test Merchant", result.getName());
    }

    @Test
    void testUpdateMerchant_Success() {
        when(merchantRepository.findById(1L)).thenReturn(Optional.of(merchant));
        when(merchantRepository.save(any(Merchant.class))).thenReturn(merchant);
        when(merchantMapper.toDTO(merchant)).thenReturn(merchantDTO);

        MerchantDTO result = merchantService.updateMerchant(1L, merchantDTO);

        assertNotNull(result);
        assertEquals("Test Merchant", result.getName());
    }

    @Test
    void testUpdateMerchant_NotFound() {
        when(merchantRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            merchantService.updateMerchant(1L, merchantDTO);
        });

        assertEquals("Merchant not found", exception.getMessage());
    }
}