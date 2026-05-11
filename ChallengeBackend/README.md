# Challenge WTC - Backend

This is the Spring Boot backend for the Challenge WTC project (Sprint 2).

## Technologies
- Java 17
- Spring Boot 3
- Spring Data MongoDB
- Spring Security (JWT)
- Docker

## Setup

### 1. Database
Run the following command in the root of the repository to start MongoDB:
```bash
docker-compose up -d
```
The database will be available at `localhost:27017`.
You can access Mongo Express (GUI) at `http://localhost:8081`.

### 2. Run the Application
Navigate to the `ChallengeBackend` directory and run:
```bash
./gradlew bootRun
```
(If on Windows, use `gradlew.bat bootRun`)

## Project Structure
- `src/main/java/br/com/savedra/challengebackend/controller`: API Endpoints
- `src/main/java/br/com/savedra/challengebackend/service`: Business Logic
- `src/main/java/br/com/savedra/challengebackend/repository`: Data Access (MongoDB)
- `src/main/java/br/com/savedra/challengebackend/model`: Domain Entities
- `src/main/java/br/com/savedra/challengebackend/config`: Configuration (Security, etc.)
