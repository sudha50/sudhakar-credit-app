package com.cardoffers.oms.service;

import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
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

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class MerchantServiceImplFunctionalTest {

    @Mock
    private MerchantRepository merchantRepository;

    @Mock
    private MerchantMapper merchantMapper;

    @InjectMocks
    private MerchantServiceImpl merchantService;

    private Merchant merchant;
    private MerchantDTO merchantDTO;

    @BeforeEach
    void setUp() {
        merchant = new Merchant();
        merchant.setName("Test Merchant");
        merchant.setDescription("Test Description");
        merchant.setCategory("Test Category");
        merchant.setLogoUrl("http://example.com/logo.png");
        merchant.setWebsite("http://example.com");
        merchant.setActive(true);

        merchantDTO = new MerchantDTO();
        merchantDTO.setName("Test Merchant");
        merchantDTO.setDescription("Test Description");
        merchantDTO.setCategory("Test Category");
        merchantDTO.setLogoUrl("http://example.com/logo.png");
        merchantDTO.setWebsite("http://example.com");
        merchantDTO.setActive(true);
    }

    @Test
    void shouldReturnMerchantById() {
        when(merchantRepository.findById(1L)).thenReturn(Optional.of(merchant));
        when(merchantMapper.toDTO(merchant)).thenReturn(merchantDTO);

        MerchantDTO foundMerchant = merchantService.getMerchantById(1L);

        assertNotNull(foundMerchant);
        assertEquals("Test Merchant", foundMerchant.getName());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenMerchantNotFound() {
        when(merchantRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            merchantService.getMerchantById(1L);
        });

        assertEquals("Merchant not found", exception.getMessage());
    }

    @Test
    void shouldCreateMerchant() {
        when(merchantMapper.toEntity(merchantDTO)).thenReturn(merchant);
        when(merchantRepository.save(merchant)).thenReturn(merchant);
        when(merchantMapper.toDTO(merchant)).thenReturn(merchantDTO);

        MerchantDTO createdMerchant = merchantService.createMerchant(merchantDTO);

        assertNotNull(createdMerchant);
        assertEquals("Test Merchant", createdMerchant.getName());
    }

    @Test
    void shouldUpdateMerchant() {
        when(merchantRepository.findById(1L)).thenReturn(Optional.of(merchant));
        when(merchantMapper.toEntity(merchantDTO)).thenReturn(merchant);
        when(merchantRepository.save(merchant)).thenReturn(merchant);
        when(merchantMapper.toDTO(merchant)).thenReturn(merchantDTO);

        MerchantDTO updatedMerchant = merchantService.updateMerchant(1L, merchantDTO);

        assertNotNull(updatedMerchant);
        assertEquals("Test Merchant", updatedMerchant.getName());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenUpdatingNonExistentMerchant() {
        when(merchantRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            merchantService.updateMerchant(1L, merchantDTO);
        });

        assertEquals("Merchant not found", exception.getMessage());
    }

    @Test
    void shouldReturnAllActiveMerchants() {
        List<Merchant> merchants = List.of(merchant);
        List<MerchantDTO> merchantDTOs = List.of(merchantDTO);

        when(merchantRepository.findByActiveTrue()).thenReturn(merchants);
        when(merchantMapper.toDTO(merchant)).thenReturn(merchantDTO);

        List<MerchantDTO> activeMerchants = merchantService.getAllActiveMerchants();

        assertNotNull(activeMerchants);
        assertEquals(1, activeMerchants.size());
        assertEquals("Test Merchant", activeMerchants.get(0).getName());
    }
}