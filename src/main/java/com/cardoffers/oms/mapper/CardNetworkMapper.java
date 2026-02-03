package com.cardoffers.oms.mapper;

import org.mapstruct.Mapper;

import com.cardoffers.oms.model.dto.CardNetworkDTO;
import com.cardoffers.oms.model.entity.CardNetwork;

@Mapper(componentModel = "spring")
public interface CardNetworkMapper {

    CardNetworkDTO toDTO(CardNetwork entity);

    CardNetwork toEntity(CardNetworkDTO dto);
}
