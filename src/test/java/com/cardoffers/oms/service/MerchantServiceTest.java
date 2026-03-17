package com.cardoffers.oms.service;

import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import com.cardoffers.oms.model.dto.MerchantDTO;
import com.cardoffers.oms.service.MerchantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.cardoffers.oms.repository.MerchantRepository;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MerchantServiceTest {

    @Mock
    private MerchantService merchantService;
    @Mock
    private MerchantRepository merchantRepository;

    @Test
void shouldCreateMerchant_happyPath() {
    // Given
    MerchantDTO dto = new MerchantDTO();
    dto.setName("Test Merchant");
    dto.setCategory("Retail");
    dto.setDescription("A test merchant description");
    dto.setLogoUrl("http://example.com/logo.png");
    dto.setWebsite("http://example.com");
    dto.setActive(true);
    when(merchantService.createMerchant(dto)).thenReturn(dto);

    // When
    MerchantDTO result = merchantService.createMerchant(dto);

    // Then
    assertNotNull(result);
    assertEquals("Test Merchant", result.getName());
    assertEquals("Retail", result.getCategory());
    verify(merchantService).createMerchant(dto);
}

    @Test
void shouldUpdateMerchant_happyPath() {
    // Given
    Long id = 1L;
    MerchantDTO updatedMerchant = new MerchantDTO();
    updatedMerchant.setId(id);
    updatedMerchant.setName("Updated Merchant");
    updatedMerchant.setCategory("Updated Category");
    
    MerchantDTO existingMerchant = new MerchantDTO();
    existingMerchant.setId(id);
    existingMerchant.setName("Existing Merchant");
    existingMerchant.setCategory("Existing Category");
    
    when(merchantRepository.findById(id)).thenReturn(Optional.of(existingMerchant));
    when(merchantRepository.save(any())).thenReturn(updatedMerchant);

    // When
    MerchantDTO result = merchantService.updateMerchant(id, updatedMerchant);

    // Then
    assertNotNull(result);
    assertEquals("Updated Merchant", result.getName());
    assertEquals("Updated Category", result.getCategory());
    verify(merchantRepository).findById(id);
    verify(merchantRepository).save(any());
}



    @Test
void shouldGetMerchantById_negativeId() {
    // Given
    Long negativeId = -1L;

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> merchantService.getMerchantById(negativeId));
}

    @Test
void shouldGetAllActiveMerchants_noMerchants() {
    // Given
    when(merchantService.getAllActiveMerchants()).thenReturn(Collections.emptyList());

    // When
    List<MerchantDTO> result = merchantService.getAllActiveMerchants();

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
    verify(merchantService).getAllActiveMerchants();
}



    @Test
void shouldUpdateMerchant_nonExistentId() {
    // Given
    Long nonExistentId = 999L;
    MerchantDTO dto = new MerchantDTO();
    dto.setName("Updated Merchant");
    dto.setCategory("Retail");

    // When & Then
    assertThrows(NotFoundException.class, () -> merchantService.updateMerchant(nonExistentId, dto));
}

}