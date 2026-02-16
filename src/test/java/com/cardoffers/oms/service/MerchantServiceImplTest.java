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

import java.util.Arrays;
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

    @Test
    void shouldReturnMerchantDTO_whenGetMerchantById() {
        Merchant merchant = aMerchant();
        when(merchantRepository.findById(1L)).thenReturn(Optional.of(merchant));
        when(merchantMapper.toDTO(merchant)).thenReturn(aMerchantDTO());

        MerchantDTO result = merchantServiceImpl.getMerchantById(1L);

        assertNotNull(result);
        assertEquals("TestName", result.getName());
    }

    @Test
    void shouldThrowResourceNotFoundException_whenGetMerchantByIdDoesNotExist() {
        when(merchantRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            merchantServiceImpl.getMerchantById(1L);
        });

        assertEquals("Merchant not found", exception.getMessage());
    }

    @Test
    void shouldReturnListOfMerchantDTOs_whenGetMerchantsByCategory() {
        Merchant merchant = aMerchant();
        when(merchantRepository.findByCategory("test-value")).thenReturn(Arrays.asList(merchant));
        when(merchantMapper.toDTO(merchant)).thenReturn(aMerchantDTO());

        List<MerchantDTO> result = merchantServiceImpl.getMerchantsByCategory("test-value");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("TestName", result.get(0).getName());
    }

    @Test
    void shouldCreateMerchant_andReturnMerchantDTO() {
        MerchantDTO dto = aMerchantDTO();
        Merchant merchant = aMerchant();
        when(merchantMapper.toEntity(dto)).thenReturn(merchant);
        when(merchantRepository.save(merchant)).thenReturn(merchant);
        when(merchantMapper.toDTO(merchant)).thenReturn(dto);

        MerchantDTO result = merchantServiceImpl.createMerchant(dto);

        assertNotNull(result);
        assertEquals("TestName", result.getName());
    }

    @Test
    void shouldUpdateMerchant_andReturnUpdatedMerchantDTO() {
        Merchant existingMerchant = aMerchant();
        MerchantDTO dto = aMerchantDTO();
        when(merchantRepository.findById(1L)).thenReturn(Optional.of(existingMerchant));
        when(merchantMapper.toDTO(existingMerchant)).thenReturn(dto);
        when(merchantRepository.save(existingMerchant)).thenReturn(existingMerchant);

        MerchantDTO result = merchantServiceImpl.updateMerchant(1L, dto);

        assertNotNull(result);
        assertEquals("TestName", result.getName());
        verify(merchantRepository).save(existingMerchant);
    }

    @Test
    void shouldThrowResourceNotFoundException_whenUpdateMerchantDoesNotExist() {
        MerchantDTO dto = aMerchantDTO();
        when(merchantRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            merchantServiceImpl.updateMerchant(1L, dto);
        });

        assertEquals("Merchant not found", exception.getMessage());
    }
}