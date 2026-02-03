INSERT INTO card_networks (name, code, active) VALUES
('VISA', 'VIS', TRUE),
('MASTERCARD', 'MAS', TRUE),
('AMEX', 'AMX', TRUE)
ON CONFLICT (code) DO NOTHING;

INSERT INTO merchants (name, description, category, logo_url, website, active) VALUES
('FreshMart', 'Grocery and daily essentials', 'RETAIL', 'https://example.com/logo/freshmart.png', 'https://freshmart.example.com', TRUE),
('SkyHigh Airlines', 'Domestic and international flights', 'TRAVEL', 'https://example.com/logo/skyhigh.png', 'https://skyhigh.example.com', TRUE),
('Dine & Delight', 'Casual dining restaurant chain', 'DINING', 'https://example.com/logo/dinedelight.png', 'https://dinedelight.example.com', TRUE),
('TechTown', 'Electronics and gadgets', 'RETAIL', 'https://example.com/logo/techtown.png', 'https://techtown.example.com', TRUE),
('StayEasy Hotels', 'Hotel bookings and stays', 'TRAVEL', 'https://example.com/logo/stayeasy.png', 'https://stayeasy.example.com', TRUE)
ON CONFLICT DO NOTHING;

INSERT INTO cardholders (first_name, last_name, email, phone_number, active)
VALUES
('Aarav', 'Sharma', 'aarav.sharma@example.com', '+91-99999-00001', TRUE),
('Diya', 'Iyer', 'diya.iyer@example.com', '+91-99999-00002', TRUE),
('Kabir', 'Verma', 'kabir.verma@example.com', '+91-99999-00003', TRUE)
ON CONFLICT (email) DO NOTHING;

INSERT INTO cardholder_cards (cardholder_id, card_network_id, card_number_last_four, card_type, active)
SELECT ch.id, cn.id, v.last_four, v.card_type, TRUE
FROM (
    VALUES
    ('aarav.sharma@example.com', 'VIS', '1234', 'CREDIT'),
    ('aarav.sharma@example.com', 'MAS', '2222', 'DEBIT'),
    ('diya.iyer@example.com', 'AMX', '9876', 'CREDIT'),
    ('kabir.verma@example.com', 'VIS', '5555', 'DEBIT')
) AS v(email, code, last_four, card_type)
JOIN cardholders ch ON ch.email = v.email
JOIN card_networks cn ON cn.code = v.code;

INSERT INTO offers (
    title, description, offer_type, discount_percentage, cashback_amount, minimum_purchase_amount,
    start_date, end_date, terms_and_conditions, merchant_id, card_network_id, source,
    max_redemptions, current_redemptions, active
)
SELECT o.title, o.description, o.offer_type, o.discount_percentage, o.cashback_amount, o.minimum_purchase_amount,
       o.start_date, o.end_date, o.terms_and_conditions, m.id, cn.id, o.source,
       o.max_redemptions, o.current_redemptions, o.active
FROM (
    VALUES
    ('10% off groceries', 'Save on your grocery bill', 'DISCOUNT', 10.00, NULL, 500.00, CURRENT_DATE - 10, CURRENT_DATE + 40, 'Valid on weekdays only', 'FreshMart', 'VIS', 'NETWORK', 1000, 120, TRUE),
    ('₹200 cashback on electronics', 'Cashback on electronics purchase', 'CASHBACK', NULL, 200.00, 2000.00, CURRENT_DATE - 5, CURRENT_DATE + 60, 'Max 1 per card per week', 'TechTown', 'MAS', 'THIRD_PARTY', 300, 50, TRUE),
    ('5% dining discount', 'Discount at partner restaurants', 'DISCOUNT', 5.00, NULL, 300.00, CURRENT_DATE - 1, CURRENT_DATE + 20, 'Not valid on alcohol', 'Dine & Delight', 'AMX', 'NETWORK', NULL, 10, TRUE),
    ('Travel points bonus', 'Earn bonus points on flights', 'POINTS', NULL, NULL, 5000.00, CURRENT_DATE - 15, CURRENT_DATE + 90, 'Only on online bookings', 'SkyHigh Airlines', 'VIS', 'AGGREGATOR', 500, 200, TRUE),
    ('15% hotel discount', 'Save on hotel stays', 'DISCOUNT', 15.00, NULL, 8000.00, CURRENT_DATE - 3, CURRENT_DATE + 30, 'Blackout dates apply', 'StayEasy Hotels', 'MAS', 'NETWORK', 200, 25, TRUE),

    ('₹100 cashback on groceries', 'Cashback on grocery shopping', 'CASHBACK', NULL, 100.00, 700.00, CURRENT_DATE - 7, CURRENT_DATE + 14, 'Valid once per month', 'FreshMart', 'AMX', 'THIRD_PARTY', 150, 140, TRUE),
    ('2x points on dining', 'Double points on dining spends', 'POINTS', NULL, NULL, 500.00, CURRENT_DATE - 2, CURRENT_DATE + 25, 'Applies automatically', 'Dine & Delight', 'VIS', 'NETWORK', NULL, 0, TRUE),
    ('₹500 flight cashback', 'Cashback on flight bookings', 'CASHBACK', NULL, 500.00, 10000.00, CURRENT_DATE - 20, CURRENT_DATE + 10, 'Limited seats', 'SkyHigh Airlines', 'MAS', 'AGGREGATOR', 50, 49, TRUE),
    ('10% off gadgets', 'Discount on select gadgets', 'DISCOUNT', 10.00, NULL, 1500.00, CURRENT_DATE - 1, CURRENT_DATE + 45, 'Select SKUs only', 'TechTown', 'VIS', 'NETWORK', 100, 1, TRUE),
    ('Bonus ₹300 on hotels', 'Bonus cashback on hotel bookings', 'BONUS', NULL, 300.00, 12000.00, CURRENT_DATE - 5, CURRENT_DATE + 55, 'New users only', 'StayEasy Hotels', 'AMX', 'THIRD_PARTY', 80, 5, TRUE),

    ('Expired sample offer', 'Should not appear in active queries', 'DISCOUNT', 20.00, NULL, 1000.00, CURRENT_DATE - 40, CURRENT_DATE - 1, 'Expired', 'FreshMart', 'VIS', 'NETWORK', 100, 0, FALSE)
) AS o(title, description, offer_type, discount_percentage, cashback_amount, minimum_purchase_amount,
       start_date, end_date, terms_and_conditions, merchant_name, network_code, source,
       max_redemptions, current_redemptions, active)
JOIN merchants m ON m.name = o.merchant_name
JOIN card_networks cn ON cn.code = o.network_code;
