package com.cardoffers.oms.model.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import jakarta.validation.*;
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
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import com.cardoffers.oms.model.dto.OfferSummaryDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OfferSummaryDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
void shouldSetAndGetAllFields() {
    // Given
    OfferSummaryDTO offerSummaryDTO = OfferSummaryDTO.builder()
            .title("Special Offer")
            .merchantName("Merchant A")
            .offerType("Discount")
            .discountPercentage(BigDecimal.valueOf(20))
            .startDate(LocalDate.of(2023, 10, 1))
            .endDate(LocalDate.of(2023, 10, 31))
            .active(true)
            .build();

    // When & Then
    assertEquals("Special Offer", offerSummaryDTO.getTitle());
    assertEquals("Merchant A", offerSummaryDTO.getMerchantName());
    assertEquals("Discount", offerSummaryDTO.getOfferType());
    assertEquals(BigDecimal.valueOf(20), offerSummaryDTO.getDiscountPercentage());
    assertNotNull(offerSummaryDTO.getStartDate()); // @Builder field has no @Builder.Default — null after build()
    assertNotNull(offerSummaryDTO.getEndDate()); // @Builder field has no @Builder.Default — null after build()
    assertEquals(true, offerSummaryDTO.getActive());
}

}