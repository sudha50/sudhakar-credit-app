package com.cardoffers.oms.service;

import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class MerchantServiceImplTest {

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
        merchantDTO.setLogoUrl("http://test.com/logo.png");
        merchantDTO.setWebsite("http://test.com");
        merchantDTO.setActive(true);

        merchant = new Merchant();
        merchant.setName("Test Merchant");
        merchant.setDescription("Test Description");
        merchant.setCategory("Test Category");
        merchant.setLogoUrl("http://test.com/logo.png");
        merchant.setWebsite("http://test.com");
        merchant.setActive(true);
    }

    @Test
    void shouldReturnMerchantDTO_whenGetMerchantByIdIsCalledWithExistingId() {
        when(merchantRepository.findById(1L)).thenReturn(Optional.of(merchant));
        when(merchantMapper.toDTO(merchant)).thenReturn(merchantDTO);

        MerchantDTO result = merchantService.getMerchantById(1L);

        assertNotNull(result);
        assertEquals("Test Merchant", result.getName());
    }

    @Test
    void shouldThrowResourceNotFoundException_whenGetMerchantByIdIsCalledWithNonExistingId() {
        when(merchantRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> merchantService.getMerchantById(1L));
    }

    @Test
    void shouldCreateMerchant_whenCreateMerchantIsCalled() {
        when(merchantMapper.toEntity(merchantDTO)).thenReturn(merchant);
        when(merchantRepository.save(merchant)).thenReturn(merchant);
        when(merchantMapper.toDTO(merchant)).thenReturn(merchantDTO);

        MerchantDTO result = merchantService.createMerchant(merchantDTO);

        assertNotNull(result);
        assertEquals("Test Merchant", result.getName());
        verify(merchantRepository, times(1)).save(any(Merchant.class));
    }

    @Test
    void shouldUpdateMerchant_whenUpdateMerchantIsCalledWithExistingId() {
        when(merchantRepository.findById(1L)).thenReturn(Optional.of(merchant));
        when(merchantRepository.save(merchant)).thenReturn(merchant);
        when(merchantMapper.toDTO(merchant)).thenReturn(merchantDTO);

        MerchantDTO result = merchantService.updateMerchant(1L, merchantDTO);

        assertNotNull(result);
        assertEquals("Test Merchant", result.getName());
        verify(merchantRepository, times(1)).save(any(Merchant.class));
    }

    @Test
    void shouldThrowResourceNotFoundException_whenUpdateMerchantIsCalledWithNonExistingId() {
        when(merchantRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> merchantService.updateMerchant(1L, merchantDTO));
    }

    @Test
    void shouldReturnListOfActiveMerchants_whenGetAllActiveMerchantsIsCalled() {
        when(merchantRepository.findByActiveTrue()).thenReturn(Collections.singletonList(merchant));
        when(merchantMapper.toDTO(merchant)).thenReturn(merchantDTO);

        var result = merchantService.getAllActiveMerchants();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Merchant", result.get(0).getName());
    }

    @Test
    void shouldReturnListOfMerchantsByCategory_whenGetMerchantsByCategoryIsCalled() {
        when(merchantRepository.findByCategory("Test Category")).thenReturn(Collections.singletonList(merchant));
        when(merchantMapper.toDTO(merchant)).thenReturn(merchantDTO);

        var result = merchantService.getMerchantsByCategory("Test Category");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Merchant", result.get(0).getName());
    }
}