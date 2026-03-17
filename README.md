<p align="center">
  <h1 align="center">MeshGate</h1>
  <p align="center">
    A production-ready microservices platform built with Spring Boot, Spring Cloud, and event-driven architecture.
  </p>
</p>

---

## Overview

**MeshGate** is a full-stack microservices application that demonstrates modern distributed systems design — service discovery, API gateway routing, asynchronous messaging, and centralized reverse-proxy configuration. Each service is independently deployable and communicates via REST APIs and RabbitMQ event queues.

## Architecture

```
                          ┌─────────────┐
   Client (Browser)  ───► │    Nginx    │ :80
                          └──────┬──────┘
                   ┌─────────────┼─────────────┐
                   │             │             │
            /rabbitmq/      /api/v1/**     /eureka/
                   │             │             │
           ┌───────▼──┐  ┌──────▼──────┐  ┌───▼────────┐
           │ RabbitMQ  │  │ API Gateway │  │   Eureka   │
           │ :15672    │  │   :8080     │  │   :8761    │
           └───────────┘  └──────┬──────┘  └────────────┘
                                 │
              ┌──────────┬───────┴────────┬──────────────┐
              │          │                │              │
       ┌──────▼───┐ ┌────▼─────┐ ┌───────▼────┐ ┌──────▼──────┐
       │   Auth   │ │   User   │ │  Billing   │ │Notification │
       │  :8086   │ │  :8082   │ │   :8084    │ │   :8083     │
       └──────────┘ └──────────┘ └────────────┘ └─────────────┘
```

## Services

| Service | Port | Description |
|---|---|---|
| **eureka-server** | `8761` | Netflix Eureka service registry — all services register here for discovery |
| **api-gateway** | `8080` | Spring Cloud Gateway — routes `/api/v1/**` requests, handles CORS |
| **auth-service** | `8086` | User authentication (register/login), issues JWT tokens |
| **user-service** | `8082` | User profile management (CRUD operations) |
| **billing-service** | `8084` | Subscription and invoice management, triggered by user registration events |
| **notification-service** | `8083` | Email notifications and in-app alerts, event-driven via RabbitMQ |

## Tech Stack

| Category | Technology |
|---|---|
| Language | Java 21 (LTS) |
| Framework | Spring Boot 3.x, Spring Cloud 2025.x |
| API Gateway | Spring Cloud Gateway (WebFlux) |
| Service Discovery | Netflix Eureka |
| Database | PostgreSQL 15 (Spring Data JPA / Hibernate) |
| Messaging | RabbitMQ (AMQP) for async event-driven communication |
| Reverse Proxy | Nginx |
| Containerization | Docker & Docker Compose |

## API Endpoints

All API routes are exposed through the gateway at `http://localhost:8080/api/v1/`.

### Auth Service — `/api/v1/auth`
| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/register` | Register a new user |
| `POST` | `/login` | Authenticate and receive JWT |

### User Service — `/api/v1/users`
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/` | List all users |
| `GET` | `/{authId}` | Get user profile by auth ID |
| `PUT` | `/{authId}` | Update user profile |

### Billing Service — `/api/v1/billing`
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/health` | Health check |
| `GET` | `/subscriptions/{userId}` | Get user subscription |
| `PUT` | `/subscriptions/{userId}/upgrade?tier=` | Upgrade subscription tier |
| `POST` | `/invoices/{userId}?amount=` | Generate invoice |

### Notification Service — `/api/v1/notifications`
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/health` | Health check |
| `GET` | `/{userId}` | Get notifications for a user |

## Prerequisites

- **Java 21+** (JDK)
- **Apache Maven 3.9+**
- **Docker** & **Docker Compose**

## Getting Started

### 1. Clone the repository
```bash
git clone https://github.com/your-username/meshgate.git
cd meshgate
```

### 2. Start infrastructure (Postgres, RabbitMQ, Nginx)
```bash
docker-compose up -d
```

### 3. Build all services
```bash
# Windows — double-click or run:
build-all.bat

# Linux/macOS:
for dir in eureka-server api-gateway auth-service user-service billing-service notification-service; do
  (cd $dir && mvn clean package -DskipTests -q) && echo "[OK] $dir" || echo "[FAIL] $dir"
done
```

### 4. Start all services
```bash
# Windows — double-click or run:
start-all.bat

# Linux/macOS:
bash start-all.sh
```

### 5. Verify
| Dashboard | URL |
|---|---|
| Eureka | http://localhost:8761 |
| RabbitMQ | http://localhost:15672 (guest/guest) |
| Gateway Health | http://localhost:8080/api/v1/billing/health |
| Via Nginx | http://localhost/api/v1/billing/health |

## Event-Driven Flows

When a user registers via the Auth Service, the following events fire asynchronously through RabbitMQ:

```
[Auth Service] ── UserRegisteredEvent ──► [RabbitMQ Exchange]
                                              │
                                    ┌─────────┴─────────┐
                                    ▼                   ▼
                            [Billing Service]   [Notification Service]
                            Creates FREE sub    Sends welcome email
                            for new user        and saves notification
```

## CORS Strategy

| Traffic Path | CORS Handling |
|---|---|
| `/api/v1/**` → Gateway → Services | API Gateway `CorsConfig.java` (allows `localhost:3000`) |
| `/rabbitmq/**` → RabbitMQ | Nginx `add_header` directives |
| `/eureka/**` → Eureka | Nginx `add_header` directives |

> Microservices themselves do **not** add CORS headers — this is handled at the edge (gateway + nginx).

## Project Structure

```
meshgate/
├── eureka-server/          # Service registry
├── api-gateway/            # Gateway + CORS config + route definitions
├── auth-service/           # Authentication (register, login)
├── user-service/           # User profile management
├── billing-service/        # Subscriptions & invoicing
├── notification-service/   # Email & in-app notifications
├── docker-compose.yml      # Infrastructure containers
├── nginx.conf              # Reverse proxy config
├── build-all.bat           # Build all services (Windows)
├── start-all.bat           # Start all services (Windows)
├── start-all.sh            # Start all services (Linux/macOS)
└── README.md
```

## License

This project is for educational and portfolio purposes.
