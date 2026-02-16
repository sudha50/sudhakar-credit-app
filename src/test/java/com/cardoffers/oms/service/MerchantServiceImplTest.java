package com.cardoffers.oms.service;

import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
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
class MerchantServiceImplTest {

    @Mock
    MerchantRepository merchantRepository;

    @Mock
    MerchantMapper merchantMapper;

    @InjectMocks
    MerchantServiceImpl merchantServiceImpl;

    private static Merchant aMerchant() {
        Merchant entity = new Merchant();
        entity.setId(1L);
        entity.setName("TestName");
        entity.setDescription("Test description");
        entity.setCategory("test-value");
        entity.setLogoUrl("https://example.com");
        entity.setWebsite("test-value");
        entity.setCreatedAt(java.time.LocalDateTime.of(2025, 1, 15, 10, 30));
        entity.setActive(true);
        return entity;
    }

    private static MerchantDTO aMerchantDTO() {
        MerchantDTO entity = new MerchantDTO();
        entity.setName("TestName");
        entity.setDescription("Test description");
        entity.setCategory("test-value");
        entity.setLogoUrl("https://example.com");
        entity.setWebsite("test-value");
        entity.setActive(true);
        return entity;
    }

    @Test
    void shouldReturnMerchant_whenMerchantExists() {
        Merchant merchant = aMerchant();
        MerchantDTO dto = aMerchantDTO();
        
        when(merchantRepository.findById(1L)).thenReturn(Optional.of(merchant));
        when(merchantMapper.toDTO(merchant)).thenReturn(dto);

        MerchantDTO result = merchantServiceImpl.getMerchantById(1L);

        assertNotNull(result);
        assertEquals(dto.getName(), result.getName());
    }

    @Test
    void shouldThrowResourceNotFoundException_whenMerchantDoesNotExist() {
        when(merchantRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> merchantServiceImpl.getMerchantById(1L));
    }

    @Test
    void shouldReturnMerchantsByCategory_whenCategoryExists() {
        Merchant merchant = aMerchant();
        MerchantDTO dto = aMerchantDTO();
        
        when(merchantRepository.findByCategory("test-value")).thenReturn(List.of(merchant));
        when(merchantMapper.toDTO(merchant)).thenReturn(dto);

        List<MerchantDTO> result = merchantServiceImpl.getMerchantsByCategory("test-value");

        assertEquals(1, result.size());
        assertEquals(dto.getName(), result.get(0).getName());
    }

    @Test
    void shouldCreateMerchant_whenMerchantIsValid() {
        MerchantDTO merchantDTO = aMerchantDTO();
        Merchant merchant = aMerchant();

        when(merchantMapper.toEntity(merchantDTO)).thenReturn(merchant);
        when(merchantRepository.save(merchant)).thenReturn(merchant);
        when(merchantMapper.toDTO(merchant)).thenReturn(merchantDTO);

        MerchantDTO result = merchantServiceImpl.createMerchant(merchantDTO);

        assertNotNull(result);
        assertEquals(merchantDTO.getName(), result.getName());
    }

    @Test
    void shouldUpdateMerchant_whenMerchantExists() {
        Merchant existingMerchant = aMerchant();
        MerchantDTO merchantDTO = aMerchantDTO();
        
        when(merchantRepository.findById(1L)).thenReturn(Optional.of(existingMerchant));
        when(merchantMapper.toEntity(merchantDTO)).thenReturn(existingMerchant);
        when(merchantRepository.save(existingMerchant)).thenReturn(existingMerchant);
        when(merchantMapper.toDTO(existingMerchant)).thenReturn(merchantDTO);

        MerchantDTO result = merchantServiceImpl.updateMerchant(1L, merchantDTO);

        assertNotNull(result);
        assertEquals(merchantDTO.getName(), result.getName());
    }

    @Test
    void shouldThrowResourceNotFoundException_whenUpdatingNonExistentMerchant() {
        when(merchantRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> merchantServiceImpl.updateMerchant(1L, aMerchantDTO()));
    }
}