package com.cardoffers.oms.service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cardoffers.oms.exception.ResourceNotFoundException;
import com.cardoffers.oms.mapper.CardholderMapper;
import com.cardoffers.oms.mapper.CardNetworkMapper;
import com.cardoffers.oms.model.dto.CardholderDTO;
import com.cardoffers.oms.model.dto.CardNetworkDTO;
import com.cardoffers.oms.model.entity.Cardholder;
import com.cardoffers.oms.repository.CardholderCardRepository;
import com.cardoffers.oms.repository.CardholderRepository;

@Service
@Transactional
public class CardholderServiceImpl implements CardholderService {

    private final CardholderRepository cardholderRepository;
    private final CardholderCardRepository cardholderCardRepository;
    private final CardholderMapper cardholderMapper;
    private final CardNetworkMapper cardNetworkMapper;

    public CardholderServiceImpl(CardholderRepository cardholderRepository,
            CardholderCardRepository cardholderCardRepository,
            CardholderMapper cardholderMapper,
            CardNetworkMapper cardNetworkMapper) {
        this.cardholderRepository = cardholderRepository;
        this.cardholderCardRepository = cardholderCardRepository;
        this.cardholderMapper = cardholderMapper;
        this.cardNetworkMapper = cardNetworkMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public CardholderDTO getCardholderById(Long id) {
        Cardholder entity = cardholderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cardholder not found"));
        return enrichCardNetworks(cardholderMapper.toDTO(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public CardholderDTO getCardholderByEmail(String email) {
        Cardholder entity = cardholderRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Cardholder not found"));
        return enrichCardNetworks(cardholderMapper.toDTO(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CardholderDTO> getAllActiveCardholders() {
        return cardholderRepository.findByActiveTrue().stream()
                .map(cardholderMapper::toDTO)
                .map(this::enrichCardNetworks)
                .collect(Collectors.toList());
    }

    @Override
    public CardholderDTO createCardholder(CardholderDTO dto) {
        Cardholder entity = cardholderMapper.toEntity(dto);
        Cardholder saved = cardholderRepository.save(entity);
        return enrichCardNetworks(cardholderMapper.toDTO(saved));
    }

    @Override
    public CardholderDTO updateCardholder(Long id, CardholderDTO dto) {
        Cardholder existing = cardholderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cardholder not found"));

        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setEmail(dto.getEmail());
        existing.setPhoneNumber(dto.getPhoneNumber());
        existing.setActive(dto.getActive());

        Cardholder saved = cardholderRepository.save(existing);
        return enrichCardNetworks(cardholderMapper.toDTO(saved));
    }

    private CardholderDTO enrichCardNetworks(CardholderDTO dto) {
        List<CardNetworkDTO> networks = cardholderCardRepository
                .findByCardholderIdAndActiveTrue(dto.getId()).stream()
                .map(cc -> cc.getCardNetwork())
                .filter(Objects::nonNull)
                .distinct()
                .map(cardNetworkMapper::toDTO)
                .sorted(Comparator.comparing(CardNetworkDTO::getCode, Comparator.nullsLast(String::compareToIgnoreCase)))
                .collect(Collectors.toList());

        dto.setCardNetworks(networks);
        return dto;
    }
}
