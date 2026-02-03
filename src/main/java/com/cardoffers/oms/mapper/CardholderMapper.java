package com.cardoffers.oms.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cardoffers.oms.model.dto.CardholderDTO;
import com.cardoffers.oms.model.entity.Cardholder;

@Mapper(componentModel = "spring")
public interface CardholderMapper {

    @Mapping(target = "cardNetworks", ignore = true)
    CardholderDTO toDTO(Cardholder entity);

    @Mapping(target = "cards", ignore = true)
    Cardholder toEntity(CardholderDTO dto);

    List<CardholderDTO> toDTOs(List<Cardholder> entities);
}
