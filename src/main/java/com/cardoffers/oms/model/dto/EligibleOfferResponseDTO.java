package com.cardoffers.oms.model.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EligibleOfferResponseDTO {

    private Long cardholderId;
    private String cardholderName;

    @Builder.Default
    private List<OfferDTO> eligibleOffers = new ArrayList<>();

    private Integer totalOffers;
}
