package com.cardoffers.oms.service;

import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

import java.util.List;

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
    void shouldReturnMerchantDTO_whenGetMerchantByIdIsCalled() {
        Merchant merchant = aMerchant();
        MerchantDTO dto = aMerchantDTO();

        when(merchantRepository.findById(1L)).thenReturn(Optional.of(merchant));
        when(merchantMapper.toDTO(merchant)).thenReturn(dto);

        MerchantDTO result = merchantServiceImpl.getMerchantById(1L);

        assertNotNull(result);
        assertEquals("TestName", result.getName());
    }

    @Test
    void shouldThrowResourceNotFoundException_whenGetMerchantByIdIsCalledWithInvalidId() {
        when(merchantRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            merchantServiceImpl.getMerchantById(1L);
        });

        assertEquals("Merchant not found", exception.getMessage());
    }

    @Test
    void shouldReturnListOfMerchantDTO_whenGetMerchantsByCategoryIsCalled() {
        Merchant merchant = aMerchant();
        MerchantDTO dto = aMerchantDTO();
        List<Merchant> merchants = List.of(merchant);

        when(merchantRepository.findByCategory("test-value")).thenReturn(merchants);
        when(merchantMapper.toDTO(merchant)).thenReturn(dto);

        List<MerchantDTO> result = merchantServiceImpl.getMerchantsByCategory("test-value");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("TestName", result.get(0).getName());
    }

    @Test
    void shouldCreateMerchant_whenCreateMerchantIsCalled() {
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
    void shouldUpdateMerchant_whenUpdateMerchantIsCalled() {
        Merchant existingMerchant = aMerchant();
        MerchantDTO dto = aMerchantDTO();

        when(merchantRepository.findById(1L)).thenReturn(Optional.of(existingMerchant));
        when(merchantRepository.save(existingMerchant)).thenReturn(existingMerchant);
        when(merchantMapper.toDTO(existingMerchant)).thenReturn(dto);

        MerchantDTO result = merchantServiceImpl.updateMerchant(1L, dto);

        assertNotNull(result);
        assertEquals("TestName", result.getName());
        assertEquals("Test description", result.getDescription());
    }

    @Test
    void shouldThrowResourceNotFoundException_whenUpdateMerchantIsCalledWithInvalidId() {
        MerchantDTO dto = aMerchantDTO();
        when(merchantRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            merchantServiceImpl.updateMerchant(1L, dto);
        });

        assertEquals("Merchant not found", exception.getMessage());
    }
}