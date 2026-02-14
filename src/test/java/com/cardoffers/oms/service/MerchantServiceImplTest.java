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
    void shouldReturnMerchantDTO_whenGetMerchantById() {
        Merchant merchant = aMerchant();
        MerchantDTO merchantDTO = aMerchantDTO();

        when(merchantRepository.findById(1L)).thenReturn(Optional.of(merchant));
        when(merchantMapper.toDTO(merchant)).thenReturn(merchantDTO);

        MerchantDTO result = merchantServiceImpl.getMerchantById(1L);

        assertNotNull(result);
        assertEquals("TestName", result.getName());
    }

    @Test
    void shouldThrowResourceNotFoundException_whenMerchantNotFound() {
        when(merchantRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> merchantServiceImpl.getMerchantById(1L));
    }

    @Test
    void shouldReturnMerchantsByCategory_whenGetMerchantsByCategory() {
        Merchant merchant = aMerchant();
        List<Merchant> merchants = List.of(merchant);
        MerchantDTO merchantDTO = aMerchantDTO();

        when(merchantRepository.findByCategory("test-value")).thenReturn(merchants);
        when(merchantMapper.toDTO(merchant)).thenReturn(merchantDTO);

        List<MerchantDTO> result = merchantServiceImpl.getMerchantsByCategory("test-value");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("TestName", result.get(0).getName());
    }

    @Test
    void shouldCreateMerchant_whenValidMerchantDTO() {
        MerchantDTO merchantDTO = aMerchantDTO();
        Merchant savedMerchant = aMerchant();

        when(merchantMapper.toEntity(merchantDTO)).thenReturn(savedMerchant);
        when(merchantRepository.save(any(Merchant.class))).thenReturn(savedMerchant);
        when(merchantMapper.toDTO(savedMerchant)).thenReturn(merchantDTO);

        MerchantDTO result = merchantServiceImpl.createMerchant(merchantDTO);

        assertNotNull(result);
        assertEquals("TestName", result.getName());
        verify(merchantRepository, times(1)).save(any(Merchant.class));
    }

    @Test
    void shouldUpdateMerchant_whenValidIdAndMerchantDTO() {
        Merchant existingMerchant = aMerchant();
        MerchantDTO updatedDTO = aMerchantDTO();
        updatedDTO.setDescription("Updated description");

        when(merchantRepository.findById(1L)).thenReturn(Optional.of(existingMerchant));
        when(merchantRepository.save(existingMerchant)).thenReturn(existingMerchant);
        when(merchantMapper.toDTO(existingMerchant)).thenReturn(updatedDTO);

        MerchantDTO result = merchantServiceImpl.updateMerchant(1L, updatedDTO);

        assertNotNull(result);
        assertEquals("Updated description", result.getDescription());
        verify(merchantRepository, times(1)).save(existingMerchant);
    }

    @Test
    void shouldThrowResourceNotFoundException_whenUpdatingNonExistentMerchant() {
        when(merchantRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> merchantServiceImpl.updateMerchant(1L, aMerchantDTO()));
    }
}