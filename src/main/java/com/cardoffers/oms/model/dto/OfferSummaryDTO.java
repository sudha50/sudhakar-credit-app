package com.cardoffers.oms.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferSummaryDTO {

    private Long id;
    private String title;
    private String merchantName;
    private String offerType;
    private BigDecimal discountPercentage;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean active;
}
