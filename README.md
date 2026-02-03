# Offer Management System (Card Offers)

## Overview
A Spring Boot (3.2+), Java 21 REST API for managing card offers, merchants, card networks, and cardholder eligibility. Uses PostgreSQL (Flyway migrations) and Redis for caching.

## Tech Stack
- Java 21 (LTS)
- Spring Boot 3.2+
- Spring Web, Spring Data JPA
- PostgreSQL + Flyway
- Redis (Lettuce) + Spring Cache
- MapStruct + Lombok
- SpringDoc OpenAPI

## Setup Instructions

### Prerequisites
- Java 21
- Maven 3.9+
- PostgreSQL 14+
- Redis 6+

### Configure Environment Variables (optional)
The app supports environment-based configuration:

- `DB_HOST` (default `localhost`)
- `DB_PORT` (default `5432`)
- `DB_NAME` (default `offers_db`)
- `DB_USERNAME` (default `postgres`)
- `DB_PASSWORD` (default `postgres`)
- `REDIS_HOST` (default `localhost`)
- `REDIS_PORT` (default `6379`)

### Run
```bash
mvn spring-boot:run
```

Flyway will automatically create schema and insert sample data on startup.

## API Documentation
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Main Business Endpoint (Critical)
`GET /api/v1/eligible-offers/cardholder/{cardholderId}`

Returns all active offers the cardholder is eligible for based on their active card networks, active offer dates, and redemption limits.

## Endpoints

### Cardholders
- `GET /api/v1/cardholders/{id}`
- `GET /api/v1/cardholders/email/{email}`
- `GET /api/v1/cardholders`
- `POST /api/v1/cardholders`
- `PUT /api/v1/cardholders/{id}`

### Merchants
- `GET /api/v1/merchants/{id}`
- `GET /api/v1/merchants`
- `GET /api/v1/merchants/category/{category}`
- `POST /api/v1/merchants`
- `PUT /api/v1/merchants/{id}`

### Card Networks
- `GET /api/v1/card-networks`
- `GET /api/v1/card-networks/{id}`
- `GET /api/v1/card-networks/code/{code}`

### Offers
- `GET /api/v1/offers`
- `GET /api/v1/offers/{id}`
- `GET /api/v1/offers/merchant/{merchantId}`
- `GET /api/v1/offers/network/{networkId}`
- `GET /api/v1/offers/search?keyword=&offerType=&category=`
- `POST /api/v1/offers`
- `PUT /api/v1/offers/{id}`

### Eligible Offers (Main Use Case)
- `GET /api/v1/eligible-offers/cardholder/{cardholderId}`
- `GET /api/v1/eligible-offers/cardholder/{cardholderId}/category/{category}`
- `GET /api/v1/eligible-offers/cardholder/{cardholderId}/type/{offerType}`

## Redis Caching Strategy
- `merchants` and `cardNetworks` are cached for quick lookup.
- `offers` caches individual offers by ID, and `activeOffers` caches the active offer list.
- `eligibleOffers` caches per-cardholder eligibility results (`key = cardholderId`) with TTL = 10 minutes.

## Sample cURL Calls

```bash
curl -s http://localhost:8080/api/v1/cardholders
```

```bash
curl -s http://localhost:8080/api/v1/offers
```

```bash
curl -s http://localhost:8080/api/v1/eligible-offers/cardholder/1
```

## Database Schema Diagram (High Level)

```text
cardholders (1) ---- (N) cardholder_cards (N) ---- (1) card_networks
     |                                         |
     |                                         | (1)
     |                                         v
     |                                    offers (N) ---- (1) merchants
     |
     v
offer_eligibility (optional historical tracking)
```
