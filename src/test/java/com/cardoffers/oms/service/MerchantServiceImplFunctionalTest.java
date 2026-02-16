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

import java.util.ArrayList;
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
    void testGetMerchantById() {
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
        when(merchantRepository.save(merchant)).thenReturn(merchant);
        when(merchantMapper.toDTO(merchant)).thenReturn(merchantDTO);

        MerchantDTO result = merchantService.createMerchant(merchantDTO);

        assertNotNull(result);
        assertEquals("Test Merchant", result.getName());
    }

    @Test
    void testUpdateMerchant() {
        when(merchantRepository.findById(1L)).thenReturn(Optional.of(merchant));
        when(merchantMapper.toDTO(merchant)).thenReturn(merchantDTO);
        when(merchantRepository.save(merchant)).thenReturn(merchant);

        MerchantDTO result = merchantService.updateMerchant(1L, merchantDTO);

        assertNotNull(result);
        assertEquals("Test Merchant", result.getName());
    }

    @Test
    void testUpdateMerchant_NotFound() {
        when(merchantRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            merchantService.updateMerchant(1L, merchantDTO);
        });

        assertEquals("Merchant not found", exception.getMessage());
    }

    @Test
    void testGetMerchantsByCategory() {
        List<Merchant> merchants = new ArrayList<>();
        merchants.add(merchant);
        
        when(merchantRepository.findByCategory("Category")).thenReturn(merchants);
        when(merchantMapper.toDTO(merchant)).thenReturn(merchantDTO);

        List<MerchantDTO> result = merchantService.getMerchantsByCategory("Category");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Merchant", result.get(0).getName());
    }
}