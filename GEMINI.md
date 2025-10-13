# Project Overview

This is an Android application built with Kotlin and Jetpack Compose. The project is set up as a standard Gradle project. The app's package name is `br.com.savedra.challengecrm`.

## Building and Running

The project can be built and run using Gradle.

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

The project uses Kotlin and Jetpack Compose for UI development. The code is structured in a standard Android project layout.

*   **Main application code:** `app/src/main/java/br/com/savedra/challengecrm/`
*   **UI Theme:** `app/src/main/java/br/com/savedra/challengecrm/ui/theme/`
*   **Unit tests:** `app/src/test/java/br/com/savedra/challengecrm/`
*   **Instrumentation tests:** `app/src/androidTest/java/br/com/savedra/challengecrm/`
