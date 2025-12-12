# 01Blog Microservices Breakdown

We can divide the backend into several microservices:

- **Auth-Service** → handles authentication and role management (JWT)
- **User-Service** → manages user profiles, subscriptions, and public blocks
- **Post-Service** → manages posts, media, likes, and comments
- **Notification-Service** → handles notifications for subscriptions and interactions
- **Report-Service** → manages user reports and admin moderation
- **Gateway-Service** → API Gateway with JWT validation and routing
- **Admin-Service** → optional (admin endpoints can live in other services with role checks)

---

## 1️⃣ Auth-Service

**Responsibilities:**
- Register users (USER / ADMIN)
- Login users
- JWT token generation and validation
- Password hashing and secure storage

### Endpoints

| HTTP   | Endpoint          | Request Body                                      | Response                              | Role         |
|--------|-------------------|---------------------------------------------------|---------------------------------------|--------------|
| POST   | `/auth/signup`    | `{email, password, name, bio, avatar, location}`  | `{token, userId, role}`               | PUBLIC       |
| POST   | `/auth/login`     | `{email, password}`                               | `{token, userId, role}`               | PUBLIC       |
| GET    | `/auth/me`        | –                                                 | `{userId, name, email, role}`         | USER / ADMIN |

**Notes:**
- JWT contains `sub` (userId) and `role`
- API Gateway adds `X-USER-ID` and `X-USER-ROLE` headers for downstream services

---

## 2️⃣ User-Service

**Responsibilities:**
- Manage user profiles
- Public user block pages
- Subscriptions (follow/unfollow)
- Update profile info updates
- Fetch other users’ public info

### Endpoints

| HTTP   | Endpoint                     | Request Body                           | Response                                    | Role         |
|--------|------------------------------|----------------------------------------|---------------------------------------------|--------------|
| POST   | `/users/create`              | `{email, name, bio, avatar, location}` | `{id, role}`                                 | INTERNAL     |
| GET    | `/users/{id}`                | –                                      | `{id, name, bio, avatar, location, postsCount}` | USER / ADMIN |
| GET    | `/users/me`                  | –                                      | Full profile + subscriptions                 | USER         |
| PUT    | `/users/me`                  | `{name, bio, avatar, location}`        | `{updatedProfile}`                           | USER         |
| POST   | `/users/{id}/subscribe`      | –                                      | `{success: true}`                            | USER         |
| DELETE | `/users/{id}/unsubscribe`    | –                                      | `{success: true}`                            | USER         |
| GET    | `/users/{id}/subscribers`    | –                                      | `[{userId, name}]`                           | USER / ADMIN |

**Notes:**
- Internal endpoints used by Auth-Service / Post-Service via internal token
- Subscriptions trigger events to Notification-Service

---

## 3️⃣ Post-Service

**Responsibilities:**
- Create, edit, delete posts
- Like / unlike posts
- Comment on posts
- Media upload (images/videos)
- Serve personalized feeds (from subscriptions)

### Endpoints

| HTTP   | Endpoint                  | Request Body                       | Response                                   | Role               |
|--------|---------------------------|------------------------------------|--------------------------------------------|--------------------|
| POST   | `/posts`                  | `{title, description, media}`      | `{postId, timestamp}`                      | USER               |
| GET    | `/posts/{id}`            | –                                  | Full post + likes + comments               | USER / ADMIN       |
| PUT    | `/posts/{id}`             | `{title, description, media}`      | `{updatedPost}`                            | USER (owner only)  |
| DELETE | `/posts/{id}`             | –                                  | `{success: true}`                          | USER (owner) / ADMIN |
| GET    | `/feeds`                  | –                                  | `[posts]` (from subscriptions)             | USER               |
| POST   | `/posts/{id}/like`        | –                                  | `{success: true, likesCount}`              | USER               |
| POST   | `/posts/{id}/comment`     | `{text}`                           | `{commentId, timestamp}`                   | USER               |
| GET    | `/posts/{id}/comments`    | –                                  | `[comments]`                               | USER / ADMIN       |

**Notes:**
- Media stored in filesystem or cloud storage (S3, etc.)
- Comments can use WebSockets for real-time updates

---

## 4️⃣ Notification-Service

**Responsibilities:**
- Notify subscribers when someone they follow posts
- Notify on likes and comments
- Mark notifications as read/unread

### Endpoints

| HTTP   | Endpoint                     | Request Body | Response              | Role |
|--------|------------------------------|--------------|-----------------------|------|
| GET    | `/notifications`             | –            | `[notifications]`     | USER |
| POST   | `/notifications/read/{id}`   | –            | `{success: true}`     | USER |
| POST   | `/notifications/read-all`    | –            | `{success: true}`     | USER |

**Notes:**
- Preferably WebSocket-based for real-time push
- Triggered asynchronously by Post-Service and User-Service events

---

## 5️⃣ Report-Service

**Responsibilities:**
- Allow users to report inappropriate content or users
- Admin dashboard to review and act on reports (ban user, remove post)

### Endpoints

| HTTP   | Endpoint                   | Request Body                           | Response                  | Role  |
|--------|----------------------------|----------------------------------------|---------------------------|-------|
| POST   | `/reports`                 | `{userId?, postId?, reason}`           | `{reportId, timestamp}`   | USER  |
| GET    | `/reports`                 | –                                      | `[reports]`               | ADMIN |
| POST   | `/reports/{id}/ban-user`   | –                                      | `{success: true}`         | ADMIN |
| POST   | `/reports/{id}/remove-post`| –                                      | `{success: true}`         | ADMIN |

---

## 6️⃣ API Gateway

**Responsibilities:**
- JWT verification
- Extract userId & role
- Add `X-USER-ID` and `X-USER-ROLE` headers
- Route requests to correct microservice
- Handle internal service-to-service authentication

### Example Routes

| Path                  | Service           |
|-----------------------|-------------------|
| `/api/v1/auth/**`     | Auth-Service      |
| `/api/v1/users/**`    | User-Service      |
| `/api/v1/posts/**`    | Post-Service      |
| `/api/v1/notifications/**` | Notification-Service |
| `/api/v1/reports/**`  | Report-Service    |

### JWT Flow
1. Client sends request with `Authorization: Bearer <token>`
2. Gateway verifies JWT → extracts userId & role
3. Adds headers: `X-USER-ID`, `X-USER-ROLE`
4. Forwards request to the appropriate service

---

## 7️⃣ Admin-Service (Optional)

Can be a separate service or admin routes can be implemented inside each service with role checks.

Possible admin features:
- View all reports
- Ban / unban users
- Delete posts
- Analytics (total users, posts, most reported users, etc.)

---

## Database Design (Simplified)

```sql
User          → id, name, email, password_hash, bio, avatar, location, role, created_at
Post          → id, author_id, title, description, media[], timestamp
Comment       → id, post_id, author_id, text, timestamp
Like          → id, post_id, user_id (unique constraint on post_id + user_id)
Subscription  → id, subscriber_id, subscribed_to_id (unique)
Notification  → id, user_id, type, message, related_id, read, timestamp
Report        → id, reporter_id, target_user_id?, target_post_id?, reason, status, timestamp