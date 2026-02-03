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
- PostgreSQL 14+ (or Docker)
- Redis 6+ (or Docker)

### Runtime Dependencies (Developer / DevOps)
- PostgreSQL
  - Required for persistence and Flyway migrations (`ddl-auto: validate`)
- Redis
  - Required for Spring Cache (10-minute TTL)

### Configure Environment Variables (optional)
The app supports environment-based configuration:

- `DB_HOST` (default `localhost`)
- `DB_PORT` (default `5432`)
- `DB_NAME` (default `offers_db`)
- `DB_USERNAME` (default `postgres`)
- `DB_PASSWORD` (default `postgres`)
- `REDIS_HOST` (default `localhost`)
- `REDIS_PORT` (default `6379`)

#### .env files
- `.env` is intended for local developer use and is ignored by git.
- `.env.example` is a template you can copy.

Spring Boot does not automatically read `.env` by default. Either:
- Export variables in your terminal session, or
- Use your IDE run configuration to set environment variables.

##### Load `.env` in PowerShell (Windows)
Run from project root:

```powershell
Get-Content .env | ForEach-Object {
  if ($_ -match '^\s*(#|$)') { return }
  $n,$v = $_ -split '=',2
  Set-Item -Path "Env:$($n.Trim())" -Value $v.Trim()
}
```

### Docker (Recommended for local development)

#### PostgreSQL

```powershell
docker run --name pg-server `
  -e POSTGRES_DB=offers_db `
  -e POSTGRES_USER=postgres `
  -e POSTGRES_PASSWORD=postgres `
  -p 5432:5432 `
  -d postgres:16
```

Verify:

```powershell
docker exec -it pg-server psql -U postgres -d offers_db -c "SELECT 1;"
```

#### Redis

```powershell
docker run --name redis-server -p 6379:6379 -d redis:7
```

Verify:

```powershell
docker exec -it redis-server redis-cli ping
```

### Run
```bash
mvn spring-boot:run
```

Flyway will automatically create schema and insert sample data on startup.

### Build (DevOps)

```bash
mvn -q clean package
```

Run the packaged JAR:

```bash
java -jar target/offer-management-system-0.0.1-SNAPSHOT.jar
```

Note: the project forces JVM timezone to UTC for `spring-boot:run` to avoid PostgreSQL rejecting certain system timezone identifiers.

## API Documentation
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Operations / DevOps Runbook

### Health / Readiness
- Liveness/health:
  - `GET /actuator/health`

### Logs
- Default log level:
  - `com.cardoffers`: `DEBUG`
  - `org.hibernate.SQL`: `DEBUG`

### Database Migrations (Flyway)
- Enabled by default.
- Migration scripts:
  - `src/main/resources/db/migration/V1__Create_initial_schema.sql`
  - `src/main/resources/db/migration/V2__Insert_sample_data.sql`

### Caching (Redis)
- Cache manager:
  - Redis with JSON serialization
  - Default TTL: 10 minutes

Cache names used:
- `merchants`
- `cardNetworks`
- `offers`
- `activeOffers`
- `eligibleOffers` (keyed by `cardholderId`)

### Configuration
The app is configured via environment variables (recommended for DevOps):
- PostgreSQL:
  - `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`
- Redis:
  - `REDIS_HOST`, `REDIS_PORT`

### Troubleshooting

#### PostgreSQL authentication failures
- Ensure the container/user/password matches your environment variables.
- Validate with:

```powershell
docker exec -it pg-server psql -U <user> -d <db> -c "SELECT 1;"
```

#### Schema validation errors on startup
- JPA is configured with `ddl-auto: validate`, meaning:
  - The app expects Flyway schema to match entity mappings.

#### Redis SerializationException
- If you change DTOs/entities and cached data becomes incompatible, flush Redis:

```powershell
docker exec -it redis-server redis-cli FLUSHALL
```

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
