# Threat Model

## Assets
- User data
- Orders
- JWT tokens
- API endpoints

## Threats
- Unauthorized access
- SQL injection
- XSS / CSRF
- Misconfigured CORS

## Mitigations
- JWT with short expiration + refresh token
- ORM + prepared statements
- Input validation & sanitization
- CORS configured only for frontend domains
