# nice working with docker

```
wordium/
│
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