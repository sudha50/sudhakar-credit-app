package com.cardoffers.oms.mapper;

import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import com.cardoffers.oms.model.dto.MerchantDTO;
import com.cardoffers.oms.model.entity.Merchant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MerchantMapperTest {

    @InjectMocks
    private MerchantMapperImpl merchantMapper;

    private Merchant merchant;
    private MerchantDTO merchantDTO;

    @BeforeEach
    void setUp() {
        merchant = new Merchant();
        merchant.setId(1L);
        merchant.setName("Test Merchant");
        merchant.setAddress("123 Test St");

        merchantDTO = new MerchantDTO();
        merchantDTO.setId(1L);
        merchantDTO.setName("Test Merchant");
        merchantDTO.setAddress("123 Test St");
    }

    @Test
    void shouldConvertMerchantToDTO_whenCalled() {
        MerchantDTO dto = merchantMapper.toDTO(merchant);
        assertNotNull(dto);
        assertEquals(merchant.getId(), dto.getId());
        assertEquals(merchant.getName(), dto.getName());
        assertEquals(merchant.getAddress(), dto.getAddress());
    }

    @Test
    void shouldConvertDTOToMerchant_whenCalled() {
        Merchant entity = merchantMapper.toEntity(merchantDTO);
        assertNotNull(entity);
        assertEquals(merchantDTO.getId(), entity.getId());
        assertEquals(merchantDTO.getName(), entity.getName());
        assertEquals(merchantDTO.getAddress(), entity.getAddress());
    }

    @Test
    void shouldReturnNull_whenMerchantIsNull() {
        MerchantDTO dto = merchantMapper.toDTO(null);
        assertNull(dto);
    }

    @Test
    void shouldReturnNull_whenDTOIsNull() {
        Merchant entity = merchantMapper.toEntity(null);
        assertNull(entity);
    }
}