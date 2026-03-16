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
import com.cardoffers.oms.model.dto.EligibleOfferResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EligibleOfferResponseDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
void shouldSetAndGetAllFields() {
    // Given
    List<OfferDTO> eligibleOffers = new ArrayList<>();
    String cardholderId = "12345";
    String cardholderName = "John Doe";

    // When
    EligibleOfferResponseDTO responseDTO = EligibleOfferResponseDTO.builder()
            .cardholderId(cardholderId)
            .cardholderName(cardholderName)
            .eligibleOffers(eligibleOffers)
            .build();

    // Then
    assertEquals(cardholderId, responseDTO.getCardholderId());
    assertEquals(cardholderName, responseDTO.getCardholderName());
    assertEquals(eligibleOffers, responseDTO.getEligibleOffers());
}

    @Test
void shouldHaveDefaultEligibleOffers() {
    // Given
    EligibleOfferResponseDTO instance = EligibleOfferResponseDTO.builder()
            .cardholderId(null)
            .cardholderName(null)
            .eligibleOffers(new ArrayList<>())
            .build();

    // When
    List<OfferDTO> result = instance.getEligibleOffers();

    // Then
    assertNotNull(result);
    assertEquals(0, result.size());
}

}