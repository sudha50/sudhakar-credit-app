package com.cardoffers.oms.service;

import com.cardoffers.oms.model.dto.MerchantDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MerchantServiceTest {

    @Mock
    private MerchantService merchantService;

    @InjectMocks
    private MerchantServiceImpl merchantServiceImpl; // Assuming there is an implementation

    @Test
    void shouldReturnMerchant_whenValidIdProvided() {
        MerchantDTO expectedMerchant = new MerchantDTO(1L, "Merchant A");
        when(merchantService.getMerchantById(1L)).thenReturn(expectedMerchant);

        MerchantDTO actualMerchant = merchantServiceImpl.getMerchantById(1L);
        
        assertNotNull(actualMerchant);
        assertEquals(expectedMerchant, actualMerchant);
    }

    @Test
    void shouldReturnAllActiveMerchants_whenMerchantsExist() {
        List<MerchantDTO> expectedMerchants = List.of(new MerchantDTO(1L, "Merchant A"), new MerchantDTO(2L, "Merchant B"));
        when(merchantService.getAllActiveMerchants()).thenReturn(expectedMerchants);
        
        List<MerchantDTO> actualMerchants = merchantServiceImpl.getAllActiveMerchants();
        
        assertNotNull(actualMerchants);
        assertEquals(2, actualMerchants.size());
    }

    @Test
    void shouldReturnEmptyList_whenNoActiveMerchants() {
        when(merchantService.getAllActiveMerchants()).thenReturn(Collections.emptyList());
        
        List<MerchantDTO> actualMerchants = merchantServiceImpl.getAllActiveMerchants();
        
        assertNotNull(actualMerchants);
        assertTrue(actualMerchants.isEmpty());
    }

    @Test
    void shouldReturnMerchants_whenValidCategoryProvided() {
        List<MerchantDTO> expectedMerchants = List.of(new MerchantDTO(1L, "Merchant A"));
        when(merchantService.getMerchantsByCategory("Retail")).thenReturn(expectedMerchants);
        
        List<MerchantDTO> actualMerchants = merchantServiceImpl.getMerchantsByCategory("Retail");
        
        assertNotNull(actualMerchants);
        assertEquals(1, actualMerchants.size());
    }

    @Test
    void shouldThrowException_whenCreatingDuplicateMerchant() {
        MerchantDTO duplicateMerchant = new MerchantDTO(null, "Merchant A");
        when(merchantService.createMerchant(duplicateMerchant)).thenThrow(new IllegalArgumentException("Duplicate merchant"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            merchantServiceImpl.createMerchant(duplicateMerchant);
        });

        assertEquals("Duplicate merchant", exception.getMessage());
    }

    @Test
    void shouldReturnUpdatedMerchant_whenValidUpdate() {
        MerchantDTO existingMerchant = new MerchantDTO(1L, "Merchant A");
        MerchantDTO updatedMerchant = new MerchantDTO(1L, "Merchant A Updated");
        when(merchantService.updateMerchant(1L, updatedMerchant)).thenReturn(updatedMerchant);

        MerchantDTO actualMerchant = merchantServiceImpl.updateMerchant(1L, updatedMerchant);
        
        assertNotNull(actualMerchant);
        assertEquals("Merchant A Updated", actualMerchant.getName());
    }

    @Test
    void shouldThrowException_whenUpdatingNonExistentMerchant() {
        MerchantDTO nonExistentMerchant = new MerchantDTO(99L, "Merchant Z");
        when(merchantService.updateMerchant(99L, nonExistentMerchant)).thenThrow(new IllegalArgumentException("Merchant not found"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            merchantServiceImpl.updateMerchant(99L, nonExistentMerchant);
        });

        assertEquals("Merchant not found", exception.getMessage());
    }
}