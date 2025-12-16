src/main/java/com/wordium/wsgateway/
├── common/                   # Shared configs, utils, and base services
│   ├── config/
│   │   ├── RedisConfig.java
│   │   └── WebSocketConfig.java
│   └── websocket/            # Base WS helpers if needed
├── notifications/            # Notifications feature
│   ├── controller/           # Optional REST endpoints
│   ├── dto/
│   │   └── NotificationEvent.java
│   ├── events/
│   │   └── NotificationsEventListener.java
│   ├── model/
│   │   └── Notification.java
│   ├── repo/
│   │   └── NotificationsRepo.java
│   └── service/
│       ├── NotificationsService.java
│       ├── NotificationCacheService.java
│       └── NotificationSocketService.java
├── chat/                     # Chat feature
│   ├── controller/
│   ├── dto/
│   ├── events/
│   ├── model/
│   ├── repo/
│   └── service/
├── events/                   # Generic events that multiple features may subscribe to
│   └── KafkaEventListeners.java
└── WsgatewayApplication.java
