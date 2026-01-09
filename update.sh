#!/bin/zsh

cd backend/auth && ./mvnw package -DskipTests
cd ..
cd ..
cd backend/api-gatway && ./mvnw package -DskipTests
cd ..
cd ..
cd backend/users && ./mvnw package -DskipTests
cd ..
cd ..
cd backend/posts && ./mvnw package -DskipTests
cd ..
cd ..
cd backend/wsgateway && ./mvnw package -DskipTests
cd ..
cd ..