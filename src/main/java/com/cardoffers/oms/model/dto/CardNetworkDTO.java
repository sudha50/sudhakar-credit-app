package com.cardoffers.oms.model.dto;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardNetworkDTO {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String code;

    private Boolean active;
}
