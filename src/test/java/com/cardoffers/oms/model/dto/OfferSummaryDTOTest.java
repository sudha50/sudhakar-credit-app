package com.cardoffers.oms.model.dto;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class OfferSummaryDTOTest {

    @Test
    void shouldCreateOfferSummaryDTO_whenAllFieldsAreSet() {
        OfferSummaryDTO offer = new OfferSummaryDTO();
        offer.setId(1L);
        offer.setTitle("Special Offer");
        offer.setMerchantName("Merchant Inc.");
        offer.setOfferType("Discount");
        offer.setDiscountPercentage(BigDecimal.valueOf(20));
        offer.setStartDate(LocalDate.now());
        offer.setEndDate(LocalDate.now().plusDays(10));
        offer.setActive(true);

        assertNotNull(offer);
        assertEquals(1L, offer.getId());
        assertEquals("Special Offer", offer.getTitle());
        assertEquals("Merchant Inc.", offer.getMerchantName());
        assertEquals("Discount", offer.getOfferType());
        assertEquals(BigDecimal.valueOf(20), offer.getDiscountPercentage());
        assertEquals(LocalDate.now(), offer.getStartDate());
        assertEquals(LocalDate.now().plusDays(10), offer.getEndDate());
        assertTrue(offer.getActive());
    }

    @Test
    void shouldNotCreateOfferSummaryDTO_whenMandatoryFieldsAreMissing() {
        OfferSummaryDTO offer = new OfferSummaryDTO();
        offer.setId(null);
        offer.setTitle(null);
        offer.setMerchantName("Merchant Inc.");
        offer.setOfferType("Discount");
        offer.setDiscountPercentage(BigDecimal.valueOf(20));
        offer.setStartDate(LocalDate.now());
        offer.setEndDate(LocalDate.now().plusDays(10));
        offer.setActive(true);

        assertThrows(NullPointerException.class, () -> {
            offer.getId(); // Simulate behavior when mandatory field is missing
        });
    }

    @Test
    void shouldCreateOfferSummaryDTOWithoutActiveField_whenSetToNull() {
        OfferSummaryDTO offer = new OfferSummaryDTO();
        offer.setId(2L);
        offer.setTitle("Another Offer");
        offer.setMerchantName("Merchant LLC");
        offer.setOfferType("Cashback");
        offer.setDiscountPercentage(BigDecimal.valueOf(15));
        offer.setStartDate(LocalDate.now());
        offer.setEndDate(LocalDate.now().plusDays(5));
        offer.setActive(null);

        assertNotNull(offer);
        assertNull(offer.getActive());
    }

    @Test
    void shouldUpdateOfferSummaryDTOFields_correctly() {
        OfferSummaryDTO offer = new OfferSummaryDTO();
        offer.setId(3L);
        offer.setTitle("Seasonal Offer");
        offer.setMerchantName("Merchant Co.");
        offer.setOfferType("Promotion");
        offer.setDiscountPercentage(BigDecimal.valueOf(25));
        offer.setStartDate(LocalDate.now());
        offer.setEndDate(LocalDate.now().plusDays(15));
        offer.setActive(true);

        offer.setTitle("Updated Seasonal Offer");

        assertEquals("Updated Seasonal Offer", offer.getTitle());
    }

    @Test
    void shouldCreateOfferSummaryDTOUsingBuilder_whenAllFieldsAreProvided() {
        OfferSummaryDTO offer = OfferSummaryDTO.builder()
                .id(4L)
                .title("Summer Offer")
                .merchantName("Merchant Group")
                .offerType("Special")
                .discountPercentage(BigDecimal.valueOf(30))
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(20))
                .active(true)
                .build();

        assertNotNull(offer);
        assertEquals(4L, offer.getId());
        assertEquals("Summer Offer", offer.getTitle());
        assertEquals("Merchant Group", offer.getMerchantName());
        assertEquals("Special", offer.getOfferType());
        assertEquals(BigDecimal.valueOf(30), offer.getDiscountPercentage());
        assertEquals(LocalDate.now(), offer.getStartDate());
        assertEquals(LocalDate.now().plusDays(20), offer.getEndDate());
        assertTrue(offer.getActive());
    }
}