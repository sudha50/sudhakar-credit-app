package com.cardoffers.oms.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cardoffers.oms.exception.ResourceNotFoundException;
import com.cardoffers.oms.mapper.CardNetworkMapper;
import com.cardoffers.oms.model.dto.CardNetworkDTO;
import com.cardoffers.oms.model.entity.CardNetwork;
import com.cardoffers.oms.repository.CardNetworkRepository;

@Service
@Transactional
public class CardNetworkServiceImpl implements CardNetworkService {

    private final CardNetworkRepository cardNetworkRepository;
    private final CardNetworkMapper cardNetworkMapper;

    public CardNetworkServiceImpl(CardNetworkRepository cardNetworkRepository, CardNetworkMapper cardNetworkMapper) {
        this.cardNetworkRepository = cardNetworkRepository;
        this.cardNetworkMapper = cardNetworkMapper;
    }

    @Override
    @Cacheable("cardNetworks")
    @Transactional(readOnly = true)
    public CardNetworkDTO getCardNetworkById(Long id) {
        CardNetwork entity = cardNetworkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card network not found"));
        return cardNetworkMapper.toDTO(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CardNetworkDTO> getAllActiveCardNetworks() {
        return cardNetworkRepository.findByActiveTrue().stream()
                .map(cardNetworkMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CardNetworkDTO getCardNetworkByCode(String code) {
        CardNetwork entity = cardNetworkRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Card network not found"));
        return cardNetworkMapper.toDTO(entity);
    }
}
