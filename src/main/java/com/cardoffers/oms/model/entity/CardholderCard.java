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
@Table(name = "cardholder_cards")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardholderCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cardholder_id")
    private Cardholder cardholder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_network_id")
    private CardNetwork cardNetwork;

    @Column(name = "card_number_last_four")
    private String cardNumberLastFour;

    @Column(name = "card_type")
    private String cardType;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    private Boolean active;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.active == null) {
            this.active = true;
        }
    }
}
