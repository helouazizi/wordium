
# 🌍 Environment Configuration Guide

Wordium follows the **Twelve-Factor App** methodology for configuration. Currently, we utilize a **Distributed Environment Strategy** where each microservice and database maintains its own scoped `.env` file.

> **Note:** We are planning to migrate to **Spring Cloud Config Server** in future releases for centralized configuration management.

---

## 🚀 The Quick Start (Automated)

Instead of manually searching for every `.env.example` file, you can run this one-liner in your terminal (Linux/macOS) to initialize all environment files at once:

We provide an automation script to initialize all environment variables across the microservices and databases:

```bash
chmod +x setup.sh
./setup.sh
```

*This command finds every `.env.example` in the project and creates a `.env` copy if it doesn't already exist.*

---

## 📋 Configuration Map

If you prefer manual setup, here is a checklist of every directory requiring a `.env` file:

### 1. Root Configuration

- **Location:** `./.env`
- **Purpose:** Controls Docker Compose ports and global infrastructure settings (Kafka, Eureka, Redis).

### 2. Database Layer

Each database instance requires its own credentials:

- [ ] `database/auth/.env`
- [ ] `database/users/.env`
- [ ] `database/posts/.env`
- [ ] `database/wsgateway/.env`

### 3. Backend Services

- [ ] `backend/auth/.env`
- [ ] `backend/users/.env`
- [ ] `backend/posts/.env`
- [ ] `backend/wsgateway/.env`
- [ ] `backend/api-gateway/.env`

---

## 🖼️ Media Management (Cloudinary)

Wordium uses **Cloudinary** to handle image and video uploads for user profiles and blog posts.

> [!IMPORTANT]
> **Action Required for Media Features:**
> The `.env.example` files contain **placeholder/fake** Cloudinary credentials.
>
> - **To just explore the code:** You can proceed with the fake credentials. The backend and database will run perfectly.
> - **To use images/videos:** You must create a free [Cloudinary account](https://cloudinary.com/) and replace the placeholders in `backend/posts-service/.env` (and any other service using media) with your real `CLOUD_NAME`, `API_KEY`, and `API_SECRET`.
>
> **Note:** If you use fake credentials, any attempt to upload an image via the Angular UI will result in a `500 Internal Server Error` or a "Service Integration Error" in the browser console.

---

## 🔑 Key Variables Reference

| Variable | Importance | Example Value |
| :--- | :--- | :--- |
| `EUREKA_URL` | How services find the registry | `http://eureka-server:8761/eureka` |
| `POSTGRES_HOST` | Database connection string | `psql_auth_db` (Docker) or `localhost` |
| `JWT_SECRET` | Signing key for Auth | *Must be 32+ characters in production* |
| `KAFKA_BOOTSTRAP` | Messaging entry point | `kafka:29092` |

---

## 🛡️ Security Best Practices

1. **Never Commit `.env`:** All `.env` files are already included in the `.gitignore`. **Do not force-add them.**
2. **Environment Isolation:** Each database has a unique `POSTGRES_DB` name and password. This ensures that a breach in the `Posts` service does not compromise the `Auth` service credentials.
3. **Production vs Dev:** For local development, the `.env.example` files are pre-configured to work with the `docker-compose.yaml` defaults.

---

## 🛠️ Troubleshooting

**Issue: Services cannot connect to PostgreSQL.**

- **Check:** Ensure the `POSTGRES_PORT` in your root `.env` matches the port mapping in the specific database `.env`.
- **Docker Tip:** If you change a `.env` file after the containers are running, you must restart them:

  ```bash
  docker-compose up -d --force-recreate
  ```
