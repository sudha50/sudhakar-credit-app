package com.cardoffers.oms.service;

import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MerchantServiceImplTest {

    @Mock 
    MerchantRepository merchantRepository;

    @Mock 
    MerchantMapper merchantMapper;

    @InjectMocks 
    MerchantServiceImpl merchantServiceImpl;

    @Test
    void shouldReturnMerchantDTO_whenGetMerchantById() {
        Merchant merchant = aMerchant();
        MerchantDTO merchantDTO = aMerchantDTO();

        when(merchantRepository.findById(anyLong())).thenReturn(Optional.of(merchant));
        when(merchantMapper.toDTO(merchant)).thenReturn(merchantDTO);

        MerchantDTO result = merchantServiceImpl.getMerchantById(1L);

        assertNotNull(result);
        assertEquals("TestName", result.getName());
    }

    @Test
    void shouldThrowResourceNotFoundException_whenGetMerchantByIdWithInvalidId() {
        when(merchantRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            merchantServiceImpl.getMerchantById(1L);
        });

        assertEquals("Merchant not found", exception.getMessage());
    }

    @Test
    void shouldReturnListOfMerchantDTO_whenGetMerchantsByCategory() {
        Merchant merchant = aMerchant();
        MerchantDTO merchantDTO = aMerchantDTO();
        String category = "test-value";

        when(merchantRepository.findByCategory(category)).thenReturn(List.of(merchant));
        when(merchantMapper.toDTO(merchant)).thenReturn(merchantDTO);

        List<MerchantDTO> result = merchantServiceImpl.getMerchantsByCategory(category);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("TestName", result.get(0).getName());
    }

    @Test
    void shouldCreateMerchant_whenCreateMerchant() {
        MerchantDTO merchantDTO = aMerchantDTO();
        Merchant merchant = aMerchant();

        when(merchantMapper.toEntity(merchantDTO)).thenReturn(merchant);
        when(merchantRepository.save(merchant)).thenReturn(merchant);
        when(merchantMapper.toDTO(merchant)).thenReturn(merchantDTO);

        MerchantDTO result = merchantServiceImpl.createMerchant(merchantDTO);

        assertNotNull(result);
        assertEquals("TestName", result.getName());
    }

    @Test
    void shouldUpdateMerchant_whenUpdateMerchant() {
        Merchant existingMerchant = aMerchant();
        MerchantDTO updatedMerchantDTO = aMerchantDTO();
        updatedMerchantDTO.setName("UpdatedName");

        when(merchantRepository.findById(anyLong())).thenReturn(Optional.of(existingMerchant));
        when(merchantMapper.toDTO(existingMerchant)).thenReturn(updatedMerchantDTO);
        when(merchantRepository.save(existingMerchant)).thenReturn(existingMerchant);

        MerchantDTO result = merchantServiceImpl.updateMerchant(1L, updatedMerchantDTO);

        assertNotNull(result);
        assertEquals("UpdatedName", result.getName());
        verify(merchantRepository).save(existingMerchant);
    }

    @Test
    void shouldThrowResourceNotFoundException_whenUpdateMerchantWithInvalidId() {
        MerchantDTO updatedMerchantDTO = aMerchantDTO();

        when(merchantRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            merchantServiceImpl.updateMerchant(1L, updatedMerchantDTO);
        });

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
        MerchantDTO entity = new MerchantDTO();
        entity.setName("TestName");
        entity.setDescription("Test description");
        entity.setCategory("test-value");
        entity.setLogoUrl("https://example.com");
        entity.setWebsite("test-value");
        entity.setActive(true);
        return entity;
    }
}