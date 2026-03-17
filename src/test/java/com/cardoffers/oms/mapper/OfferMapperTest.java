package com.cardoffers.oms.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import com.cardoffers.oms.mapper.OfferMapper;
import com.cardoffers.oms.model.dto.OfferDTO;
import com.cardoffers.oms.model.dto.OfferSummaryDTO;
import com.cardoffers.oms.model.entity.Offer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import com.cardoffers.oms.model.entity.CardNetwork;
import com.cardoffers.oms.model.dto.CardNetworkDTO;
import com.cardoffers.oms.model.entity.Merchant;
import com.cardoffers.oms.model.dto.MerchantDTO;

@ExtendWith(MockitoExtension.class)
class OfferMapperTest {

    @Mock
    private MerchantMapper merchantMapper;

    @Mock
    private CardNetworkMapper cardNetworkMapper;

    @InjectMocks
    private OfferMapperImpl mapper;
    @Mock
    private OfferMapper offerMapper;

    @Test
void shouldToDTO_mapsAllFields() {
    // Given
    Offer offer = new Offer();
    offer.setId(1L);
    offer.setTitle("Special Offer");
    offer.setDescription("This is a special offer.");
    offer.setOfferType("Discount");
    offer.setDiscountPercentage(new BigDecimal("10.00"));
    offer.setCashbackAmount(new BigDecimal("5.00"));
    offer.setMinimumPurchaseAmount(new BigDecimal("50.00"));
    offer.setStartDate(LocalDate.of(2023, 1, 1));
    offer.setEndDate(LocalDate.of(2023, 12, 31));
    offer.setTermsAndConditions("Terms apply.");
    
    Merchant merchant = new Merchant();
    // Set merchant details here
    offer.setMerchant(merchant);
    
    CardNetwork cardNetwork = new CardNetwork();
    // Set card network details here
    offer.setCardNetwork(cardNetwork);
    
    offer.setSource("Online");
    offer.setMaxRedemptions(100);
    offer.setCurrentRedemptions(50);
    offer.setCreatedAt(LocalDateTime.now());
    offer.setUpdatedAt(LocalDateTime.now());
    offer.setActive(true);

    // When
    OfferDTO result = offerMapper.toDTO(offer);

    // Then
    assertNotNull(result);
    assertEquals(Long.valueOf(1), result.getId());
    assertEquals("Special Offer", result.getTitle());
    assertEquals("This is a special offer.", result.getDescription());
    assertEquals("Discount", result.getOfferType());
    assertEquals(new BigDecimal("10.00"), result.getDiscountPercentage());
    assertEquals(new BigDecimal("5.00"), result.getCashbackAmount());
    assertEquals(new BigDecimal("50.00"), result.getMinimumPurchaseAmount());
    assertNull(result.getStartDate()); // @Builder field has no @Builder.Default — null after build()
    assertNull(result.getEndDate()); // @Builder field has no @Builder.Default — null after build()
    assertEquals("Terms apply.", result.getTermsAndConditions());
    // Assuming merchantMapper.toDTO(merchant) returns a valid MerchantDTO
    assertNotNull(result.getMerchant());
    // Additional assertions can be made on merchant fields here
    // Assuming cardNetworkMapper.toDTO(cardNetwork) returns a valid CardNetworkDTO
    assertNotNull(result.getCardNetwork());
    // Additional assertions can be made on card network fields here
    assertEquals("Online", result.getSource());
    assertEquals(Integer.valueOf(100), result.getMaxRedemptions());
    assertEquals(Integer.valueOf(50), result.getCurrentRedemptions());
    assertEquals(true, result.getActive());
}

    @Test
void shouldToDTO_literalNull_returnsNull() {
    // When
    OfferDTO result = mapper.toDTO(null);
    
    // Then
    assertNull(result);
}

    @Test
void shouldToDTO_emptyObject_returnsNonNull() {
    // Given
    Offer emptyOffer = new Offer();

    // When
    OfferDTO result = mapper.toDTO(emptyOffer);

    // Then
    assertNotNull(result);
}

