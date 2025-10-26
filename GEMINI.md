# Project Overview

This is an Android application built with Kotlin and Jetpack Compose. The project is a CRM (Customer Relationship Management) tool that allows operators to manage clients and communicate with them through chat and campaigns. It uses Firebase for backend services, including Authentication, Cloud Firestore, and Firebase Cloud Messaging. The app is structured following the MVVM (Model-View-ViewModel) architecture.

## Building and Running

The project can be built and run using Gradle.

*   **Prerequisites:**
    *   A `google-services.json` file from a Firebase project with Authentication, Cloud Firestore, and Cloud Messaging enabled must be placed in the `app/` directory.

*   **Build the project:**
    ```bash
    ./gradlew build
    ```
*   **Run the application:**
    You can run the application on an emulator or a physical device using Android Studio or by running the following command:
    ```bash
    ./gradlew installDebug
    ```
*   **Run tests:**
    ```bash
    ./gradlew test
    ```

## Development Conventions

The project uses Kotlin and Jetpack Compose for UI development. The code is structured in a standard Android project layout, following the MVVM pattern.

*   **Main application code:** `app/src/main/java/br/com/savedra/challengecrm/`
*   **UI Theme:** `app/src/main/java/br/com/savedra/challengecrm/ui/theme/`
*   **Unit tests:** `app/src/test/java/br/com/savedra/challengecrm/`
*   **Instrumentation tests:** `app/src/androidTest/java/br/com/savedra/challengecrm/`
*   **Architecture:** The project follows the MVVM (Model-View-ViewModel) architecture.
    *   **Models:** Data classes representing the application's data (e.g., `User`, `Message`).
    *   **Views:** Jetpack Compose functions that constitute the UI of the application.
    *   **ViewModels:** Classes that hold the business logic and expose the state to the UI.
*   **Dependencies:** The project uses several libraries, including:
    *   Jetpack Compose for the UI.
    *   Firebase SDK for backend services.
    *   ViewModel and Navigation components from the Android Jetpack library.
