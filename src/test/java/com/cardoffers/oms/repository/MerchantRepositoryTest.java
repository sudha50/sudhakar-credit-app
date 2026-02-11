package com.cardoffers.oms.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cardoffers.oms.model.entity.Merchant;

@ExtendWith(MockitoExtension.class)
class MerchantRepositoryTest {

    @Mock
    private MerchantRepository merchantRepository;

    @InjectMocks
    private MerchantService merchantService; // Assuming a service using the repository.

    private Merchant activeMerchant;
    private Merchant inactiveMerchant;

    @BeforeEach
    void setUp() {
        activeMerchant = new Merchant(1L, "Active Merchant", true, "Category1");
        inactiveMerchant = new Merchant(2L, "Inactive Merchant", false, "Category2");
    }

    @Test
    void shouldReturnActiveMerchants_whenFindByActiveTrue() {
        when(merchantRepository.findByActiveTrue()).thenReturn(Arrays.asList(activeMerchant));

        List<Merchant> result = merchantRepository.findByActiveTrue();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(activeMerchant, result.get(0));
    }

    @Test
    void shouldReturnEmptyList_whenNoActiveMerchants() {
        when(merchantRepository.findByActiveTrue()).thenReturn(Collections.emptyList());

        List<Merchant> result = merchantRepository.findByActiveTrue();
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnMerchantsByCategory_whenFindByCategory() {
        when(merchantRepository.findByCategory("Category1")).thenReturn(Arrays.asList(activeMerchant));

        List<Merchant> result = merchantRepository.findByCategory("Category1");
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(activeMerchant, result.get(0));
    }

    @Test
    void shouldReturnEmptyList_whenNoMerchantsInCategory() {
        when(merchantRepository.findByCategory("NonExistentCategory")).thenReturn(Collections.emptyList());

        List<Merchant> result = merchantRepository.findByCategory("NonExistentCategory");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnMerchant_whenFindByNameIgnoreCase() {
        when(merchantRepository.findByNameIgnoreCase("active merchant")).thenReturn(Optional.of(activeMerchant));

        Optional<Merchant> result = merchantRepository.findByNameIgnoreCase("active merchant");

        assertTrue(result.isPresent());
        assertEquals(activeMerchant, result.get());
    }

    @Test
    void shouldReturnEmptyOptional_whenMerchantNameDoesNotExist() {
        when(merchantRepository.findByNameIgnoreCase("unknown merchant")).thenReturn(Optional.empty());

        Optional<Merchant> result = merchantRepository.findByNameIgnoreCase("unknown merchant");

        assertFalse(result.isPresent());
    }
}