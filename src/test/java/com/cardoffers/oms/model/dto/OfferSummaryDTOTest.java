package com.cardoffers.oms.model.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class OfferSummaryDTOTest {

    @Test
    void shouldCreateOfferSummaryDTO_whenAllAttributesAreProvided() {
        OfferSummaryDTO offerSummary = new OfferSummaryDTO();
        offerSummary.setId(1L);
        offerSummary.setTitle("Discount Offer");
        offerSummary.setMerchantName("Best Merchant");
        offerSummary.setOfferType("Percentage");
        offerSummary.setDiscountPercentage(new BigDecimal("20.00"));
        offerSummary.setStartDate(LocalDate.now());
        offerSummary.setEndDate(LocalDate.now().plusDays(10));
        offerSummary.setActive(true);

        assertNotNull(offerSummary);
        assertEquals(1L, offerSummary.getId());
        assertEquals("Discount Offer", offerSummary.getTitle());
        assertEquals("Best Merchant", offerSummary.getMerchantName());
        assertEquals("Percentage", offerSummary.getOfferType());
        assertEquals(new BigDecimal("20.00"), offerSummary.getDiscountPercentage());
        assertEquals(LocalDate.now(), offerSummary.getStartDate());
        assertEquals(LocalDate.now().plusDays(10), offerSummary.getEndDate());
        assertTrue(offerSummary.getActive());
    }

    @Test
    void shouldReturnNull_whenNoAttributesAreSet() {
        OfferSummaryDTO offerSummary = new OfferSummaryDTO();

        assertNull(offerSummary.getId());
        assertNull(offerSummary.getTitle());
        assertNull(offerSummary.getMerchantName());
        assertNull(offerSummary.getOfferType());
        assertNull(offerSummary.getDiscountPercentage());
        assertNull(offerSummary.getStartDate());
        assertNull(offerSummary.getEndDate());
        assertNull(offerSummary.getActive());
    }

    @Test
    void shouldAllowNullForActive_whenOfferSummaryIsCreated() {
        OfferSummaryDTO offerSummary = new OfferSummaryDTO();
        offerSummary.setActive(null);

        assertNull(offerSummary.getActive());
    }

    @Test
    void shouldCreateOfferSummaryDTO_usingBuilder() {
        OfferSummaryDTO offerSummary = OfferSummaryDTO.builder()
                .id(2L)
                .title("Flat Discount")
                .merchantName("Another Merchant")
                .offerType("Flat")
                .discountPercentage(new BigDecimal("50.00"))
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(5))
                .active(false)
                .build();

        assertNotNull(offerSummary);
        assertEquals(2L, offerSummary.getId());
        assertEquals("Flat Discount", offerSummary.getTitle());
        assertEquals("Another Merchant", offerSummary.getMerchantName());
        assertEquals("Flat", offerSummary.getOfferType());
        assertEquals(new BigDecimal("50.00"), offerSummary.getDiscountPercentage());
        assertEquals(LocalDate.now(), offerSummary.getStartDate());
        assertEquals(LocalDate.now().plusDays(5), offerSummary.getEndDate());
        assertFalse(offerSummary.getActive());
    }
}