package com.cardoffers.oms.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cardoffers.oms.exception.InvalidOfferException;
import com.cardoffers.oms.exception.ResourceNotFoundException;
import com.cardoffers.oms.mapper.OfferMapper;
import com.cardoffers.oms.model.dto.OfferDTO;
import com.cardoffers.oms.model.dto.OfferSummaryDTO;
import com.cardoffers.oms.model.entity.CardNetwork;
import com.cardoffers.oms.model.entity.Merchant;
import com.cardoffers.oms.model.entity.Offer;
import com.cardoffers.oms.repository.CardNetworkRepository;
import com.cardoffers.oms.repository.MerchantRepository;
import com.cardoffers.oms.repository.OfferRepository;

@Service
@Transactional
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final MerchantRepository merchantRepository;
    private final CardNetworkRepository cardNetworkRepository;
    private final OfferMapper offerMapper;

    public OfferServiceImpl(OfferRepository offerRepository,
            MerchantRepository merchantRepository,
            CardNetworkRepository cardNetworkRepository,
            OfferMapper offerMapper) {
        this.offerRepository = offerRepository;
        this.merchantRepository = merchantRepository;
        this.cardNetworkRepository = cardNetworkRepository;
        this.offerMapper = offerMapper;
    }

    @Override
    @Cacheable(value = "offers", key = "#id")
    @Transactional(readOnly = true)
    public OfferDTO getOfferById(Long id) {
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Offer not found"));
        return offerMapper.toDTO(offer);
    }

    @Override
    @Cacheable(value = "activeOffers")
    @Transactional(readOnly = true)
    public List<OfferDTO> getAllActiveOffers() {
        return offerRepository.findActiveOffers(LocalDate.now()).stream()
                .map(offerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OfferSummaryDTO> getOffersByMerchant(Long merchantId) {
        return offerRepository.findActiveOffersByMerchant(merchantId).stream()
                .map(offerMapper::toSummaryDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OfferSummaryDTO> getOffersByCardNetwork(Long networkId) {
        return offerRepository.findActiveOffersByCardNetwork(networkId).stream()
                .map(offerMapper::toSummaryDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "offers", allEntries = true),
            @CacheEvict(value = "activeOffers", allEntries = true)
    })
    public OfferDTO createOffer(OfferDTO dto) {
        validateOfferDates(dto);

        Merchant merchant = merchantRepository.findById(dto.getMerchant().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Merchant not found"));
        CardNetwork network = cardNetworkRepository.findById(dto.getCardNetwork().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Card network not found"));

        Offer entity = offerMapper.toEntity(dto);
        entity.setMerchant(merchant);
        entity.setCardNetwork(network);

        Offer saved = offerRepository.save(entity);
        return offerMapper.toDTO(saved);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "offers", key = "#id"),
            @CacheEvict(value = "activeOffers", allEntries = true)
    })
    public OfferDTO updateOffer(Long id, OfferDTO dto) {
        validateOfferDates(dto);

        Offer existing = offerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Offer not found"));

        Merchant merchant = merchantRepository.findById(dto.getMerchant().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Merchant not found"));
        CardNetwork network = cardNetworkRepository.findById(dto.getCardNetwork().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Card network not found"));

        existing.setTitle(dto.getTitle());
        existing.setDescription(dto.getDescription());
        existing.setOfferType(dto.getOfferType());
        existing.setDiscountPercentage(dto.getDiscountPercentage());
        existing.setCashbackAmount(dto.getCashbackAmount());
        existing.setMinimumPurchaseAmount(dto.getMinimumPurchaseAmount());
        existing.setStartDate(dto.getStartDate());
        existing.setEndDate(dto.getEndDate());
        existing.setTermsAndConditions(dto.getTermsAndConditions());
        existing.setSource(dto.getSource());
        existing.setMaxRedemptions(dto.getMaxRedemptions());
        if (dto.getCurrentRedemptions() != null) {
            existing.setCurrentRedemptions(dto.getCurrentRedemptions());
        }
        existing.setActive(dto.getActive());
        existing.setMerchant(merchant);
        existing.setCardNetwork(network);

        Offer saved = offerRepository.save(existing);
        return offerMapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OfferDTO> searchOffers(String keyword, String offerType, String category) {
        String kw = keyword == null ? null : keyword.toLowerCase(Locale.ROOT).trim();
        String ot = offerType == null ? null : offerType.toUpperCase(Locale.ROOT).trim();
        String cat = category == null ? null : category.toUpperCase(Locale.ROOT).trim();

        return offerRepository.findActiveOffers(LocalDate.now()).stream()
                .filter(o -> kw == null || containsIgnoreCase(o.getTitle(), kw) || containsIgnoreCase(o.getDescription(), kw))
                .filter(o -> ot == null || (o.getOfferType() != null && o.getOfferType().toUpperCase(Locale.ROOT).equals(ot)))
                .filter(o -> cat == null || (o.getMerchant() != null && o.getMerchant().getCategory() != null
                        && o.getMerchant().getCategory().toUpperCase(Locale.ROOT).equals(cat)))
                .map(offerMapper::toDTO)
                .collect(Collectors.toList());
    }

    private boolean containsIgnoreCase(String value, String keywordLower) {
        if (value == null) {
            return false;
        }
        return value.toLowerCase(Locale.ROOT).contains(keywordLower);
    }

    private void validateOfferDates(OfferDTO dto) {
        if (dto.getStartDate() != null && dto.getEndDate() != null && dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new InvalidOfferException("Offer endDate must be on/after startDate");
        }
        if (Objects.equals(dto.getMaxRedemptions(), 0)) {
            throw new InvalidOfferException("maxRedemptions must be greater than 0 when provided");
        }
    }
}
