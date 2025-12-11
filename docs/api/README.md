# API Documentation

This folder contains the Swagger/OpenAPI specification.

## Swagger UI

Open in browser:

```

http://localhost:8080/swagger-ui/index.html

```


## Microservices Covered
- User Service
- Product Service
- Order Service
- API Gateway

## Angular Client Generation

```
openapi-generator-cli generate
-i http://localhost:8080/v3/api-docs

-g typescript-angular
-o src/app/api

```


## Notes
- All endpoints are versioned (e.g., `/api/v1/users`)
- Response and request models are defined in `components/schemas`
