package com.cardoffers.oms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cardoffers.oms.model.dto.OfferDTO;
import com.cardoffers.oms.model.dto.OfferSummaryDTO;
import com.cardoffers.oms.model.entity.Offer;

@Mapper(componentModel = "spring", uses = { MerchantMapper.class, CardNetworkMapper.class })
public interface OfferMapper {

    OfferDTO toDTO(Offer entity);

    Offer toEntity(OfferDTO dto);

    @Mapping(target = "merchantName", source = "merchant.name")
    OfferSummaryDTO toSummaryDTO(Offer entity);
}
