package com.cardoffers.oms.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferDTO {

    private Long id;

    @NotBlank
    private String title;

    private String description;

    @NotBlank
    private String offerType;

    @Positive
    private BigDecimal discountPercentage;

    @Positive
    private BigDecimal cashbackAmount;

    @Positive
    private BigDecimal minimumPurchaseAmount;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private String termsAndConditions;

    @Valid
    @NotNull
    private MerchantDTO merchant;

    @Valid
    @NotNull
    private CardNetworkDTO cardNetwork;

    @NotBlank
    private String source;

    @Positive
    private Integer maxRedemptions;

    @PositiveOrZero
    private Integer currentRedemptions;

    private Boolean active;
}
