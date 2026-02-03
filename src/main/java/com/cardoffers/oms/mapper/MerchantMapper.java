package com.cardoffers.oms.mapper;

import org.mapstruct.Mapper;

import com.cardoffers.oms.model.dto.MerchantDTO;
import com.cardoffers.oms.model.entity.Merchant;

@Mapper(componentModel = "spring")
public interface MerchantMapper {

    MerchantDTO toDTO(Merchant entity);

    Merchant toEntity(MerchantDTO dto);
}
