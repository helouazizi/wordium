#!/bin/zsh

cd backend/auth && rm -rf ./target && ./mvnw package -DskipTests
cd ..
cd ..
cd backend/api-gatway && rm -rf ./target && ./mvnw package -DskipTests
cd ..
cd ..
cd backend/users && rm -rf ./target && ./mvnw package -DskipTests
cd ..
cd ..
cd backend/posts && rm -rf ./target && ./mvnw package -DskipTests
cd ..
cd ..
cd backend/wsgateway && rm -rf ./target && ./mvnw package -DskipTests
cd ..
cd ..
cd backend/eurika-server && rm -rf ./target && ./mvnw package -DskipTests
cd ..
cd ..

cd backend/test-service && rm -rf ./target && ./mvnw package -DskipTests
cd ..
cd ..