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

import java.util.Collections;
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
class MerchantServiceImplTest {

    @Mock
    MerchantRepository merchantRepository;

    @Mock
    MerchantMapper merchantMapper;

    @InjectMocks
    MerchantServiceImpl merchantServiceImpl;

    @Test
    void shouldReturnMerchantDTO_whenMerchantExists() {
        Merchant merchant = aMerchant();
        MerchantDTO merchantDTO = aMerchantDTO();
        
        when(merchantRepository.findById(any())).thenReturn(Optional.of(merchant));
        when(merchantMapper.toDTO(merchant)).thenReturn(merchantDTO);

        MerchantDTO result = merchantServiceImpl.getMerchantById(1L);

        assertNotNull(result);
        assertEquals("TestName", result.getName());
    }

    @Test
    void shouldThrowResourceNotFoundException_whenMerchantDoesNotExist() {
        when(merchantRepository.findById(any())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, 
            () -> merchantServiceImpl.getMerchantById(1L));
        
        assertEquals("Merchant not found", exception.getMessage());
    }

    @Test
    void shouldReturnListOfMerchantDTO_whenMerchantsBelongToCategory() {
        Merchant merchant = aMerchant();
        MerchantDTO merchantDTO = aMerchantDTO();

        when(merchantRepository.findByCategory(any())).thenReturn(Collections.singletonList(merchant));
        when(merchantMapper.toDTO(merchant)).thenReturn(merchantDTO);

        var result = merchantServiceImpl.getMerchantsByCategory("test-value");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("TestName", result.get(0).getName());
    }

    @Test
    void shouldCreateMerchantAndReturnMerchantDTO_whenMerchantDTOIsValid() {
        Merchant merchant = aMerchant();
        MerchantDTO merchantDTO = aMerchantDTO();

        when(merchantMapper.toEntity(merchantDTO)).thenReturn(merchant);
        when(merchantRepository.save(any())).thenReturn(merchant);
        when(merchantMapper.toDTO(merchant)).thenReturn(merchantDTO);

        MerchantDTO result = merchantServiceImpl.createMerchant(merchantDTO);

        assertNotNull(result);
        assertEquals("TestName", result.getName());
        verify(merchantRepository).save(any());
    }

    @Test
    void shouldUpdateMerchantAndReturnMerchantDTO_whenMerchantExists() {
        Merchant existing = aMerchant();
        MerchantDTO updatedDTO = aMerchantDTO();
        
        when(merchantRepository.findById(any())).thenReturn(Optional.of(existing));
        when(merchantMapper.toDTO(existing)).thenReturn(updatedDTO);
        when(merchantRepository.save(existing)).thenReturn(existing);

        MerchantDTO result = merchantServiceImpl.updateMerchant(1L, updatedDTO);

        assertNotNull(result);
        assertEquals("TestName", result.getName());
        verify(merchantRepository).save(existing);
    }

    @Test
    void shouldThrowResourceNotFoundException_whenUpdatingNonExistentMerchant() {
        MerchantDTO updatedDTO = aMerchantDTO();
        
        when(merchantRepository.findById(any())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, 
            () -> merchantServiceImpl.updateMerchant(1L, updatedDTO));
        
        assertEquals("Merchant not found", exception.getMessage());
    }

    private static Merchant aMerchant() {
        Merchant entity = new Merchant();
        entity.setId(1L);
        entity.setName("TestName");
        entity.setDescription("Test description");
        entity.setCategory("test-value");
        entity.setLogoUrl("https://example.com");
        entity.setWebsite("test-value");
        entity.setActive(true);
        return entity;
    }

    private static MerchantDTO aMerchantDTO() {
        MerchantDTO dto = new MerchantDTO();
        dto.setName("TestName");
        dto.setDescription("Test description");
        dto.setCategory("test-value");
        dto.setLogoUrl("https://example.com");
        dto.setWebsite("test-value");
        dto.setActive(true);
        return dto;
    }
}