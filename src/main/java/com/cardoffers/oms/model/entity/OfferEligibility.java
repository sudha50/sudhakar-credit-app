package com.cardoffers.oms.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "offer_eligibility")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferEligibility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offer_id")
    private Offer offer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cardholder_id")
    private Cardholder cardholder;

    @Column(name = "is_eligible")
    private Boolean isEligible;

    @Column(name = "eligibility_reason")
    private String eligibilityReason;

    @Column(name = "checked_at", nullable = false)
    private LocalDateTime checkedAt;

    @PrePersist
    public void prePersist() {
        if (this.checkedAt == null) {
            this.checkedAt = LocalDateTime.now();
        }
    }
}
