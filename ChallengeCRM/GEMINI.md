# Project Overview

Challenge CRM is a native Android application designed for Customer Relationship Management, featuring modules for operators and clients. It facilitates real-time communication, campaign management, and customer tracking.

## Core Technologies
- **Language:** Kotlin (2.0+)
- **UI Framework:** Jetpack Compose with Material 3
- **Architecture:** MVVM (Model-View-ViewModel)
- **Backend Services:** Custom Spring Boot (REST + WebSockets) & MongoDB Atlas (Cloud)
- **Security:** JWT (JSON Web Token) for authentication and role-based access.
- **Navigation:** Jetpack Navigation Compose
- **Build System:** Gradle (Kotlin DSL) with Version Catalog

## Project Structure

The project is a monorepo containing:
- **`ChallengeBackend/`**: Spring Boot 3 Java application.
- **`ChallengeCRM/`**: Native Android application.
- **`FASE-2/`**: Documentation and sprint requirements.

## Integrated Development Environment

### ChallengeBackend
- Recommended: IntelliJ IDEA or VS Code.
- Requirements: Java 17+, MongoDB Atlas cluster.
- Run: `./gradlew bootRun`

### ChallengeCRM
- Required: **Android Studio**.
- **IMPORTANT:** Open the `ChallengeCRM` folder specifically in Android Studio to enable Gradle Sync and Run features.
- Build: `./gradlew assembleDebug`

### Emulator Troubleshooting
If the Android Emulator (e.g., Pixel 8) terminates immediately on AMD GPUs:
1. **Disable Vulkan:** Create/Update `~/.android/advancedFeatures.ini` with `Vulkan = off` and `GLDirectMem = on`.
2. **Graphics Mode:** In Device Manager, set Graphics to `Software - GLES 2.0`.

## Sprint 2: Roadmap & Objectives

The focus of Sprint 2 is to replace existing mocks and direct Firebase interactions with a robust, custom backend and integrate advanced communication features.

### Backend Implementation
- [x] **Framework:** Java (Spring Boot 3)
- [x] **Database:** NoSQL (MongoDB Atlas - Cloud)
- [x] **Authentication:** JWT-based Security with Role-based access (Operator vs. Client)
- [x] **Data Integrity:** Automatic duplicate sanitation and unique email indexing.
- [ ] **CRM & Relationship:** CRUD for Clients and Segments; "Perfil 360" query.
- [ ] **Chat & Messaging:** 1:1 and Segment-based message orchestration with status tracking.
- [ ] **Governance:** Centralized logging and auditing for all operations.

### App Integration & Features
- [x] **Auth Migration:** Login and Register flows fully integrated with the REST API.
- [x] **UI Foundations:** High-contrast theme and Notch (Camera hole) compatibility.
- [ ] **Feature Testing:** Comprehensive test of Banners, Campaigns, Invites, and Promotions.
- [ ] **Bug: Ghost Registration:** Investigate why users created in the UI are not appearing in the list and causing timeouts on re-login.
- [ ] **UI Polish:** Refine layouts and fix remaining frontend spacing/alignment issues.

### Success Criteria
- Functional APK integrated with a live, authenticated backend.
- Comprehensive technical documentation (Architecture diagram, API specifications, NoSQL model).
- Code quality adhering to clean architecture and naming conventions.
