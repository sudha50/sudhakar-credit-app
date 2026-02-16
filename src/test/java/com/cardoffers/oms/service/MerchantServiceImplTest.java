package com.cardoffers.oms.service;

import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    void shouldReturnMerchantDTO_whenGetMerchantByIdIsCalled() {
        Merchant merchant = aMerchant();
        MerchantDTO expectedDto = aMerchantDTO();

        when(merchantRepository.findById(1L)).thenReturn(Optional.of(merchant));
        when(merchantMapper.toDTO(merchant)).thenReturn(expectedDto);

        MerchantDTO result = merchantServiceImpl.getMerchantById(1L);

        assertEquals(expectedDto, result);
    }

    @Test
    void shouldThrowResourceNotFoundException_whenGetMerchantByIdIsCalledWithInvalidId() {
        when(merchantRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> merchantServiceImpl.getMerchantById(99L));
    }

    @Test
    void shouldReturnMerchantDTOList_whenGetMerchantsByCategoryIsCalled() {
        Merchant merchant = aMerchant();
        List<Merchant> merchants = List.of(merchant);
        MerchantDTO expectedDto = aMerchantDTO();

        when(merchantRepository.findByCategory("test-value")).thenReturn(merchants);
        when(merchantMapper.toDTO(merchant)).thenReturn(expectedDto);

        List<MerchantDTO> result = merchantServiceImpl.getMerchantsByCategory("test-value");

        assertEquals(1, result.size());
        assertEquals(expectedDto, result.get(0));
    }

    @Test
    void shouldCreateMerchantAndReturnMerchantDTO_whenCreateMerchantIsCalled() {
        MerchantDTO dto = aMerchantDTO();
        Merchant merchant = aMerchant();
        Merchant savedMerchant = aMerchant();

        when(merchantMapper.toEntity(dto)).thenReturn(merchant);
        when(merchantRepository.save(merchant)).thenReturn(savedMerchant);
        when(merchantMapper.toDTO(savedMerchant)).thenReturn(dto);

        MerchantDTO result = merchantServiceImpl.createMerchant(dto);

        assertEquals(dto, result);
        verify(merchantRepository, times(1)).save(merchant);
    }

    @Test
    void shouldUpdateMerchantAndReturnMerchantDTO_whenUpdateMerchantIsCalled() {
        Merchant existingMerchant = aMerchant();
        MerchantDTO updateDto = aMerchantDTO();
        Merchant updatedMerchant = aMerchant();

        when(merchantRepository.findById(1L)).thenReturn(Optional.of(existingMerchant));
        when(merchantMapper.toDTO(updatedMerchant)).thenReturn(updateDto);
        when(merchantRepository.save(existingMerchant)).thenReturn(updatedMerchant);

        MerchantDTO result = merchantServiceImpl.updateMerchant(1L, updateDto);

        assertEquals(updateDto, result);
        verify(merchantRepository, times(1)).save(existingMerchant);
    }

    @Test
    void shouldThrowResourceNotFoundException_whenUpdateMerchantIsCalledWithInvalidId() {
        when(merchantRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> merchantServiceImpl.updateMerchant(99L, aMerchantDTO()));
    }
}