package com.cardoffers.oms.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cardoffers.oms.exception.ResourceNotFoundException;
import com.cardoffers.oms.mapper.MerchantMapper;
import com.cardoffers.oms.model.dto.MerchantDTO;
import com.cardoffers.oms.model.entity.Merchant;
import com.cardoffers.oms.repository.MerchantRepository;

@Service
@Transactional
public class MerchantServiceImpl implements MerchantService {

    private final MerchantRepository merchantRepository;
    private final MerchantMapper merchantMapper;

    public MerchantServiceImpl(MerchantRepository merchantRepository, MerchantMapper merchantMapper) {
        this.merchantRepository = merchantRepository;
        this.merchantMapper = merchantMapper;
    }

    @Override
    @Cacheable("merchants")
    @Transactional(readOnly = true)
    public MerchantDTO getMerchantById(Long id) {
        Merchant entity = merchantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Merchant not found"));
        return merchantMapper.toDTO(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MerchantDTO> getAllActiveMerchants() {
        return merchantRepository.findByActiveTrue().stream()
                .map(merchantMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MerchantDTO> getMerchantsByCategory(String category) {
        return merchantRepository.findByCategory(category).stream()
                .filter(Merchant::getActive)
                .map(merchantMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value = "merchants", allEntries = true)
    public MerchantDTO createMerchant(MerchantDTO dto) {
        Merchant saved = merchantRepository.save(merchantMapper.toEntity(dto));
        return merchantMapper.toDTO(saved);
    }

    @Override
    @CacheEvict(value = "merchants", allEntries = true)
    public MerchantDTO updateMerchant(Long id, MerchantDTO dto) {
        Merchant existing = merchantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Merchant not found"));

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setCategory(dto.getCategory());
        existing.setLogoUrl(dto.getLogoUrl());
        existing.setWebsite(dto.getWebsite());
        existing.setActive(dto.getActive());

        Merchant saved = merchantRepository.save(existing);
        return merchantMapper.toDTO(saved);
    }
}
