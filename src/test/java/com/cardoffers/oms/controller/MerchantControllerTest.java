package com.cardoffers.oms.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.cardoffers.oms.model.dto.MerchantDTO;
import com.cardoffers.oms.service.MerchantService;

import jakarta.validation.ValidationException;

@ExtendWith(MockitoExtension.class)
class MerchantControllerTest {

    @Mock
    private MerchantService merchantService;

    @InjectMocks
    private MerchantController merchantController;

    private MerchantDTO merchantDTO;

    @BeforeEach
    void setUp() {
        merchantDTO = new MerchantDTO(); // Assume necessary fields are set
    }

    @Test
    void shouldReturnMerchant_whenGetMerchantById() {
        when(merchantService.getMerchantById(1L)).thenReturn(merchantDTO);

        ResponseEntity<MerchantDTO> response = merchantController.getMerchantById(1L);

        assertEquals(merchantDTO, response.getBody());
        verify(merchantService).getMerchantById(1L);
    }

    @Test
    void shouldReturnAllMerchants_whenGetAllMerchants() {
        when(merchantService.getAllActiveMerchants()).thenReturn(List.of(merchantDTO));

        ResponseEntity<List<MerchantDTO>> response = merchantController.getAllMerchants();

        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(merchantService).getAllActiveMerchants();
    }

    @Test
    void shouldReturnMerchantsByCategory_whenGetMerchantsByCategory() {
        when(merchantService.getMerchantsByCategory("food")).thenReturn(List.of(merchantDTO));

        ResponseEntity<List<MerchantDTO>> response = merchantController.getMerchantsByCategory("food");

        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(merchantService).getMerchantsByCategory("food");
    }

    @Test
    void shouldCreateMerchant_whenCreateMerchant() {
        when(merchantService.createMerchant(any(MerchantDTO.class))).thenReturn(merchantDTO);

        ResponseEntity<MerchantDTO> response = merchantController.createMerchant(merchantDTO);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(merchantDTO, response.getBody());
        verify(merchantService).createMerchant(any(MerchantDTO.class));
    }

    @Test
    void shouldUpdateMerchant_whenUpdateMerchant() {
        when(merchantService.updateMerchant(eq(1L), any(MerchantDTO.class))).thenReturn(merchantDTO);

        ResponseEntity<MerchantDTO> response = merchantController.updateMerchant(1L, merchantDTO);

        assertEquals(merchantDTO, response.getBody());
        verify(merchantService).updateMerchant(eq(1L), any(MerchantDTO.class));
    }

    @Test
    void shouldThrowValidationException_whenCreateMerchantWithInvalidData() {
        // Simulate validation failure
        when(merchantService.createMerchant(any(MerchantDTO.class))).thenThrow(new ValidationException("Invalid data"));

        ValidationException exception = assertThrows(ValidationException.class, () -> 
            merchantController.createMerchant(merchantDTO)
        );

        assertEquals("Invalid data", exception.getMessage());
    }
}