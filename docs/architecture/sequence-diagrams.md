sequenceDiagram
    participant U as User
    participant F as Frontend
    participant A as API Gateway
    participant S as Auth Service
    participant D as Database

    U->>F: Fill registration form
    F->>A: POST /register
    A->>S: Validate & process
    S->>D: Insert user record
    D->>S: OK
    S->>A: Return success
    A->>F: 201 Created
    F->>U: Show success message
