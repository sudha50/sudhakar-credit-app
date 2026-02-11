package com.cardoffers.oms.service;

import com.cardoffers.oms.exception.ResourceNotFoundException;
import com.cardoffers.oms.mapper.MerchantMapper;
import com.cardoffers.oms.model.dto.MerchantDTO;
import com.cardoffers.oms.model.entity.Merchant;
import com.cardoffers.oms.repository.MerchantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MerchantServiceImplTest {

    @Mock
    private MerchantRepository merchantRepository;

    @Mock
    private MerchantMapper merchantMapper;

    @InjectMocks
    private MerchantServiceImpl merchantService;

    @Test
    void shouldReturnMerchantDTO_whenMerchantExists() {
        Long merchantId = 1L;
        Merchant merchant = new Merchant();
        MerchantDTO merchantDTO = new MerchantDTO();

        when(merchantRepository.findById(merchantId)).thenReturn(Optional.of(merchant));
        when(merchantMapper.toDTO(merchant)).thenReturn(merchantDTO);

        MerchantDTO result = merchantService.getMerchantById(merchantId);

        assertNotNull(result);
        verify(merchantRepository).findById(merchantId);
        verify(merchantMapper).toDTO(merchant);
    }

    @Test
    void shouldThrowResourceNotFoundException_whenMerchantDoesNotExist() {
        Long merchantId = 1L;

        when(merchantRepository.findById(merchantId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            merchantService.getMerchantById(merchantId);
        });

        assertEquals("Merchant not found", exception.getMessage());
    }

    @Test
    void shouldCreateMerchant_whenMerchantIsValid() {
        MerchantDTO merchantDTO = new MerchantDTO();
        Merchant savedMerchant = new Merchant();

        when(merchantMapper.toEntity(merchantDTO)).thenReturn(savedMerchant);
        when(merchantRepository.save(any(Merchant.class))).thenReturn(savedMerchant);
        when(merchantMapper.toDTO(savedMerchant)).thenReturn(merchantDTO);

        MerchantDTO result = merchantService.createMerchant(merchantDTO);

        assertNotNull(result);
        verify(merchantRepository).save(any(Merchant.class));
    }

    @Test
    void shouldUpdateMerchant_whenMerchantExists() {
        Long merchantId = 1L;
        Merchant existingMerchant = new Merchant();
        MerchantDTO merchantDTO = new MerchantDTO();

        when(merchantRepository.findById(merchantId)).thenReturn(Optional.of(existingMerchant));
        when(merchantMapper.toDTO(existingMerchant)).thenReturn(merchantDTO);
        when(merchantRepository.save(existingMerchant)).thenReturn(existingMerchant);

        MerchantDTO result = merchantService.updateMerchant(merchantId, merchantDTO);

        assertNotNull(result);
        verify(merchantRepository).findById(merchantId);
        verify(merchantRepository).save(existingMerchant);
    }

    @Test
    void shouldThrowResourceNotFoundException_whenUpdatingNonExistentMerchant() {
        Long merchantId = 1L;
        MerchantDTO merchantDTO = new MerchantDTO();

        when(merchantRepository.findById(merchantId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            merchantService.updateMerchant(merchantId, merchantDTO);
        });

        assertEquals("Merchant not found", exception.getMessage());
    }
}