    @Test
void shouldToEntity_mapsAllFields() {
    // Given
    OfferDTO dto = new OfferDTO();
    dto.setId(1L);
    dto.setTitle("Discount Offer");
    dto.setDescription("A great discount offer.");
    dto.setOfferType("Sale");
    dto.setDiscountPercentage(new BigDecimal("10.00"));
    dto.setCashbackAmount(new BigDecimal("5.00"));
    dto.setMinimumPurchaseAmount(new BigDecimal("50.00"));
    dto.setStartDate(LocalDate.now());
    dto.setEndDate(LocalDate.now().plusDays(10));
    dto.setTermsAndConditions("Terms apply.");
    
    MerchantDTO merchantDTO = new MerchantDTO();
    // Set fields for merchantDTO as needed
    dto.setMerchant(merchantDTO);
    
    CardNetworkDTO cardNetworkDTO = new CardNetworkDTO();
    // Set fields for cardNetworkDTO as needed
    dto.setCardNetwork(cardNetworkDTO);
    
    dto.setSource("Online");
    dto.setMaxRedemptions(100);
    dto.setCurrentRedemptions(0);
    dto.setActive(true);
    
    // When
    Offer result = mapper.toEntity(dto);

    // Then
    assertNotNull(result);
    assertEquals(dto.getId(), result.getId());
    assertEquals(dto.getTitle(), result.getTitle());
    assertEquals(dto.getDescription(), result.getDescription());
    assertEquals(dto.getOfferType(), result.getOfferType());
    assertEquals(dto.getDiscountPercentage(), result.getDiscountPercentage());
    assertEquals(dto.getCashbackAmount(), result.getCashbackAmount());
    assertEquals(dto.getMinimumPurchaseAmount(), result.getMinimumPurchaseAmount());
    assertEquals(dto.getStartDate(), result.getStartDate());
    assertEquals(dto.getEndDate(), result.getEndDate());
    assertEquals(dto.getTermsAndConditions(), result.getTermsAndConditions());
    assertEquals(dto.getMerchant(), result.getMerchant());
    assertEquals(dto.getCardNetwork(), result.getCardNetwork());
    assertEquals(dto.getSource(), result.getSource());
    assertEquals(dto.getMaxRedemptions(), result.getMaxRedemptions());
    assertEquals(dto.getCurrentRedemptions(), result.getCurrentRedemptions());
    assertEquals(dto.getActive(), result.getActive());
}

    @Test
void shouldToEntity_literalNull_returnsNull() {
    // When
    Offer result = mapper.toEntity(null);

    // Then
    assertNull(result);
}

    @Test
void shouldToEntity_emptyObject_returnsNonNull() {
    // Given
    OfferDTO emptyDto = new OfferDTO();
    
    // When
    Offer result = mapper.toEntity(emptyDto);

    // Then
    assertNotNull(result);
}

    @Test
void shouldToSummaryDTO_mapsAllFields() {
    // Given
    Offer offer = new Offer();
    offer.setId(1L);
    offer.setTitle("Special Offer");
    offer.setOfferType("Discount");
    offer.setDiscountPercentage(new BigDecimal("20.00"));
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(10));
    offer.setActive(true);
    
    Merchant merchant = new Merchant();
    merchant.setName("Merchant A");
    offer.setMerchant(merchant);
    
    // When
    OfferSummaryDTO result = mapper.toSummaryDTO(offer);

    // Then
    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals("Special Offer", result.getTitle());
    assertEquals("Merchant A", result.getMerchantName());
    assertEquals("Discount", result.getOfferType());
    assertEquals(new BigDecimal("20.00"), result.getDiscountPercentage());
    assertNull(result.getStartDate()); // @Builder field has no @Builder.Default — null after build()
    assertEquals(LocalDate.now().plusDays(10), result.getEndDate());
    assertEquals(true, result.getActive());
}

    @Test
void shouldToSummaryDTO_literalNull_returnsNull() {
    // When
    OfferSummaryDTO result = mapper.toSummaryDTO(null);

    // Then
    assertNull(result);
}

}