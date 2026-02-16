package com.cardoffers.oms.service;

import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

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

    @Test
    void shouldReturnMerchantById() {
        // Arrange
        Merchant merchant = new Merchant();
        merchant.setId(1L);
        merchant.setName("Test Merchant");
        
        MerchantDTO merchantDTO = new MerchantDTO();
        merchantDTO.setId(1L);
        merchantDTO.setName("Test Merchant");
        
        when(merchantRepository.findById(1L)).thenReturn(Optional.of(merchant));
        when(merchantMapper.toDTO(merchant)).thenReturn(merchantDTO);
        
        // Act
        MerchantDTO result = merchantService.getMerchantById(1L);
        
        // Assert
        assertEquals("Test Merchant", result.getName());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenMerchantNotFound() {
        // Arrange
        when(merchantRepository.findById(1L)).thenReturn(Optional.empty());
        
        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            merchantService.getMerchantById(1L);
        });
        assertEquals("Merchant not found", exception.getMessage());
    }

    @Test
    void shouldCreateMerchant() {
        // Arrange
        MerchantDTO merchantDTO = new MerchantDTO();
        merchantDTO.setName("New Merchant");
        merchantDTO.setCategory("Retail");
        
        Merchant savedMerchant = new Merchant();
        savedMerchant.setId(2L);
        savedMerchant.setName("New Merchant");

        when(merchantMapper.toEntity(any(MerchantDTO.class))).thenReturn(savedMerchant);
        when(merchantRepository.save(any(Merchant.class))).thenReturn(savedMerchant);
        when(merchantMapper.toDTO(savedMerchant)).thenReturn(merchantDTO);
        
        // Act
        MerchantDTO result = merchantService.createMerchant(merchantDTO);
        
        // Assert
        assertEquals("New Merchant", result.getName());
    }

    @Test
    void shouldUpdateMerchant() {
        // Arrange
        Merchant existingMerchant = new Merchant();
        existingMerchant.setId(1L);
        existingMerchant.setName("Old Merchant");
        
        MerchantDTO updateDTO = new MerchantDTO();
        updateDTO.setName("Updated Merchant");

        when(merchantRepository.findById(1L)).thenReturn(Optional.of(existingMerchant));
        when(merchantMapper.toEntity(updateDTO)).thenReturn(existingMerchant);
        when(merchantRepository.save(existingMerchant)).thenReturn(existingMerchant);
        when(merchantMapper.toDTO(existingMerchant)).thenReturn(updateDTO);
        
        // Act
        MerchantDTO result = merchantService.updateMerchant(1L, updateDTO);
        
        // Assert
        assertEquals("Updated Merchant", result.getName());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenUpdatingNonExistentMerchant() {
        // Arrange
        MerchantDTO updateDTO = new MerchantDTO();
        updateDTO.setName("Updated Merchant");

        when(merchantRepository.findById(1L)).thenReturn(Optional.empty());
        
        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            merchantService.updateMerchant(1L, updateDTO);
        });
        assertEquals("Merchant not found", exception.getMessage());
    }

    @Test
    void shouldReturnMerchantsByCategory() {
        // Arrange
        Merchant merchant = new Merchant();
        merchant.setId(1L);
        merchant.setName("Test Merchant");
        merchant.setCategory("Food");
        merchant.setActive(true);

        MerchantDTO merchantDTO = new MerchantDTO();
        merchantDTO.setId(1L);
        merchantDTO.setName("Test Merchant");

        when(merchantRepository.findByCategory("Food")).thenReturn(List.of(merchant));
        when(merchantMapper.toDTO(merchant)).thenReturn(merchantDTO);
        
        // Act
        List<MerchantDTO> result = merchantService.getMerchantsByCategory("Food");
        
        // Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Test Merchant", result.get(0).getName());
    }
}