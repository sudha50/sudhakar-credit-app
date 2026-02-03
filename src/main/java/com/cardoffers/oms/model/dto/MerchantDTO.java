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
public class MerchantDTO {

    private Long id;

    @NotBlank
    private String name;

    private String description;

    @NotBlank
    private String category;

    private String logoUrl;

    private String website;

    private Boolean active;
}
