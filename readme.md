# Wordium Project

## Overview

Wordium is a microservices-based full-stack project with Angular frontend and Spring Boot backend. It features JWT authentication, role-based access, a centralized API Gateway, and professional DevSecOps practices.

## Features

- User, Product, and Order microservices
- JWT authentication & authorization
- API Gateway with aggregated Swagger documentation
- Dockerized microservices for easy deployment
- CI/CD-ready structure
- Angular frontend with generated API client

## Documentation

- `docs/api` – Swagger/OpenAPI spec and API instructions
- `docs/architecture` – diagrams and sequence flows
- `docs/devsecops` – security checklist and threat model
- `docs/stack` – technology stack
- `docs/frontend` – frontend structure & generated API usage
- `docs/env` – environment variables template
- `docs/tests` – testing instructions

## Setup

1. Clone repository
2. Copy `.env.example` to `.env` and fill variables
3. Run `docker-compose up --build`
4. Swagger UI: `http://localhost:8080/swagger-ui/index.html`
5. Angular frontend: `http://localhost:4200`


```
wordium/
├── backend/
│   ├── auth-service/
│   ├── user-service/
│   ├── content-service/
│   ├── notification-service/
│   └── gateway-service/
│
├── frontend/              # ← Add this (Angular)
│   ├── wordium-ui/
│
├── docs/                  # ← Add this
│   ├── architecture/
│   │   ├── microservices.png
│   │   ├── sequence-diagrams.md
│   │   ├── deployment-diagram.png
│   ├── api/
│   │   ├── swagger.json
│   │   ├── endpoints.md
│   ├── devsecops/
│   │   ├── threat-model.md
│   │   ├── security-checklist.md
│   └── readme.md
│
├── .github/
│   └── workflows/
│       ├── backend-ci.yml
│       ├── frontend-ci.yml
│       ├── security-scans.yml
│       └── docker-deploy.yml
│
├── docker/
│   ├── auth-service.Dockerfile
│   ├── user-service.Dockerfile
│   ├── gateway.Dockerfile
│   └── nginx.Dockerfile
│
├── kubernetes/            # Optional but professional
│   ├── deployments/
│   ├── services/
│   ├── ingress/
│   ├── configmaps/
│   └── secrets/
│
├── tests/
│   ├── backend/
│   │   ├── unit/
│   │   ├── integration/
│   │   └── e2e/
│   └── frontend/
│       ├── unit/
│       └── e2e/
|    e2e/
|   integration/
|   security/
|   performance/
|   contracts/
│
├── database/
│
├── env/
│   ├── dev.env
│   ├── prod.env
│   ├── local.env
│
├── docker-compose.yaml
└── readme.md
```