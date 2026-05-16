# Project Overview

Challenge CRM is a native Android application designed for Customer Relationship Management, featuring modules for operators and clients. It facilitates real-time communication, campaign management, and customer tracking.

## Core Technologies
- **Language:** Kotlin (2.0+)
- **UI Framework:** Jetpack Compose with Material 3
- **Architecture:** MVVM (Model-View-ViewModel)
- **Backend Services:** Firebase (Authentication, Cloud Firestore, Cloud Storage, Firebase Cloud Messaging)
- **Navigation:** Jetpack Navigation Compose
- **Build System:** Gradle (Kotlin DSL) with Version Catalog

## Project Structure

The source code is located in `app/src/main/java/br/com/savedra/challengecrm/` and follows a clear separation of concerns:

- **`data/repository/`**: Contains repository classes that abstract data sources (primarily Firebase Firestore and Auth).
- **`model/`**: Data classes representing the domain entities (User, Chat, Message, Campaign, etc.).
- **`viewmodel/`**: ViewModels that manage UI state and interact with repositories.
- **`ui/view/`**: Jetpack Compose-based UI components.
    - `screens/`: Top-level screens (e.g., `LoginScreen`, `OperatorHomeScreen`).
    - `modals/`: Bottom sheet or full-screen modal components for creating content.
    - `dialogs/`: Reusable dialog components (Date/Time pickers, Notifications).
- **`navigation/`**: Centralized navigation logic using `NavHost`.
- **`di/`**: Dependency injection configuration (if applicable).

## Building and Running

### Prerequisites
- **`google-services.json`**: Must be present in the `app/` directory. This file contains the Firebase configuration.
- **JDK 17+**: Required for the current Gradle configuration.

### Common Commands
- **Assemble Debug APK:**
  ```bash
  ./gradlew assembleDebug
  ```
- **Install and Run on Device:**
  ```bash
  ./gradlew installDebug
  ```
- **Run Unit Tests:**
  ```bash
  ./gradlew test
  ```
- **Run Instrumented Tests:**
  ```bash
  ./gradlew connectedAndroidTest
  ```
- **Clean Build:**
  ```bash
  ./gradlew clean build
  ```

## Development Conventions

- **State Management:** Use `mutableStateOf` or `MutableStateFlow` within ViewModels to expose state to the UI.
- **UI Architecture:** All UI should be built using Jetpack Compose. Avoid XML layouts.
- **Data Access:** Never access Firestore directly from the UI; always go through a Repository and ViewModel.
- **Naming:**
    - Screens: `[Name]Screen.kt`
    - ViewModels: `[Name]ViewModel.kt`
    - Repositories: `[Name]Repository.kt`
- **Resources:** Use the Material 3 theme defined in `ui/theme/`.

## Integrated Development Environment

The project is structured as a monorepo with two main components:
- **`ChallengeBackend`**: Spring Boot application. Best managed in IntelliJ IDEA or VS Code.
- **`ChallengeCRM`**: Native Android application. Must be opened as a standalone project in Android Studio for full functionality (Sync, Run, Layout Preview).

### Emulator Troubleshooting
If the Android Emulator (e.g., Pixel 8) terminates immediately:
1. **Disable Vulkan:** Create/Update `~/.android/advancedFeatures.ini` with `Vulkan = off` and `GLDirectMem = on`.
2. **Graphics Mode:** In Device Manager, set Graphics to `Software - GLES 2.0` if hardware acceleration fails on AMD Radeon GPUs.
3. **Lock Files:** Ensure no `.lock` files exist in the `.avd` directory before launching.

## Sprint 2: Roadmap & Objectives

The focus of Sprint 2 was to replace existing mocks and direct Firebase interactions with a robust, custom backend and integrate advanced communication features.

### Backend Implementation
- [x] **Framework:** Java (Spring Boot 3)
- [x] **Database:** NoSQL (MongoDB Atlas - Cloud)
- [x] **Authentication:** JWT-based Security with Role-based access (Operator vs. Client)
- [x] **CRM & Relationship:** CRUD for Clients and Segments; "Perfil 360" query.
- [x] **Chat & Messaging:** 1:1 and Segment-based message orchestration with status tracking.
- [x] **Campaigns Express:** Immediate promotional message dispatch with internal deeplinks.
- [x] **Governance:** Centralized logging and auditing for all operations.
- [x] **Real-time:** WebSocket implementation for instant chat updates.
- [x] **Media:** Image upload and viewing support.

### App Integration & Features
- [x] **API Migration:** All repository logic updated to call the new Spring Boot REST APIs via Retrofit.
- [x] **Real-time Communication:** Integrated WebSockets in the Android app for instant updates.
- [x] **Media Support:** Integrated image handling.
- [x] **Status Tracking:** Message status (sent, delivered, etc.) fully implemented.

### Success Criteria
- Functional APK integrated with a live, authenticated backend.
- Comprehensive technical documentation (Architecture diagram, API specifications, NoSQL model).
- Code quality adhering to clean architecture and naming conventions.
