# Polaris API

**REST API for the Polaris TPA Analytics Platform**

A Java/Spring Boot backend for maritime insurance management, serving the same data and functionality as the original Next.js/Google Apps Script implementation.

## Tech Stack

- **Java 17** (Eclipse Temurin)
- **Spring Boot 3.2.4**
- **Spring Data JPA** + Hibernate
- **Spring Security** + JWT Authentication
- **H2 Database** (development) / **PostgreSQL** (production)
- **Swagger/OpenAPI** documentation
- **Maven** build system

## Quick Start

```bash
# Clone the repository
git clone https://github.com/Apostolos-Bizou/Polaris-API.git
cd Polaris-API

# Build and run
mvn spring-boot:run

# Access
# API:      http://localhost:8080
# Swagger:  http://localhost:8080/swagger-ui.html
# H2 Console: http://localhost:8080/h2-console
# Health:   http://localhost:8080/api/health
```

## Authentication

```bash
# Login to get JWT token
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"tolis_admin","password":"admin123"}'

# Use token in subsequent requests
curl http://localhost:8080/api/dashboard/kpis \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/auth/login | Authenticate and get JWT |
| GET | /api/health | System health check |
| GET | /api/dashboard/kpis | Dashboard KPIs |
| GET | /api/clients | All clients list |
| GET | /api/clients/{id} | Client details |
| GET | /api/clients/{id}/kpis | Client KPI summary |

## Default Users

| Username | Password | Role |
|----------|----------|------|
| tolis_admin | admin123 | admin |
| nik_admin | admin123 | admin |
| tolis | admin123 | client |
| apostolos | 5404775 | client |

## Project Structure

```
src/main/java/com/polaris/api/
├── PolarisApiApplication.java    # Main entry point
├── config/                        # Security, CORS, Swagger, Data seeding
├── controller/                    # REST endpoints
├── dto/                           # Request/Response objects
├── model/                         # JPA entities
├── repository/                    # Spring Data repositories
└── security/                      # JWT filter and utilities
```

## Related Projects

- [Polaris-Next](https://github.com/Apostolos-Bizou/Polaris-Next) - Next.js/React frontend
- Polaris-Vue (coming soon) - Vue.js/Nuxt 3 frontend
- Polaris-AI (coming soon) - Python AI microservice

## License

Proprietary - Polaris TPA Platform
