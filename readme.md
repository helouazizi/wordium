
# Wordium: Enterprise Event-Driven Microservices Ecosystem

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4-green)
![Angular](https://img.shields.io/badge/Angular-16+-red)
![Docker](https://img.shields.io/badge/Docker-Enabled-blue)
![Kafka](https://img.shields.io/badge/Apache%20Kafka-Messaging-black)
![Redis](https://img.shields.io/badge/Redis-Caching-red)

**Wordium** is a high-performance, scalable blogging and social interaction platform built on a modern Microservices architecture. It demonstrates advanced distributed system patterns, and automated infrastructure management.

---

## 🏗️ Architecture at a Glance

The system is architected to handle high concurrency and ensure fault tolerance through a decoupled, event-driven design.

- **API Gateway Pattern:** Centralized entry point for routing, security, and cross-cutting concerns.
- **Service Discovery:** Dynamic service registration and health monitoring via **Netflix Eureka**.
- **Event-Driven Messaging:** Asynchronous service communication and decoupling using **Apache Kafka**.
- **Distributed Caching:** Low-latency data retrieval and session management powered by **Redis**.
- **Database-per-Service:** Strict data isolation using multiple **PostgreSQL** instances to ensure independent scalability.

---

## 🛠️ Technology Stack

| Layer | Technologies |
| :--- | :--- |
| **Frontend** | Angular, RxJS, Tailwind CSS |
| **Backend** | Java 17, Spring Boot 3.4, Spring Security (JWT) |
| **Messaging** | Apache Kafka, Zookeeper |
| **Caching** | Redis (Lettuce Driver) |
| **Database** | PostgreSQL |
| **DevOps** | Docker, Docker Compose, Shell Automation |
| **Media** | Cloudinary (CDN Integration) |

---

## 📖 In-Depth Documentation

We follow a "Documentation-as-Code" philosophy. For detailed insights, please refer to the specific modules below:

### 🏛️ System Design

- **[Core Architecture](./docs/architecture/system-design.md):** High-level component map and design decisions.
- **[Sequence Diagrams](./docs/architecture/sequence-diagrams.md):** Visualizing authentication and data flows.
- **[Roadmap](./docs/architecture/next-design.md):** Future implementation of Spring Cloud Config and Service Mesh.

### ⚙️ Infrastructure & Environment

- **[Setup & Installation Guide](./docs/env/setup-guide.md):** Crucial instructions for local deployment and `.env` configuration.
- **[Event-Driven Logic (Kafka)](./docs/infrastructure/messaging-kafka.md):** Topic definitions and producer/consumer strategies.
- **[Caching Layer (Redis)](./docs/infrastructure/caching.md):** Cache-aside patterns and session registry logic.

### 🔌 API Reference

- **[OpenAPI / Swagger Specs](./docs/swagger/):** Raw JSON definitions for Auth, Posts, Users, and WebSocket Gateway.

---

## 🚀 Quick Start

1. **Clone the project:**

   ```bash
   git clone https://github.com/helouazizi/wordium.git
   cd wordium
   ```

2. **Initialize Environment:**

   ```bash
   chmod +x setup.sh
   ./setup.sh
   ```

   *(See [Setup Guide](./docs/env/setup-guide.md) for Cloudinary credentials warning).*

3. **Build Artifacts:**

   ```bash
   chmod +x build.sh
   ./build.sh
   ```

### 4. Launch Ecosystem & Verification

Once the environment is configured and the JAR files are built, spin up the entire microservices cluster using Docker:

```bash
# Using the modern Docker Compose V2
docker compose up -d --build

# OR using the legacy docker-compose
docker-compose up -d --build
```

#### ✅ Health Check

After the startup process completes, verify that the ecosystem is fully operational. Wordium is a complex system consisting of **15 active containers** (Infrastructure + Services + Databases).

Run the following command to check the status:

```bash
docker ps
```

**Expected Result:** You should see **15 services** in the `Up` (or `Up (healthy)`) status. This includes:

- **Infrastructure (4):** Eureka Server, Kafka, Zookeeper, Redis.
- **Databases (4):** PostgreSQL instances for Auth, Users, Posts, and WSGateway.
- **Backend Services (6):** API Gateway, Auth, Users, Posts, WSGateway, and Test Service.
- **Frontend (1):** Angular UI (Port 4200).

---

## 🌐 Service Access Points

| Service | URL | Purpose |
| :--- | :--- | :--- |
| **Frontend UI** | [http://localhost:4200](http://localhost:4200) | Main Angular User Interface |
| **Discovery Server** | [http://localhost:8761](http://localhost:8761) | Eureka Dashboard (Monitor service health) |
| **API Gateway** | [http://localhost:8080](http://localhost:8080) | Entry point for all Backend APIs |
| **Swagger UI** | [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) | Interactive API Documentation |
| **Kafka UI** | [http://localhost:9092](http://localhost:9092) | External Kafka Broker access |

## 👨‍💻 Key Development Features

- **Automation Scripts:** Custom Shell scripts for environment synchronization and multi-service builds.
- **Security:** Distributed JWT validation at the Gateway level.
- **Resilience:** Implemented retries and health checks for Kafka/PostgreSQL containers.

---

## 🤝 Contributing

Please read our development workflow in the `dev` branch. Pull requests are welcome for any architectural improvements!
