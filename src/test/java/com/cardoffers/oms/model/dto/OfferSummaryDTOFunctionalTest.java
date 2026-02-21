package com.cardoffers.oms.model.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class OfferSummaryDTOFunctionalTest {

    @Test
    void shouldCreateOfferSummaryDTOWithAllFields() {
        OfferSummaryDTO offer = new OfferSummaryDTO();
        offer.setId(1L);
        offer.setTitle("Summer Sale");
        offer.setMerchantName("Best Merchant");
        offer.setOfferType("Discount");
        offer.setDiscountPercentage(BigDecimal.valueOf(20.0));
        offer.setStartDate(LocalDate.now());
        offer.setEndDate(LocalDate.now().plusDays(30));
        offer.setActive(true);

        assertNotNull(offer);
        assertEquals(1L, offer.getId());
        assertEquals("Summer Sale", offer.getTitle());
        assertEquals("Best Merchant", offer.getMerchantName());
        assertEquals("Discount", offer.getOfferType());
        assertEquals(BigDecimal.valueOf(20.0), offer.getDiscountPercentage());
        assertEquals(LocalDate.now(), offer.getStartDate());
        assertEquals(LocalDate.now().plusDays(30), offer.getEndDate());
        assertTrue(offer.getActive());
    }

    @Test
    void shouldSetAndGetFieldsCorrectly() {
        OfferSummaryDTO offer = new OfferSummaryDTO();
        offer.setId(2L);
        offer.setTitle("Winter Sale");
        offer.setMerchantName("Another Merchant");
        offer.setOfferType("Promo");
        offer.setDiscountPercentage(BigDecimal.valueOf(15.0));
        offer.setStartDate(LocalDate.of(2023, 12, 1));
        offer.setEndDate(LocalDate.of(2023, 12, 31));
        offer.setActive(false);

        assertEquals(2L, offer.getId());
        assertEquals("Winter Sale", offer.getTitle());
        assertEquals("Another Merchant", offer.getMerchantName());
        assertEquals("Promo", offer.getOfferType());
        assertEquals(BigDecimal.valueOf(15.0), offer.getDiscountPercentage());
        assertEquals(LocalDate.of(2023, 12, 1), offer.getStartDate());
        assertEquals(LocalDate.of(2023, 12, 31), offer.getEndDate());
        assertFalse(offer.getActive());
    }

    @Test
    void shouldCreateOfferSummaryDTOUsingBuilder() {
        OfferSummaryDTO offer = OfferSummaryDTO.builder()
                .id(3L)
                .title("Spring Promotion")
                .merchantName("Promo Merchant")
                .offerType("Seasonal")
                .discountPercentage(BigDecimal.valueOf(25.0))
                .startDate(LocalDate.of(2024, 3, 1))
                .endDate(LocalDate.of(2024, 3, 31))
                .active(true)
                .build();

        assertNotNull(offer);
        assertEquals(3L, offer.getId());
        assertEquals("Spring Promotion", offer.getTitle());
        assertEquals("Promo Merchant", offer.getMerchantName());
        assertEquals("Seasonal", offer.getOfferType());
        assertEquals(BigDecimal.valueOf(25.0), offer.getDiscountPercentage());
        assertEquals(LocalDate.of(2024, 3, 1), offer.getStartDate());
        assertEquals(LocalDate.of(2024, 3, 31), offer.getEndDate());
        assertTrue(offer.getActive());
    }
}