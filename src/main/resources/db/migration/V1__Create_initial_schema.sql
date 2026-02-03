CREATE TABLE IF NOT EXISTS cardholders (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS merchants (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    category VARCHAR(100) NOT NULL,
    logo_url VARCHAR(1000),
    website VARCHAR(1000),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS card_networks (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    code VARCHAR(10) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS offers (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(2000),
    offer_type VARCHAR(50) NOT NULL,
    discount_percentage NUMERIC(7, 4),
    cashback_amount NUMERIC(12, 2),
    minimum_purchase_amount NUMERIC(12, 2),
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    terms_and_conditions TEXT,
    merchant_id BIGINT NOT NULL,
    card_network_id BIGINT NOT NULL,
    source VARCHAR(50) NOT NULL,
    max_redemptions INTEGER,
    current_redemptions INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_offers_merchant FOREIGN KEY (merchant_id) REFERENCES merchants (id),
    CONSTRAINT fk_offers_card_network FOREIGN KEY (card_network_id) REFERENCES card_networks (id),
    CONSTRAINT chk_offer_dates CHECK (end_date >= start_date),
    CONSTRAINT chk_redemptions_non_negative CHECK (current_redemptions >= 0)
);

CREATE TABLE IF NOT EXISTS cardholder_cards (
    id BIGSERIAL PRIMARY KEY,
    cardholder_id BIGINT NOT NULL,
    card_network_id BIGINT NOT NULL,
    card_number_last_four VARCHAR(4) NOT NULL,
    card_type VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_cardholder_cards_cardholder FOREIGN KEY (cardholder_id) REFERENCES cardholders (id),
    CONSTRAINT fk_cardholder_cards_card_network FOREIGN KEY (card_network_id) REFERENCES card_networks (id)
);

CREATE TABLE IF NOT EXISTS offer_eligibility (
    id BIGSERIAL PRIMARY KEY,
    offer_id BIGINT NOT NULL,
    cardholder_id BIGINT NOT NULL,
    is_eligible BOOLEAN NOT NULL,
    eligibility_reason VARCHAR(1000),
    checked_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_offer_eligibility_offer FOREIGN KEY (offer_id) REFERENCES offers (id),
    CONSTRAINT fk_offer_eligibility_cardholder FOREIGN KEY (cardholder_id) REFERENCES cardholders (id)
);

CREATE INDEX IF NOT EXISTS idx_cardholders_email ON cardholders (email);
CREATE INDEX IF NOT EXISTS idx_cardholders_active ON cardholders (active);

CREATE INDEX IF NOT EXISTS idx_merchants_active ON merchants (active);
CREATE INDEX IF NOT EXISTS idx_merchants_category ON merchants (category);

CREATE INDEX IF NOT EXISTS idx_card_networks_active ON card_networks (active);
CREATE INDEX IF NOT EXISTS idx_card_networks_code ON card_networks (code);

CREATE INDEX IF NOT EXISTS idx_offers_merchant_id ON offers (merchant_id);
CREATE INDEX IF NOT EXISTS idx_offers_card_network_id ON offers (card_network_id);
CREATE INDEX IF NOT EXISTS idx_offers_active ON offers (active);
CREATE INDEX IF NOT EXISTS idx_offers_dates ON offers (start_date, end_date);
CREATE INDEX IF NOT EXISTS idx_offers_offer_type ON offers (offer_type);

CREATE INDEX IF NOT EXISTS idx_cardholder_cards_cardholder_id ON cardholder_cards (cardholder_id);
CREATE INDEX IF NOT EXISTS idx_cardholder_cards_card_network_id ON cardholder_cards (card_network_id);
CREATE INDEX IF NOT EXISTS idx_cardholder_cards_active ON cardholder_cards (active);

CREATE INDEX IF NOT EXISTS idx_offer_eligibility_cardholder_id ON offer_eligibility (cardholder_id);
CREATE INDEX IF NOT EXISTS idx_offer_eligibility_offer_id ON offer_eligibility (offer_id);
CREATE INDEX IF NOT EXISTS idx_offer_eligibility_is_eligible ON offer_eligibility (is_eligible);
